package com.icerojects.icemanagment.domain.use_case.category

import com.icerojects.icemanagment.domain.model.Category
import com.icerojects.icemanagment.domain.repository.CategoryRepository
import com.icerojects.icemanagment.utils.Resource
import javax.inject.Inject

/**
 * Use case to add a new category
 */
class AddCategoryUseCase @Inject constructor(
    private val repository: CategoryRepository
) {
    suspend operator fun invoke(category: Category): Resource<String> {
        if (category.name.isBlank()) {
            return Resource.Error("Category name cannot be empty")
        }
        
        return repository.addCategory(category)
    }
}