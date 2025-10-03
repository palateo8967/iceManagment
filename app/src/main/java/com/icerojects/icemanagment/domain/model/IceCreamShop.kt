package com.icerojects.icemanagment.domain.model

/**
 * Data model for ice cream shop/branch information
 */
data class IceCreamShop(
    val id: String = "",
    val name: String = "",
    val street: String = "",
    val streetNumber: String = "",
    val type: String = "",
    val userId: String = "",
    val createdAt: Long = 0
)