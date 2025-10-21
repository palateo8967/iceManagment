package com.icerojects.icemanagment.data.remote.firestore

import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirestoreManager @Inject constructor() {
    private val db: FirebaseFirestore = Firebase.firestore

    suspend fun saveNewUser(user: FirebaseUser?) {
        user?.let {
            val userDocument = mapOf(
                "uid" to it.uid,
                "email" to it.email,
                "createdAt" to System.currentTimeMillis()
            )

            try {
                db.collection("users").document(it.uid).set(userDocument).await()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}