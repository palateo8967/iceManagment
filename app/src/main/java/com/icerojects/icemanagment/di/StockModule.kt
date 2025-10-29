package com.icerojects.icemanagment.di

import com.icerojects.icemanagment.data.remote.firestore.CategoriaFirestoreService
import com.icerojects.icemanagment.data.remote.firestore.StockFirestoreService
import com.icerojects.icemanagment.data.repository.CategoriaRepositoryImpl
import com.icerojects.icemanagment.data.repository.StockRepositoryImpl
import com.icerojects.icemanagment.domain.repository.CategoriaRepository
import com.icerojects.icemanagment.domain.repository.StockRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object StockModule {
    
    @Provides
    @Singleton
    fun provideStockFirestoreService(): StockFirestoreService {
        return StockFirestoreService()
    }
    
    @Provides
    @Singleton
    fun provideCategoriaFirestoreService(): CategoriaFirestoreService {
        return CategoriaFirestoreService()
    }
    
    @Provides
    @Singleton
    fun provideStockRepository(stockFirestoreService: StockFirestoreService): StockRepository {
        return StockRepositoryImpl(stockFirestoreService)
    }
    
    @Provides
    @Singleton
    fun provideCategoriaRepository(categoriaFirestoreService: CategoriaFirestoreService): CategoriaRepository {
        return CategoriaRepositoryImpl(categoriaFirestoreService)
    }
}