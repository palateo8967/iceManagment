package com.icerojects.icemanagment.domain.model

import com.google.firebase.auth.FirebaseUser

//Wrapper para manejar los dos posibles resultados de la autenticacion
sealed class AuthOperationResult {

    data class Succes(val user: FirebaseUser?): AuthOperationResult()
    data class Error(val errorMessage: String): AuthOperationResult()


}
