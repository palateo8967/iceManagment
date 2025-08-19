package com.icerojects.icemanagment.ui.auth

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import com.icerojects.icemanagment.data.remote.auth.FirebaseAuthManager
import com.icerojects.icemanagment.domain.model.AuthOperationResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.annotation.meta.When
import javax.inject.Inject

sealed class AuthUiState {

    object Idle : AuthUiState()
    object Loading : AuthUiState()
    data class Success(val user: FirebaseUser?) : AuthUiState()
    data class Error(val message: String) : AuthUiState()

}

@HiltViewModel
class AuthViewModel @Inject constructor(

    private val authManager: FirebaseAuthManager

) : ViewModel() {

    private val _authState = MutableStateFlow<FirebaseUser?>(null)
    val authState: StateFlow<FirebaseUser?> = _authState

    private val _authUiState = mutableStateOf<AuthUiState>(AuthUiState.Idle)
    val authUiState: State<AuthUiState> = _authUiState

    val email = mutableStateOf("")
    val password = mutableStateOf("")

    init {

        authManager.getAuthState()
            .onEach { user ->

                _authState.value = user

                if (user != null) {

                    _authUiState.value = AuthUiState.Success(user)

                } else {

                    _authUiState.value = AuthUiState.Idle

                }
            }
            .launchIn(viewModelScope)
    }


    fun signUp() {

        viewModelScope.launch {

            _authUiState.value = AuthUiState.Loading
            val result = authManager.signUp(email.value.trim(), password.value.trim())
            _authUiState.value = when (result){

                is AuthOperationResult.Succes -> AuthUiState.Success(result.user)
                is AuthOperationResult.Error -> AuthUiState.Error(result.errorMessage)

            }

        }

    }

    fun signOut(){

        viewModelScope.launch {

            authManager.signOut()
            email.value = ""
            password.value = ""
            _authUiState.value = AuthUiState.Idle

        }

    }

    fun resetUiState(){

        _authUiState.value = AuthUiState.Idle

    }

}