package com.icerojects.icemanagment.data.repository

import com.icerojects.icemanagment.data.remote.firestore.ProductFirestoreService
import com.icerojects.icemanagment.domain.model.Product
import com.icerojects.icemanagment.domain.repository.ProductRepository
import com.icerojects.icemanagment.utils.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProductRepositoryImpl @Inject constructor(
    private val productFirestoreService: ProductFirestoreService
) : ProductRepository {
    
    override fun getProducts(): Flow<Resource<List<Product>>> {
        return productFirestoreService.getProducts()
    }
    
    override suspend fun getProductById(id: String): Resource<Product> {
        return productFirestoreService.getProductById(id)
    }
    
    override suspend fun addProduct(product: Product): Resource<String> {
        return productFirestoreService.addProduct(product)
    }
    
    override suspend fun updateProduct(product: Product): Resource<Unit> {
        return productFirestoreService.updateProduct(product)
    }
    
    override suspend fun deleteProduct(id: String): Resource<Unit> {
        return productFirestoreService.deleteProduct(id)
    }
    
    override fun searchProducts(query: String): Flow<Resource<List<Product>>> {
        return productFirestoreService.searchProducts(query)
    }
    
    override fun filterProductsByCategory(categoryId: String): Flow<Resource<List<Product>>> {
        return productFirestoreService.filterProductsByCategory(categoryId)
    }
}