package com.icerojects.icemanagment.domain.use_case.categoria

import com.icerojects.icemanagment.domain.model.Categoria
import com.icerojects.icemanagment.domain.repository.CategoriaRepository
import com.icerojects.icemanagment.utils.Resource
import javax.inject.Inject

/**
 * Caso de uso para añadir una nueva categoría
 */
class AddCategoriaUseCase @Inject constructor(
    private val repository: CategoriaRepository
) {
    suspend operator fun invoke(categoria: Categoria): Resource<String> {
        if (categoria.nombre.isBlank()) {
            return Resource.Error("El nombre de la categoría no puede estar vacío")
        }
        
        return repository.addCategoria(categoria)
    }
}