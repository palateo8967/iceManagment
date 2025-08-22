package com.icerojects.icemanagment.di

import com.icerojects.icemanagment.data.remote.auth.FirebaseAuthManager
import com.icerojects.icemanagment.data.remote.firestore.FirestoreManager
import com.icerojects.icemanagment.data.repository.AuthRepositoryImpl
import com.icerojects.icemanagment.domain.repository.AuthRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
//Aca se declara como se crean y se inyectan las dependencias
@Module
//Alcance de vida
@InstallIn(SingletonComponent::class)
object AuthModule {
    @Provides
    @Singleton
    fun provideAuthRepository(

        firebaseAuthManager: FirebaseAuthManager,
        firestoreManager: FirestoreManager

    ): AuthRepository {

        return AuthRepositoryImpl(firebaseAuthManager, firestoreManager)

    }

}