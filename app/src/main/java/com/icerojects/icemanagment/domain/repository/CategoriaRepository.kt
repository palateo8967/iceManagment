package com.icerojects.icemanagment.domain.repository

import com.icerojects.icemanagment.domain.model.Categoria
import com.icerojects.icemanagment.utils.Resource
import kotlinx.coroutines.flow.Flow

/**
 * Interfaz del repositorio para operaciones relacionadas con categorías de productos
 */
interface CategoriaRepository {
    /**
     * Obtiene todas las categorías
     */
    fun getCategorias(): Flow<Resource<List<Categoria>>>
    
    /**
     * Obtiene una categoría específica por su ID
     */
    suspend fun getCategoriaById(id: String): Resource<Categoria>
    
    /**
     * Agrega una nueva categoría
     */
    suspend fun addCategoria(categoria: Categoria): Resource<String>
    
    /**
     * Actualiza una categoría existente
     */
    suspend fun updateCategoria(categoria: Categoria): Resource<Unit>
    
    /**
     * Elimina una categoría
     */
    suspend fun deleteCategoria(id: String): Resource<Unit>
}