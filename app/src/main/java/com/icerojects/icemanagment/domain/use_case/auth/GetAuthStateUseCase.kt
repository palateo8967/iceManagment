package com.icerojects.icemanagment.domain.use_case

import com.icerojects.icemanagment.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAuthStateUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    operator fun invoke(): Flow<Boolean> {
        return repository.getAuthState()
    }
}