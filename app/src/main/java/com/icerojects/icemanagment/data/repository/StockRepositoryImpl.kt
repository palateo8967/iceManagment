package com.icerojects.icemanagment.data.repository

import com.icerojects.icemanagment.data.remote.firestore.StockFirestoreService
import com.icerojects.icemanagment.domain.model.StockItem
import com.icerojects.icemanagment.domain.repository.StockRepository
import com.icerojects.icemanagment.utils.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StockRepositoryImpl @Inject constructor(
    private val stockFirestoreService: StockFirestoreService
) : StockRepository {
    
    override fun getStockItems(): Flow<Resource<List<StockItem>>> {
        return stockFirestoreService.getStockItems()
    }
    
    override suspend fun getStockItemById(id: String): Resource<StockItem> {
        return stockFirestoreService.getStockItemById(id)
    }
    
    override suspend fun addStockItem(stockItem: StockItem): Resource<String> {
        return stockFirestoreService.addStockItem(stockItem)
    }
    
    override suspend fun updateStockItem(stockItem: StockItem): Resource<Unit> {
        return stockFirestoreService.updateStockItem(stockItem)
    }
    
    override suspend fun deleteStockItem(id: String): Resource<Unit> {
        return stockFirestoreService.deleteStockItem(id)
    }
    
    override fun searchStockItems(query: String): Flow<Resource<List<StockItem>>> {
        return stockFirestoreService.searchStockItems(query)
    }
    
    override fun filterStockItemsByCategory(categoryId: String): Flow<Resource<List<StockItem>>> {
        return stockFirestoreService.filterStockItemsByCategory(categoryId)
    }
}