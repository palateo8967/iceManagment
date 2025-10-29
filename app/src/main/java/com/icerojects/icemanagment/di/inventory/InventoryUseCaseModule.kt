package com.icerojects.icemanagment.di

import com.icerojects.icemanagment.domain.repository.CategoryRepository
import com.icerojects.icemanagment.domain.repository.ProductRepository
import com.icerojects.icemanagment.domain.use_case.category.*
import com.icerojects.icemanagment.domain.use_case.product.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Module for providing inventory use cases
 */
@Module
@InstallIn(SingletonComponent::class)
object InventoryUseCaseModule {
    
    // Category use cases
    @Provides
    @Singleton
    fun provideGetCategoriesUseCase(repository: CategoryRepository): GetCategoriesUseCase {
        return GetCategoriesUseCase(repository)
    }
    
    @Provides
    @Singleton
    fun provideGetCategoryByIdUseCase(repository: CategoryRepository): GetCategoryByIdUseCase {
        return GetCategoryByIdUseCase(repository)
    }
    
    @Provides
    @Singleton
    fun provideAddCategoryUseCase(repository: CategoryRepository): AddCategoryUseCase {
        return AddCategoryUseCase(repository)
    }
    
    @Provides
    @Singleton
    fun provideUpdateCategoryUseCase(repository: CategoryRepository): UpdateCategoryUseCase {
        return UpdateCategoryUseCase(repository)
    }
    
    @Provides
    @Singleton
    fun provideDeleteCategoryUseCase(repository: CategoryRepository): DeleteCategoryUseCase {
        return DeleteCategoryUseCase(repository)
    }
    
    // Product use cases
    @Provides
    @Singleton
    fun provideGetProductsUseCase(repository: ProductRepository): GetProductsUseCase {
        return GetProductsUseCase(repository)
    }
    
    @Provides
    @Singleton
    fun provideGetProductByIdUseCase(repository: ProductRepository): GetProductByIdUseCase {
        return GetProductByIdUseCase(repository)
    }
    
    @Provides
    @Singleton
    fun provideAddProductUseCase(repository: ProductRepository): AddProductUseCase {
        return AddProductUseCase(repository)
    }
    
    @Provides
    @Singleton
    fun provideUpdateProductUseCase(repository: ProductRepository): UpdateProductUseCase {
        return UpdateProductUseCase(repository)
    }
    
    @Provides
    @Singleton
    fun provideDeleteProductUseCase(repository: ProductRepository): DeleteProductUseCase {
        return DeleteProductUseCase(repository)
    }
    
    @Provides
    @Singleton
    fun provideSearchProductsUseCase(repository: ProductRepository): SearchProductsUseCase {
        return SearchProductsUseCase(repository)
    }
    
    @Provides
    @Singleton
    fun provideFilterProductsByCategoryUseCase(repository: ProductRepository): FilterProductsByCategoryUseCase {
        return FilterProductsByCategoryUseCase(repository)
    }
}