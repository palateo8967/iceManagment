package com.icerojects.icemanagment.domain.model

import java.util.Date

/**
 * Modelo de dominio para representar un producto en el inventario
 */
data class StockItem(
    val id: String = "",
    val nombre: String = "",
    val categoriaId: String = "",
    val categoriaNombre: String = "", // Para facilitar la visualización sin necesidad de joins
    val cantidad: Double = 0.0,
    val unidad: UnidadMedida = UnidadMedida.UNIDAD,
    val stockMinimo: Double = 0.0,
    val precio: Double = 0.0,
    val fechaCreacion: Date = Date()
)

enum class UnidadMedida {
    KG, UNIDAD
}