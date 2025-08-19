package com.icerojects.icemanagment.data.repository

import android.R.attr.password
import com.google.firebase.auth.FirebaseUser
import com.icerojects.icemanagment.data.remote.auth.FirebaseAuthManager
import com.icerojects.icemanagment.domain.model.AuthOperationResult
import com.icerojects.icemanagment.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepositoryImpl @Inject constructor(

    private val firebaseAuthManager: FirebaseAuthManager

) : AuthRepository{

    override fun getCurrentUser(): FirebaseUser? {
        return firebaseAuthManager.getCurrentUser()
    }

    override fun getAuthState(): Flow<FirebaseUser?> {
        return firebaseAuthManager.getAuthState()
    }

    override suspend fun signIn(email: String, password: String): AuthOperationResult {
        return firebaseAuthManager.signIn(email, password)
    }

    override suspend fun signUp(email: String, password: String): AuthOperationResult {
        return firebaseAuthManager.signUp(email,password)
    }

    override suspend fun signOut(){
        firebaseAuthManager.signOut()
    }

}