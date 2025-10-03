package com.icerojects.icemanagment.domain.model

/**
 * Data model for user information
 */
data class UserData(
    val uid: String = "",
    val email: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val phone: String = "",
    val createdAt: Long = 0
)