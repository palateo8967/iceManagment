package com.icerojects.icemanagment.domain.use_case

import com.icerojects.icemanagment.domain.repository.AuthRepository
import com.icerojects.icemanagment.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SignOutUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    operator fun invoke(): Flow<Resource<Unit>> = flow {
        emit(Resource.Loading())
        
        try {
            repository.signOut()
            emit(Resource.Success(Unit))
        } catch (e: Exception) {
            emit(Resource.Error(e.localizedMessage ?: "Error al cerrar sesión"))
        }
    }
}