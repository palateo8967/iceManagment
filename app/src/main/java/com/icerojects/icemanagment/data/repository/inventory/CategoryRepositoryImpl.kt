package com.icerojects.icemanagment.data.repository

import com.icerojects.icemanagment.data.remote.firestore.CategoryFirestoreService
import com.icerojects.icemanagment.domain.model.Category
import com.icerojects.icemanagment.domain.repository.CategoryRepository
import com.icerojects.icemanagment.utils.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CategoryRepositoryImpl @Inject constructor(
    private val categoryFirestoreService: CategoryFirestoreService
) : CategoryRepository {
    
    override fun getCategories(): Flow<Resource<List<Category>>> {
        return categoryFirestoreService.getCategories()
    }
    
    override suspend fun getCategoryById(id: String): Resource<Category> {
        return categoryFirestoreService.getCategoryById(id)
    }
    
    override suspend fun addCategory(category: Category): Resource<String> {
        return categoryFirestoreService.addCategory(category)
    }
    
    override suspend fun updateCategory(category: Category): Resource<Unit> {
        return categoryFirestoreService.updateCategory(category)
    }
    
    override suspend fun deleteCategory(id: String): Resource<Unit> {
        return categoryFirestoreService.deleteCategory(id)
    }
}