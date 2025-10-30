package com.icerojects.icemanagment.domain.use_case

import com.icerojects.icemanagment.domain.repository.AuthRepository
import javax.inject.Inject

class GetCurrentUserUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    operator fun invoke(): UserBasicInfo? {
        val userId = repository.getCurrentUserId()
        val email = repository.getCurrentUserEmail()
        
        return if (userId != null) {
            UserBasicInfo(userId, email)
        } else {
            null
        }
    }
}

data class UserBasicInfo(
    val id: String,
    val email: String?
)