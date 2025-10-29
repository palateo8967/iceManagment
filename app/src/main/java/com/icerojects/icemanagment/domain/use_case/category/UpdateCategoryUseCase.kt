package com.icerojects.icemanagment.domain.use_case.category

import com.icerojects.icemanagment.domain.model.Category
import com.icerojects.icemanagment.domain.repository.CategoryRepository
import com.icerojects.icemanagment.utils.Resource
import javax.inject.Inject

/**
 * Use case to update an existing category
 */
class UpdateCategoryUseCase @Inject constructor(
    private val repository: CategoryRepository
) {
    suspend operator fun invoke(category: Category): Resource<Unit> {
        if (category.name.isBlank()) {
            return Resource.Error("Category name cannot be empty")
        }
        
        return repository.updateCategory(category)
    }
}