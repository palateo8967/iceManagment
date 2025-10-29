package com.icerojects.icemanagment.domain.model

import java.util.Date

/**
 * Domain model representing a product category
 */
data class Category(
    val id: String = "",
    val name: String = "",
    val createdAt: Date = Date()
)