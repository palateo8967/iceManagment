package com.icerojects.icemanagment.domain.use_case.product

import com.icerojects.icemanagment.domain.model.Product
import com.icerojects.icemanagment.domain.repository.ProductRepository
import com.icerojects.icemanagment.utils.Resource
import javax.inject.Inject

/**
 * Use case to add a new product
 */
class AddProductUseCase @Inject constructor(
    private val repository: ProductRepository
) {
    suspend operator fun invoke(product: Product): Resource<String> {
        if (product.name.isBlank()) {
            return Resource.Error("Product name cannot be empty")
        }
        
        if (product.categoryId.isBlank()) {
            return Resource.Error("Category must be selected")
        }
        
        if (product.price < 0) {
            return Resource.Error("Price cannot be negative")
        }
        
        return repository.addProduct(product)
    }
}