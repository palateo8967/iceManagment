package com.icerojects.icemanagment.domain.model

import com.google.firebase.auth.FirebaseUser

// Wrapper to handle the two possible authentication results
sealed class AuthOperationResult {

    data class Success(val user: FirebaseUser?): AuthOperationResult()
    data class Error(val errorMessage: String): AuthOperationResult()

}
