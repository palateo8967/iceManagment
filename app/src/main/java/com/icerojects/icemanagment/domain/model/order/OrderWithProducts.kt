package com.icerojects.icemanagment.domain.model.order

import com.icerojects.icemanagment.domain.model.Category
import com.icerojects.icemanagment.domain.model.Product

/**
 * Domain model representing an order with its associated products and categories
 * Used for UI display and order management
 */
data class OrderWithProducts(
    val order: Order = Order(),
    val products: Map<String, Product> = emptyMap(),
    val categories: Map<String, Category> = emptyMap()
)