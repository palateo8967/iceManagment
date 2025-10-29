package com.icerojects.icemanagment.ui.auth.viewmodel

sealed class RegistrationUiState {
    object Initial : RegistrationUiState()
    object Loading : RegistrationUiState()
    object Success : RegistrationUiState()
    data class Error(val message: String) : RegistrationUiState()
}