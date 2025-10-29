package com.icerojects.icemanagment.domain.use_case.category

import com.icerojects.icemanagment.domain.model.Category
import com.icerojects.icemanagment.domain.repository.CategoryRepository
import com.icerojects.icemanagment.utils.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Use case to get all categories
 */
class GetCategoriesUseCase @Inject constructor(
    private val repository: CategoryRepository
) {
    operator fun invoke(): Flow<Resource<List<Category>>> {
        return repository.getCategories()
    }
}