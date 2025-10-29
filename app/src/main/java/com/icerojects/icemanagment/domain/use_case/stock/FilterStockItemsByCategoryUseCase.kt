package com.icerojects.icemanagment.domain.use_case.stock

import com.icerojects.icemanagment.domain.model.StockItem
import com.icerojects.icemanagment.domain.repository.StockRepository
import com.icerojects.icemanagment.utils.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Caso de uso para filtrar productos por categoría
 */
class FilterStockItemsByCategoryUseCase @Inject constructor(
    private val repository: StockRepository
) {
    operator fun invoke(categoryId: String): Flow<Resource<List<StockItem>>> {
        return repository.filterStockItemsByCategory(categoryId)
    }
}