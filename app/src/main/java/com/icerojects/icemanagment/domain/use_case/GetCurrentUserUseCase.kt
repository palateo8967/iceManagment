package com.icerojects.icemanagment.domain.use_case

import com.google.firebase.auth.FirebaseUser
import com.icerojects.icemanagment.domain.repository.AuthRepository
import javax.inject.Inject

class GetCurrentUserUseCase @Inject constructor(

    private val repository: AuthRepository

){

    operator fun invoke(): FirebaseUser? {

        return repository.getCurrentUser()

    }
}