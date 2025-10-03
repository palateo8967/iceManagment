package com.icerojects.icemanagment.domain.model

/**
 * Model to handle multi-step registration state
 */
data class RegistrationState(
    // Step 1: Personal information
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val phone: String = "",
    
    // Step 2: Ice cream shop information
    val shopName: String = "",
    val street: String = "",
    val streetNumber: String = "",
    val shopType: String = "",
    
    // Navigation control
    val currentStep: Int = 1,
    val totalSteps: Int = 2,
    
    // Terms and conditions
    val acceptTerms: Boolean = false
)