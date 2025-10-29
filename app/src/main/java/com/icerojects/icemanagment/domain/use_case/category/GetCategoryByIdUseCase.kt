package com.icerojects.icemanagment.domain.use_case.category

import com.icerojects.icemanagment.domain.model.Category
import com.icerojects.icemanagment.domain.repository.CategoryRepository
import com.icerojects.icemanagment.utils.Resource
import javax.inject.Inject

/**
 * Use case to get a category by its ID
 */
class GetCategoryByIdUseCase @Inject constructor(
    private val repository: CategoryRepository
) {
    suspend operator fun invoke(id: String): Resource<Category> {
        return repository.getCategoryById(id)
    }
}