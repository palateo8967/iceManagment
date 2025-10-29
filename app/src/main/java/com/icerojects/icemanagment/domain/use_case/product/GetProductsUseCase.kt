package com.icerojects.icemanagment.domain.use_case.product

import com.icerojects.icemanagment.domain.model.Product
import com.icerojects.icemanagment.domain.repository.ProductRepository
import com.icerojects.icemanagment.utils.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Use case to get all products
 */
class GetProductsUseCase @Inject constructor(
    private val repository: ProductRepository
) {
    operator fun invoke(): Flow<Resource<List<Product>>> {
        return repository.getProducts()
    }
}