package com.icerojects.icemanagment.domain.use_case

import com.icerojects.icemanagment.domain.model.AuthOperationResult
import com.icerojects.icemanagment.domain.repository.AuthRepository
import com.icerojects.icemanagment.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SignUpUseCase @Inject constructor(
    private  val repository: AuthRepository
) {

    operator fun invoke(email: String, password: String ): Flow<Resource<AuthOperationResult>> = flow {

        emit(Resource.Loading())

        try{

            val result = repository.signUp(email, password)
            emit(Resource.Success(result))

        } catch (e: Exception){

            emit(Resource.Error(e.localizedMessage ?: "Error inesperado en SignUpUseCase"))

        }

    }

}