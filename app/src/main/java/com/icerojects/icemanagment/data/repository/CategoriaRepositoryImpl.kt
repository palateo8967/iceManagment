package com.icerojects.icemanagment.data.repository

import com.icerojects.icemanagment.data.remote.firestore.CategoriaFirestoreService
import com.icerojects.icemanagment.domain.model.Categoria
import com.icerojects.icemanagment.domain.repository.CategoriaRepository
import com.icerojects.icemanagment.utils.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CategoriaRepositoryImpl @Inject constructor(
    private val categoriaFirestoreService: CategoriaFirestoreService
) : CategoriaRepository {
    
    override fun getCategorias(): Flow<Resource<List<Categoria>>> {
        return categoriaFirestoreService.getCategorias()
    }
    
    override suspend fun getCategoriaById(id: String): Resource<Categoria> {
        return categoriaFirestoreService.getCategoriaById(id)
    }
    
    override suspend fun addCategoria(categoria: Categoria): Resource<String> {
        return categoriaFirestoreService.addCategoria(categoria)
    }
    
    override suspend fun updateCategoria(categoria: Categoria): Resource<Unit> {
        return categoriaFirestoreService.updateCategoria(categoria)
    }
    
    override suspend fun deleteCategoria(id: String): Resource<Unit> {
        return categoriaFirestoreService.deleteCategoria(id)
    }
}