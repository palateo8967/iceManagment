package com.icerojects.icemanagment.domain.repository

import com.icerojects.icemanagment.domain.model.StockItem
import com.icerojects.icemanagment.utils.Resource
import kotlinx.coroutines.flow.Flow

/**
 * Interfaz del repositorio para operaciones relacionadas con productos del inventario
 */
interface StockRepository {
    /**
     * Obtiene todos los productos del inventario
     */
    fun getStockItems(): Flow<Resource<List<StockItem>>>
    
    /**
     * Obtiene un producto específico por su ID
     */
    suspend fun getStockItemById(id: String): Resource<StockItem>
    
    /**
     * Agrega un nuevo producto al inventario
     */
    suspend fun addStockItem(stockItem: StockItem): Resource<String>
    
    /**
     * Actualiza un producto existente
     */
    suspend fun updateStockItem(stockItem: StockItem): Resource<Unit>
    
    /**
     * Elimina un producto del inventario
     */
    suspend fun deleteStockItem(id: String): Resource<Unit>
    
    /**
     * Busca productos por nombre
     */
    fun searchStockItems(query: String): Flow<Resource<List<StockItem>>>
    
    /**
     * Filtra productos por categoría
     */
    fun filterStockItemsByCategory(categoryId: String): Flow<Resource<List<StockItem>>>
}