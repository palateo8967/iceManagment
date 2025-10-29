package com.icerojects.icemanagment.data.repository

import com.icerojects.icemanagment.data.remote.auth.FirebaseAuthManager
import com.icerojects.icemanagment.data.remote.firestore.FirestoreManager
import com.icerojects.icemanagment.domain.model.AuthOperationResult
import com.icerojects.icemanagment.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val firebaseAuthManager: FirebaseAuthManager,
    private val firestoreManager: FirestoreManager
) : AuthRepository {

    override fun getCurrentUserId(): String? {
        return firebaseAuthManager.getCurrentUser()?.uid
    }

    override fun getCurrentUserEmail(): String? {
        return firebaseAuthManager.getCurrentUser()?.email
    }

    override fun getAuthState(): Flow<Boolean> {
        return firebaseAuthManager.getAuthState().map { user -> user != null }
    }

    override suspend fun signIn(email: String, password: String): AuthOperationResult {
        return firebaseAuthManager.signIn(email, password)
    }

    override suspend fun signUp(email: String, password: String): AuthOperationResult {
        return firebaseAuthManager.signUp(email, password)
    }

    override suspend fun signOut() {
        firebaseAuthManager.signOut()
    }
}