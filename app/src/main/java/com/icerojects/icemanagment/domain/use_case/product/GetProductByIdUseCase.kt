package com.icerojects.icemanagment.domain.use_case.product

import com.icerojects.icemanagment.domain.model.Product
import com.icerojects.icemanagment.domain.repository.ProductRepository
import com.icerojects.icemanagment.utils.Resource
import javax.inject.Inject

/**
 * Use case to get a product by its ID
 */
class GetProductByIdUseCase @Inject constructor(
    private val repository: ProductRepository
) {
    suspend operator fun invoke(id: String): Resource<Product> {
        return repository.getProductById(id)
    }
}