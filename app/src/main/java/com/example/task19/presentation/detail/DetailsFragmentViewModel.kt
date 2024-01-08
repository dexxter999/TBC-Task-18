package com.example.task19.presentation.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.task19.common.helper.resource.Resource
import com.example.task19.domain.model.UserDomain
import com.example.task19.domain.use_case.GetUserDetailsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailsFragmentViewModel @Inject constructor(private val getUserDetailsUseCase: GetUserDetailsUseCase) :
    ViewModel() {
    private val _viewState = MutableStateFlow(DetailsViewState(isLoading = true))
    val viewState = _viewState.asStateFlow()


    fun getUserInfo(userId: Int) = viewModelScope.launch {
        _viewState.update { it.copy(isLoading = true) }

        getUserDetailsUseCase(userId).collectLatest { user ->
            _viewState.update { it.copy(userDomain = user, isLoading = false) }
        }
    }
}

data class DetailsViewState(
    val userDomain: Resource<UserDomain>? = null,
    val isLoading: Boolean = false
)