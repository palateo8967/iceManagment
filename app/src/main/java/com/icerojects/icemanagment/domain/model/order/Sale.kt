package com.icerojects.icemanagment.domain.model.order

import java.util.Date

/**
 * Domain model representing a completed sale
 */
data class Sale(
    val id: String = "",
    val saleNumber: Int = 0,
    val orderId: String = "",
    val customerName: String = "",
    val items: List<OrderItem> = emptyList(),
    val totalAmount: Double = 0.0,
    val profit: Double = 0.0,
    val paymentMethod: PaymentMethod = PaymentMethod.CASH,
    val date: Date = Date()
)

enum class PaymentMethod {
    CASH,
    CARD,
    BANK_TRANSFER,
    MERCADO_PAGO,
    QR,
    OTHER
}