package com.icerojects.icemanagment.domain.use_case.product

import com.icerojects.icemanagment.domain.repository.ProductRepository
import com.icerojects.icemanagment.utils.Resource
import javax.inject.Inject

/**
 * Use case to delete a product
 */
class DeleteProductUseCase @Inject constructor(
    private val repository: ProductRepository
) {
    suspend operator fun invoke(id: String): Resource<Unit> {
        if (id.isBlank()) {
            return Resource.Error("Product ID cannot be empty")
        }
        
        return repository.deleteProduct(id)
    }
}