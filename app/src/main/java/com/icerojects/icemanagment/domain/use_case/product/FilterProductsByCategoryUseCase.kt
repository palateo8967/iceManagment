package com.icerojects.icemanagment.domain.use_case.product

import com.icerojects.icemanagment.domain.model.Product
import com.icerojects.icemanagment.domain.repository.ProductRepository
import com.icerojects.icemanagment.utils.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Use case to filter products by category
 */
class FilterProductsByCategoryUseCase @Inject constructor(
    private val repository: ProductRepository
) {
    operator fun invoke(categoryId: String): Flow<Resource<List<Product>>> {
        return repository.filterProductsByCategory(categoryId)
    }
}