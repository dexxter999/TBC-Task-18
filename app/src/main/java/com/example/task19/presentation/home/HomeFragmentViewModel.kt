package com.example.task19.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.task19.common.helper.resource.Resource
import com.example.task19.domain.model.User
import com.example.task19.domain.repository.UsersRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeFragmentViewModel @Inject constructor(private val usersRepository: UsersRepository) :
    ViewModel() {

    private val _viewState = MutableStateFlow(HomeViewState())
    val viewState get() = _viewState.asStateFlow()

    init {
        getUsersList()
    }

    private fun getUsersList() = viewModelScope.launch {
        _viewState.update { it.copy(isLoading = true) }

        usersRepository.getUsersList().collectLatest { usersList ->
            _viewState.update { it.copy(usersList = usersList, isLoading = false) }
        }
    }
}


data class HomeViewState(
    val usersList: Resource<List<User>>? = null,
    val error: Exception? = null,
    val isLoading: Boolean = false,
    val isError: Boolean = false
)