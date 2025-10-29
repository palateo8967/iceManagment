package com.icerojects.icemanagment.domain.use_case.stock

import com.icerojects.icemanagment.domain.repository.StockRepository
import com.icerojects.icemanagment.utils.Resource
import javax.inject.Inject

/**
 * Caso de uso para eliminar un producto del inventario
 */
class DeleteStockItemUseCase @Inject constructor(
    private val repository: StockRepository
) {
    suspend operator fun invoke(id: String): Resource<Unit> {
        if (id.isBlank()) {
            return Resource.Error("ID de producto inválido")
        }
        
        return repository.deleteStockItem(id)
    }
}