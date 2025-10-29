package com.icerojects.icemanagment.di

import com.icerojects.icemanagment.data.remote.auth.FirebaseAuthManager
import com.icerojects.icemanagment.data.remote.firestore.FirestoreManager
import com.icerojects.icemanagment.data.repository.AuthRepositoryImpl
import com.icerojects.icemanagment.domain.repository.AuthRepository
import com.icerojects.icemanagment.domain.use_case.*
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
    fun provideAuthRepository(
        firebaseAuthManager: FirebaseAuthManager,
        firestoreManager: FirestoreManager
    ): AuthRepository {
        return AuthRepositoryImpl(firebaseAuthManager, firestoreManager)
    }
    
    @Provides
    @Singleton
    fun provideSignInUseCase(repository: AuthRepository): SignInUseCase {
        return SignInUseCase(repository)
    }
    
    @Provides
    @Singleton
    fun provideSignUpUseCase(repository: AuthRepository): SignUpUseCase {
        return SignUpUseCase(repository)
    }
    
    @Provides
    @Singleton
    fun provideSignOutUseCase(repository: AuthRepository): SignOutUseCase {
        return SignOutUseCase(repository)
    }
    
    @Provides
    @Singleton
    fun provideGetCurrentUserUseCase(repository: AuthRepository): GetCurrentUserUseCase {
        return GetCurrentUserUseCase(repository)
    }
    
    @Provides
    @Singleton
    fun provideGetAuthStateUseCase(repository: AuthRepository): GetAuthStateUseCase {
        return GetAuthStateUseCase(repository)
    }
}