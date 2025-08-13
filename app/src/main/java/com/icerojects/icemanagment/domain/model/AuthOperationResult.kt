package com.icerojects.icemanagment.domain.model

import com.google.firebase.auth.FirebaseUser

sealed class AuthOperationResult {

    data class Succes(val user: FirebaseUser?): AuthOperationResult()
    data class Error(val errorMessage: String): AuthOperationResult()


}