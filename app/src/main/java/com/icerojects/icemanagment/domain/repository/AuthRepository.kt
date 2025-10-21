package com.icerojects.icemanagment.domain.repository

import com.icerojects.icemanagment.domain.model.AuthOperationResult
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    fun getCurrentUserId(): String?
    fun getCurrentUserEmail(): String?
    fun getAuthState(): Flow<Boolean>
    suspend fun signIn(email: String, password: String): AuthOperationResult
    suspend fun signUp(email: String, password: String): AuthOperationResult
    suspend fun signOut()
}