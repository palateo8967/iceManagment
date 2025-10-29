package com.icerojects.icemanagment.domain.model

import java.util.Date

/**
 * Domain model representing a product in inventory
 */
data class Product(
    val id: String = "",
    val name: String = "",
    val categoryId: String = "",
    val categoryName: String = "", // For easier display without joins
    val quantity: Double = 0.0,
    val unit: UnitOfMeasure = UnitOfMeasure.UNIT,
    val minStock: Double = 0.0,
    val price: Double = 0.0,
    val createdAt: Date = Date()
)

enum class UnitOfMeasure {
    KG, UNIT
}