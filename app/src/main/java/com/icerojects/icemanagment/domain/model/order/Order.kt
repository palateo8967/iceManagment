package com.icerojects.icemanagment.domain.model.order

import java.util.Date

/**
 * Domain model representing an order
 */
data class Order(
    val id: String = "",
    val items: List<OrderItem> = emptyList(),
    val totalAmount: Double = 0.0,
    val createdAt: Date = Date(),
    val status: OrderStatus = OrderStatus.PENDING
)

enum class OrderStatus {
    PENDING,
    CONFIRMED,
    COMPLETED,
    CANCELLED
}