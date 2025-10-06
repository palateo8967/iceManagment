package com.icerojects.icemanagment.domain.model

/**
 * Model to handle multi-step registration state
 */
data class RegistrationState(
 
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val phone: String = "",
    
    val shopName: String = "",
    val street: String = "",
    val streetNumber: String = "",
    val shopType: String = "",

    val currentStep: Int = 1,
    val totalSteps: Int = 2,

    val acceptTerms: Boolean = false
)