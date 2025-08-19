package com.icerojects.icemanagment.domain.repository

import com.google.firebase.auth.FirebaseUser
import com.icerojects.icemanagment.domain.model.AuthOperationResult
import kotlinx.coroutines.flow.Flow

interface AuthRepository{

    fun getCurrentUser(): FirebaseUser?
    fun getAuthState(): Flow<FirebaseUser?>
    suspend fun signIn(email: String, passwotd: String): AuthOperationResult
    suspend fun signUp(email: String, passwotd: String): AuthOperationResult
    suspend fun signOut()

}