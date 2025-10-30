package com.icerojects.icemanagment.domain.model.order

/**
 * Domain model representing a cash payment with change calculation
 */
data class CashPayment(
    val totalAmount: Double = 0.0,
    val amountPaid: Double = 0.0,
    val change: Double = 0.0
)