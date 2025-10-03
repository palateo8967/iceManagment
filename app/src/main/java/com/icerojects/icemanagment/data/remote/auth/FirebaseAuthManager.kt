package com.icerojects.icemanagment.data.remote.auth

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.icerojects.icemanagment.data.remote.firestore.FirestoreManager


import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await


import com.icerojects.icemanagment.domain.model.AuthOperationResult
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirebaseAuthManager @Inject constructor(

    private val firestoreManager: FirestoreManager


){

    private val auth: FirebaseAuth = Firebase.auth

    fun getCurrentUser(): FirebaseUser? {

        return auth.currentUser

    }

    fun getAuthState(): Flow<FirebaseUser?> = callbackFlow {

        val authStateListener = FirebaseAuth.AuthStateListener {firebaseAuth ->

            trySend(firebaseAuth.currentUser).isSuccess

        }

        auth.addAuthStateListener(authStateListener)
        awaitClose {auth.removeAuthStateListener(authStateListener)}

    }

    suspend fun signIn(email: String, password: String): AuthOperationResult {

        return try{

            val result = auth.signInWithEmailAndPassword(email, password).await()
            AuthOperationResult.Success(result.user)

        } catch (e: Exception){

            AuthOperationResult.Error(e.message ?: "Unknown error during sign in")

        }

    }

    suspend fun signUp(email: String, password: String): AuthOperationResult {

        return try{

            val result = auth.createUserWithEmailAndPassword(email, password).await()
            firestoreManager.saveNewUser(result.user)
            AuthOperationResult.Success(result.user)

        } catch (e: Exception){

            AuthOperationResult.Error(e.message ?: "Unknown error during sign up")

        }

    }

    suspend fun signOut(){

        try{

            auth.signOut()

        } catch (e: Exception){

            e.printStackTrace()

        }

    }

}