package com.icerojects.icemanagment.domain.repository

import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow

interface AuthRepository{

    fun getCurrentUser(): FirebaseUser?
    fun getAuthState(): Flow<FirebaseUser?>


}