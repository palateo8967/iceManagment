package com.icerojects.icemanagment.domain.model.order

/**
 * Domain model representing an item in an order
 */
data class OrderItem(
    val id: String = "",
    val productId: String = "",
    val productName: String = "",
    val quantity: Double = 0.0,
    val unitPrice: Double = 0.0,
    val totalPrice: Double = 0.0
)