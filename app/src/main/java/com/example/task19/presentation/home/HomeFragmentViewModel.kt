package com.example.task19.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.task19.common.helper.resource.Resource
import com.example.task19.domain.model.toPresentation
import com.example.task19.domain.use_case.UserUseCase
import com.example.task19.presentation.model.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeFragmentViewModel @Inject constructor(private val userUseCase: UserUseCase) :
    ViewModel() {

    private val _viewState = MutableStateFlow(HomeViewState())
    val viewState get() = _viewState.asStateFlow()

    init {
        getUsersList()
    }

    fun onEvent(event: HomeFragmentEvents) {
        when (event) {
            is HomeFragmentEvents.SelectItemEvent -> selectItem(event.user, event.isSelected)
            is HomeFragmentEvents.SelectionModeEvent -> selectionMode(event.isSelectionModeStarted)
            is HomeFragmentEvents.DeleteUserEvent -> deleteItems()
        }
    }

    private fun getUsersList() = viewModelScope.launch {
        _viewState.update { it.copy(isLoading = true) }

        userUseCase.getUsersList().collectLatest { resource ->
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

    private fun selectItem(user: User, isSelected: Boolean) {
        if (isSelected) {
            getItemsById().add(user)
        } else {
            getItemsById().remove(user)
            if (getItemsById().isEmpty()) {
                _viewState.update { it.copy(isMultiSelectEnabled = false) }
            }
        }
    }

    private fun deleteItems() = viewModelScope.launch {
        val selectedUsers = _viewState.value.selectedUsers
        val updatedUsersList = _viewState.value.usersList.toMutableList()

        updatedUsersList.retainAll { user ->
            user !in selectedUsers
        }

        selectedUsers.forEach { user ->
            userUseCase.deleteUser(user.id!!).collectLatest { }
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

    private fun selectionMode(isSelectionModeStarted: Boolean) {
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

sealed class HomeFragmentEvents {
    data class SelectItemEvent(val user: User, var isSelected: Boolean) : HomeFragmentEvents()
    data class SelectionModeEvent(var isSelectionModeStarted: Boolean) : HomeFragmentEvents()
    data object DeleteUserEvent : HomeFragmentEvents()
}