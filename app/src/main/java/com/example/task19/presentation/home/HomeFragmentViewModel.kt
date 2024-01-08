package com.example.task19.presentation.home

import android.util.Log.d
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.task19.common.helper.resource.Resource
import com.example.task19.domain.model.toPresentation
import com.example.task19.domain.use_case.GetUserListUseCase
import com.example.task19.presentation.model.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeFragmentViewModel @Inject constructor(private val getUsersListUseCase: GetUserListUseCase) :
    ViewModel() {

    private val _viewState = MutableStateFlow(HomeViewState())
    val viewState get() = _viewState.asStateFlow()

    init {
        getUsersList()
    }

    private fun getUsersList() = viewModelScope.launch {
        _viewState.update { it.copy(isLoading = true) }

        getUsersListUseCase().collectLatest { resource ->
            when (resource) {
                is Resource.Success -> {
                    val userList = resource.data.map { it.toPresentation() }
                    _viewState.update {
                        it.copy(
                            usersList = userList,
                            isLoading = false
                        )
                    }
                }

                is Resource.Error -> _viewState.update {
                    it.copy(
                        error = resource.errorMessage,
                        isLoading = false,
                        isError = true
                    )
                }
            }
        }
    }

    fun selectItem(user: User, isSelected: Boolean) {
        if (isSelected) {
            getItemsById().add(user)
        } else {
            getItemsById().remove(user)
            if (getItemsById().isEmpty()) {
                _viewState.update { it.copy(isMultiSelectEnabled = false) }
            }
        }
    }

    fun deleteItems() {
        val selectedUsers = _viewState.value.selectedUsers
        val updatedUsersList = _viewState.value.usersList.toMutableList()

        updatedUsersList.retainAll { user ->
            user !in selectedUsers
        }

        _viewState.update {
            it.copy(
                usersList = updatedUsersList,
                selectedUsers = mutableListOf(),
                isLoading = false,
                isMultiSelectEnabled = false
            )
        }
    }

    fun selectionMode(isSelectionModeStarted: Boolean) {
        _viewState.update {
            it.copy(
                isMultiSelectEnabled = isSelectionModeStarted,
                selectedUsers = if (!isSelectionModeStarted) mutableListOf() else it.selectedUsers
            )
        }
    }

    private fun getItemsById(): MutableList<User> = _viewState.value.selectedUsers

}

data class HomeViewState(
    val usersList: List<User> = emptyList(),
    val selectedUsers: MutableList<User> = mutableListOf(),
    val error: String? = null,
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val isMultiSelectEnabled: Boolean = false
)