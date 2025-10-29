package com.icerojects.icemanagment.di

import com.icerojects.icemanagment.data.remote.firestore.CategoryFirestoreService
import com.icerojects.icemanagment.data.remote.firestore.ProductFirestoreService
import com.icerojects.icemanagment.data.repository.CategoryRepositoryImpl
import com.icerojects.icemanagment.data.repository.ProductRepositoryImpl
import com.icerojects.icemanagment.domain.repository.CategoryRepository
import com.icerojects.icemanagment.domain.repository.ProductRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object InventoryModule {
    
    @Provides
    @Singleton
    fun provideProductFirestoreService(): ProductFirestoreService {
        return ProductFirestoreService()
    }
    
    @Provides
    @Singleton
    fun provideCategoryFirestoreService(): CategoryFirestoreService {
        return CategoryFirestoreService()
    }
    
    @Provides
    @Singleton
    fun provideProductRepository(productFirestoreService: ProductFirestoreService): ProductRepository {
        return ProductRepositoryImpl(productFirestoreService)
    }
    
    @Provides
    @Singleton
    fun provideCategoryRepository(categoryFirestoreService: CategoryFirestoreService): CategoryRepository {
        return CategoryRepositoryImpl(categoryFirestoreService)
    }
}