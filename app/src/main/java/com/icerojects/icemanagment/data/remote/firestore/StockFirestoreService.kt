package com.icerojects.icemanagment.data.remote.firestore

import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.icerojects.icemanagment.domain.model.StockItem
import com.icerojects.icemanagment.domain.model.UnidadMedida
import com.icerojects.icemanagment.utils.Resource
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import java.util.Date
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StockFirestoreService @Inject constructor() {
    private val db: FirebaseFirestore = Firebase.firestore
    private val stockCollection = db.collection("productos")
    private val categoriasCollection = db.collection("categorias")

    fun getStockItems(): Flow<Resource<List<StockItem>>> = callbackFlow {
        trySend(Resource.Loading())

        val subscription = stockCollection
            .orderBy("fechaCreacion", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    trySend(Resource.Error(error.message ?: "Error desconocido"))
                    return@addSnapshotListener
                }

                val stockItems = snapshot?.documents?.mapNotNull { document ->
                    try {
                        val id = document.id
                        val nombre = document.getString("nombre") ?: ""
                        val categoriaId = document.getString("categoriaId") ?: ""
                        val categoriaNombre = document.getString("categoriaNombre") ?: ""
                        val cantidad = document.getDouble("cantidad") ?: 0.0
                        val unidadString = document.getString("unidad") ?: UnidadMedida.UNIDAD.name
                        val unidad = try {
                            UnidadMedida.valueOf(unidadString)
                        } catch (e: Exception) {
                            UnidadMedida.UNIDAD
                        }
                        val stockMinimo = document.getDouble("stockMinimo") ?: 0.0
                        val precio = document.getDouble("precio") ?: 0.0
                        val fechaCreacion = (document.getTimestamp("fechaCreacion")?.toDate() ?: Date())

                        StockItem(
                            id = id,
                            nombre = nombre,
                            categoriaId = categoriaId,
                            categoriaNombre = categoriaNombre,
                            cantidad = cantidad,
                            unidad = unidad,
                            stockMinimo = stockMinimo,
                            precio = precio,
                            fechaCreacion = fechaCreacion
                        )
                    } catch (e: Exception) {
                        null
                    }
                } ?: emptyList()

                trySend(Resource.Success(stockItems))
            }

        awaitClose { subscription.remove() }
    }

    suspend fun getStockItemById(id: String): Resource<StockItem> {
        return try {
            val document = stockCollection.document(id).get().await()
            if (document.exists()) {
                val nombre = document.getString("nombre") ?: ""
                val categoriaId = document.getString("categoriaId") ?: ""
                val categoriaNombre = document.getString("categoriaNombre") ?: ""
                val cantidad = document.getDouble("cantidad") ?: 0.0
                val unidadString = document.getString("unidad") ?: UnidadMedida.UNIDAD.name
                val unidad = try {
                    UnidadMedida.valueOf(unidadString)
                } catch (e: Exception) {
                    UnidadMedida.UNIDAD
                }
                val stockMinimo = document.getDouble("stockMinimo") ?: 0.0
                val precio = document.getDouble("precio") ?: 0.0
                val fechaCreacion = (document.getTimestamp("fechaCreacion")?.toDate() ?: Date())

                val stockItem = StockItem(
                    id = document.id,
                    nombre = nombre,
                    categoriaId = categoriaId,
                    categoriaNombre = categoriaNombre,
                    cantidad = cantidad,
                    unidad = unidad,
                    stockMinimo = stockMinimo,
                    precio = precio,
                    fechaCreacion = fechaCreacion
                )
                Resource.Success(stockItem)
            } else {
                Resource.Error("Producto no encontrado")
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Error al obtener el producto")
        }
    }

    suspend fun addStockItem(stockItem: StockItem): Resource<String> {
        return try {
            // Primero obtenemos el nombre de la categoría
            val categoriaDoc = categoriasCollection.document(stockItem.categoriaId).get().await()
            val categoriaNombre = categoriaDoc.getString("nombre") ?: "Sin categoría"

            val stockItemMap = hashMapOf(
                "nombre" to stockItem.nombre,
                "categoriaId" to stockItem.categoriaId,
                "categoriaNombre" to categoriaNombre,
                "cantidad" to stockItem.cantidad,
                "unidad" to stockItem.unidad.name,
                "stockMinimo" to stockItem.stockMinimo,
                "precio" to stockItem.precio,
                "fechaCreacion" to Timestamp(stockItem.fechaCreacion)
            )

            val documentRef = stockCollection.add(stockItemMap).await()
            Resource.Success(documentRef.id)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Error al agregar el producto")
        }
    }

    suspend fun updateStockItem(stockItem: StockItem): Resource<Unit> {
        return try {
            // Primero obtenemos el nombre de la categoría
            val categoriaDoc = categoriasCollection.document(stockItem.categoriaId).get().await()
            val categoriaNombre = categoriaDoc.getString("nombre") ?: "Sin categoría"

            val stockItemMap = hashMapOf(
                "nombre" to stockItem.nombre,
                "categoriaId" to stockItem.categoriaId,
                "categoriaNombre" to categoriaNombre,
                "cantidad" to stockItem.cantidad,
                "unidad" to stockItem.unidad.name,
                "stockMinimo" to stockItem.stockMinimo,
                "precio" to stockItem.precio,
                "fechaCreacion" to Timestamp(stockItem.fechaCreacion)
            )

            stockCollection.document(stockItem.id).update(stockItemMap as Map<String, Any>).await()
            Resource.Success(Unit)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Error al actualizar el producto")
        }
    }

    suspend fun deleteStockItem(id: String): Resource<Unit> {
        return try {
            stockCollection.document(id).delete().await()
            Resource.Success(Unit)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Error al eliminar el producto")
        }
    }

    fun searchStockItems(query: String): Flow<Resource<List<StockItem>>> = callbackFlow {
        trySend(Resource.Loading())

        val subscription = stockCollection
            .orderBy("nombre")
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    trySend(Resource.Error(error.message ?: "Error desconocido"))
                    return@addSnapshotListener
                }

                val stockItems = snapshot?.documents?.mapNotNull { document ->
                    try {
                        val nombre = document.getString("nombre") ?: ""
                        
                        // Filtrar por la consulta
                        if (!nombre.contains(query, ignoreCase = true)) {
                            return@mapNotNull null
                        }
                        
                        val id = document.id
                        val categoriaId = document.getString("categoriaId") ?: ""
                        val categoriaNombre = document.getString("categoriaNombre") ?: ""
                        val cantidad = document.getDouble("cantidad") ?: 0.0
                        val unidadString = document.getString("unidad") ?: UnidadMedida.UNIDAD.name
                        val unidad = try {
                            UnidadMedida.valueOf(unidadString)
                        } catch (e: Exception) {
                            UnidadMedida.UNIDAD
                        }
                        val stockMinimo = document.getDouble("stockMinimo") ?: 0.0
                        val precio = document.getDouble("precio") ?: 0.0
                        val fechaCreacion = (document.getTimestamp("fechaCreacion")?.toDate() ?: Date())

                        StockItem(
                            id = id,
                            nombre = nombre,
                            categoriaId = categoriaId,
                            categoriaNombre = categoriaNombre,
                            cantidad = cantidad,
                            unidad = unidad,
                            stockMinimo = stockMinimo,
                            precio = precio,
                            fechaCreacion = fechaCreacion
                        )
                    } catch (e: Exception) {
                        null
                    }
                } ?: emptyList()

                trySend(Resource.Success(stockItems))
            }

        awaitClose { subscription.remove() }
    }

    fun filterStockItemsByCategory(categoryId: String): Flow<Resource<List<StockItem>>> = callbackFlow {
        trySend(Resource.Loading())

        val subscription = stockCollection
            .whereEqualTo("categoriaId", categoryId)
            .orderBy("nombre")
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    trySend(Resource.Error(error.message ?: "Error desconocido"))
                    return@addSnapshotListener
                }

                val stockItems = snapshot?.documents?.mapNotNull { document ->
                    try {
                        val id = document.id
                        val nombre = document.getString("nombre") ?: ""
                        val categoriaId = document.getString("categoriaId") ?: ""
                        val categoriaNombre = document.getString("categoriaNombre") ?: ""
                        val cantidad = document.getDouble("cantidad") ?: 0.0
                        val unidadString = document.getString("unidad") ?: UnidadMedida.UNIDAD.name
                        val unidad = try {
                            UnidadMedida.valueOf(unidadString)
                        } catch (e: Exception) {
                            UnidadMedida.UNIDAD
                        }
                        val stockMinimo = document.getDouble("stockMinimo") ?: 0.0
                        val precio = document.getDouble("precio") ?: 0.0
                        val fechaCreacion = (document.getTimestamp("fechaCreacion")?.toDate() ?: Date())

                        StockItem(
                            id = id,
                            nombre = nombre,
                            categoriaId = categoriaId,
                            categoriaNombre = categoriaNombre,
                            cantidad = cantidad,
                            unidad = unidad,
                            stockMinimo = stockMinimo,
                            precio = precio,
                            fechaCreacion = fechaCreacion
                        )
                    } catch (e: Exception) {
                        null
                    }
                } ?: emptyList()

                trySend(Resource.Success(stockItems))
            }

        awaitClose { subscription.remove() }
    }
}