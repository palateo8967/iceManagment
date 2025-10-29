package com.icerojects.icemanagment.domain.use_case.categoria

import com.icerojects.icemanagment.domain.model.Categoria
import com.icerojects.icemanagment.domain.repository.CategoriaRepository
import com.icerojects.icemanagment.utils.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Caso de uso para obtener todas las categorías
 */
class GetCategoriasUseCase @Inject constructor(
    private val repository: CategoriaRepository
) {
    operator fun invoke(): Flow<Resource<List<Categoria>>> {
        return repository.getCategorias()
    }
}