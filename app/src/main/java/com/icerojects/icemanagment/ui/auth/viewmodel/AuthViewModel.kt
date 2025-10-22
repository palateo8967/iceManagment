package com.icerojects.icemanagment.ui.auth.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.icerojects.icemanagment.domain.model.AuthOperationResult
import com.icerojects.icemanagment.domain.use_case.GetAuthStateUseCase
import com.icerojects.icemanagment.domain.use_case.SignInUseCase
import com.icerojects.icemanagment.domain.use_case.SignOutUseCase
import com.icerojects.icemanagment.domain.use_case.UserBasicInfo
import com.icerojects.icemanagment.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class AuthUiState {
    object Idle : AuthUiState()
    object Loading : AuthUiState()
    data class Success(val userId: String, val email: String?) : AuthUiState()
    data class Error(val message: String) : AuthUiState()
}

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val signInUseCase: SignInUseCase,
    private val signOutUseCase: SignOutUseCase,
    private val getAuthStateUseCase: GetAuthStateUseCase
) : ViewModel() {

    private val _authState = MutableStateFlow<Boolean>(false)
    val authState: StateFlow<Boolean> = _authState

    private val _authUiState = mutableStateOf<AuthUiState>(AuthUiState.Idle)
    val authUiState: State<AuthUiState> = _authUiState

    val email = mutableStateOf("")
    val password = mutableStateOf("")

    init {
        getAuthStateUseCase().onEach { isAuthenticated ->
            _authState.value = isAuthenticated
            
            if (isAuthenticated) {
                _authUiState.value = AuthUiState.Success("", null)
            } else {
                _authUiState.value = AuthUiState.Idle
            }
        }.launchIn(viewModelScope)
    }

    fun signIn() {
        viewModelScope.launch {
            _authUiState.value = AuthUiState.Loading
            
            signInUseCase(email.value.trim(), password.value.trim()).collect { result ->
                _authUiState.value = when (result) {
                    is Resource.Success -> {
                        when (val authResult = result.data) {
                            is AuthOperationResult.Success -> 
                                AuthUiState.Success(authResult.userId, authResult.email)
                            is AuthOperationResult.Error -> 
                                AuthUiState.Error(authResult.errorMessage)
                            null -> AuthUiState.Error("Unknown error")
                        }
                    }
                    is Resource.Error -> AuthUiState.Error(result.message ?: "Unknown error")
                    is Resource.Loading -> AuthUiState.Loading
                }
            }
        }
    }

    fun signOut() {
        viewModelScope.launch {
            signOutUseCase().collect { result ->
                when (result) {
                    is Resource.Success -> {
                        email.value = ""
                        password.value = ""
                        _authUiState.value = AuthUiState.Idle
                    }
                    is Resource.Error -> {
                        _authUiState.value = AuthUiState.Error(result.message ?: "Error signing out")
                    }
                    is Resource.Loading -> {
                        _authUiState.value = AuthUiState.Loading
                    }
                }
            }
        }
    }

    fun resetFormAndUiState() {
        email.value = ""
        password.value = ""
        _authUiState.value = AuthUiState.Idle
    }
}