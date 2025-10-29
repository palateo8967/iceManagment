package com.icerojects.icemanagment.domain.use_case.stock

import com.icerojects.icemanagment.domain.model.StockItem
import com.icerojects.icemanagment.domain.repository.StockRepository
import com.icerojects.icemanagment.utils.Resource
import javax.inject.Inject

/**
 * Caso de uso para añadir un nuevo producto al inventario
 */
class AddStockItemUseCase @Inject constructor(
    private val repository: StockRepository
) {
    suspend operator fun invoke(stockItem: StockItem): Resource<String> {
        if (stockItem.nombre.isBlank()) {
            return Resource.Error("El nombre del producto no puede estar vacío")
        }
        
        if (stockItem.cantidad < 0) {
            return Resource.Error("La cantidad no puede ser negativa")
        }
        
        if (stockItem.stockMinimo < 0) {
            return Resource.Error("El stock mínimo no puede ser negativo")
        }
        
        if (stockItem.precio < 0) {
            return Resource.Error("El precio no puede ser negativo")
        }
        
        if (stockItem.categoriaId.isBlank()) {
            return Resource.Error("Debe seleccionar una categoría")
        }
        
        return repository.addStockItem(stockItem)
    }
}