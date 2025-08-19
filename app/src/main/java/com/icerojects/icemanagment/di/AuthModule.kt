package com.icerojects.icemanagment.di

import com.icerojects.icemanagment.data.remote.auth.FirebaseAuthManager
import com.icerojects.icemanagment.data.repository.AuthRepositoryImpl
import com.icerojects.icemanagment.domain.repository.AuthRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AuthModule {

    @Provides
    @Singleton
    fun provideFirebaseAuthManager(): FirebaseAuthManager {

        return FirebaseAuthManager()

    }

    @Provides
    @Singleton
    fun provideAuthRepository(firebaseAuthManager: FirebaseAuthManager): AuthRepository {

        return AuthRepositoryImpl(firebaseAuthManager)

    }

}