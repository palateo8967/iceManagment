package com.icerojects.icemanagment.data.remote.firestore

import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.icerojects.icemanagment.domain.model.Categoria
import com.icerojects.icemanagment.utils.Resource
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import java.util.Date
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CategoriaFirestoreService @Inject constructor() {
    private val db: FirebaseFirestore = Firebase.firestore
    private val categoriasCollection = db.collection("categorias")

    fun getCategorias(): Flow<Resource<List<Categoria>>> = callbackFlow {
        trySend(Resource.Loading())

        val subscription = categoriasCollection
            .orderBy("nombre")
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    trySend(Resource.Error(error.message ?: "Error desconocido"))
                    return@addSnapshotListener
                }

                val categorias = snapshot?.documents?.mapNotNull { document ->
                    try {
                        val id = document.id
                        val nombre = document.getString("nombre") ?: ""
                        val fechaCreacion = (document.getTimestamp("fechaCreacion")?.toDate() ?: Date())

                        Categoria(
                            id = id,
                            nombre = nombre,
                            fechaCreacion = fechaCreacion
                        )
                    } catch (e: Exception) {
                        null
                    }
                } ?: emptyList()

                trySend(Resource.Success(categorias))
            }

        awaitClose { subscription.remove() }
    }

    suspend fun getCategoriaById(id: String): Resource<Categoria> {
        return try {
            val document = categoriasCollection.document(id).get().await()
            if (document.exists()) {
                val nombre = document.getString("nombre") ?: ""
                val fechaCreacion = (document.getTimestamp("fechaCreacion")?.toDate() ?: Date())

                val categoria = Categoria(
                    id = document.id,
                    nombre = nombre,
                    fechaCreacion = fechaCreacion
                )
                Resource.Success(categoria)
            } else {
                Resource.Error("Categoría no encontrada")
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Error al obtener la categoría")
        }
    }

    suspend fun addCategoria(categoria: Categoria): Resource<String> {
        return try {
            val categoriaMap = hashMapOf(
                "nombre" to categoria.nombre,
                "fechaCreacion" to Timestamp(categoria.fechaCreacion)
            )

            val documentRef = categoriasCollection.add(categoriaMap).await()
            Resource.Success(documentRef.id)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Error al agregar la categoría")
        }
    }

    suspend fun updateCategoria(categoria: Categoria): Resource<Unit> {
        return try {
            val categoriaMap = hashMapOf(
                "nombre" to categoria.nombre,
                "fechaCreacion" to Timestamp(categoria.fechaCreacion)
            )

            categoriasCollection.document(categoria.id).update(categoriaMap as Map<String, Any>).await()
            Resource.Success(Unit)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Error al actualizar la categoría")
        }
    }

    suspend fun deleteCategoria(id: String): Resource<Unit> {
        return try {
            // Primero verificamos si hay productos que usan esta categoría
            val productosConCategoria = db.collection("productos")
                .whereEqualTo("categoriaId", id)
                .limit(1)
                .get()
                .await()

            if (!productosConCategoria.isEmpty) {
                return Resource.Error("No se puede eliminar la categoría porque hay productos que la utilizan")
            }

            categoriasCollection.document(id).delete().await()
            Resource.Success(Unit)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Error al eliminar la categoría")
        }
    }
}