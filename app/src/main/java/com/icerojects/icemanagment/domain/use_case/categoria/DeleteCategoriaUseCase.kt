package com.icerojects.icemanagment.domain.use_case.categoria

import com.icerojects.icemanagment.domain.repository.CategoriaRepository
import com.icerojects.icemanagment.utils.Resource
import javax.inject.Inject

/**
 * Caso de uso para eliminar una categoría
 */
class DeleteCategoriaUseCase @Inject constructor(
    private val repository: CategoriaRepository
) {
    suspend operator fun invoke(id: String): Resource<Unit> {
        if (id.isBlank()) {
            return Resource.Error("ID de categoría inválido")
        }
        
        return repository.deleteCategoria(id)
    }
}