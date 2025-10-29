package com.icerojects.icemanagment.domain.use_case.category

import com.icerojects.icemanagment.domain.repository.CategoryRepository
import com.icerojects.icemanagment.utils.Resource
import javax.inject.Inject

/**
 * Use case to delete a category
 */
class DeleteCategoryUseCase @Inject constructor(
    private val repository: CategoryRepository
) {
    suspend operator fun invoke(id: String): Resource<Unit> {
        if (id.isBlank()) {
            return Resource.Error("Category ID cannot be empty")
        }
        
        return repository.deleteCategory(id)
    }
}