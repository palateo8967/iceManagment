package com.icerojects.icemanagment.domain.repository

import com.icerojects.icemanagment.domain.model.Category
import com.icerojects.icemanagment.utils.Resource
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for category operations
 */
interface CategoryRepository {
    
    fun getCategories(): Flow<Resource<List<Category>>>
    
    suspend fun getCategoryById(id: String): Resource<Category>
    
    suspend fun addCategory(category: Category): Resource<String>
    
    suspend fun updateCategory(category: Category): Resource<Unit>
    
    suspend fun deleteCategory(id: String): Resource<Unit>
}