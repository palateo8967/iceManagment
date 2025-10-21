package com.icerojects.icemanagment.domain.model

sealed class AuthOperationResult {
    data class Success(val userId: String, val email: String?): AuthOperationResult()
    data class Error(val errorMessage: String): AuthOperationResult()
}
