package com.icerojects.icemanagment.domain.use_case.stock

import com.icerojects.icemanagment.domain.model.StockItem
import com.icerojects.icemanagment.domain.repository.StockRepository
import com.icerojects.icemanagment.utils.Resource
import javax.inject.Inject

/**
 * Caso de uso para obtener un producto específico por su ID
 */
class GetStockItemByIdUseCase @Inject constructor(
    private val repository: StockRepository
) {
    suspend operator fun invoke(id: String): Resource<StockItem> {
        if (id.isBlank()) {
            return Resource.Error("ID de producto inválido")
        }
        
        return repository.getStockItemById(id)
    }
}