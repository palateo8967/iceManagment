package com.icerojects.icemanagment.domain.model

import java.util.Date

/**
 * Modelo de dominio para representar una categoría de productos
 */
data class Categoria(
    val id: String = "",
    val nombre: String = "",
    val fechaCreacion: Date = Date()
)