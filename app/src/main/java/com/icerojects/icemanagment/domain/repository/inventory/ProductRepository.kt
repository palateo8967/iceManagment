package com.icerojects.icemanagment.domain.repository

import com.icerojects.icemanagment.domain.model.Product
import com.icerojects.icemanagment.utils.Resource
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for product operations
 */
interface ProductRepository {
    
    fun getProducts(): Flow<Resource<List<Product>>>
    
    suspend fun getProductById(id: String): Resource<Product>
    
    suspend fun addProduct(product: Product): Resource<String>
    
    suspend fun updateProduct(product: Product): Resource<Unit>
    
    suspend fun deleteProduct(id: String): Resource<Unit>
    
    fun searchProducts(query: String): Flow<Resource<List<Product>>>
    
    fun filterProductsByCategory(categoryId: String): Flow<Resource<List<Product>>>
}