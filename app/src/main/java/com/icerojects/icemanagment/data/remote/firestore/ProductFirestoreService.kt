package com.icerojects.icemanagment.data.remote.firestore

import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.icerojects.icemanagment.domain.model.Product
import com.icerojects.icemanagment.domain.model.UnitOfMeasure
import com.icerojects.icemanagment.utils.Resource
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import java.util.Date
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProductFirestoreService @Inject constructor() {
    private val db: FirebaseFirestore = Firebase.firestore
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    
    // Get user-specific collections
    private fun getUserProductsCollection(): CollectionReference? {
        val userId = auth.currentUser?.uid ?: return null
        return db.collection("users").document(userId).collection("products")
    }
    
    private fun getUserCategoriesCollection(): CollectionReference? {
        val userId = auth.currentUser?.uid ?: return null
        return db.collection("users").document(userId).collection("categories")
    }

    fun getProducts(): Flow<Resource<List<Product>>> = callbackFlow {
        trySend(Resource.Loading())
        
        val userId = auth.currentUser?.uid
        if (userId == null) {
            trySend(Resource.Error("User not authenticated"))
            close()
            return@callbackFlow
        }
        
        val productsCollection = getUserProductsCollection()
        if (productsCollection == null) {
            trySend(Resource.Error("User not authenticated"))
            close()
            return@callbackFlow
        }

        val subscription = productsCollection
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    trySend(Resource.Error(error.message ?: "Unknown error"))
                    return@addSnapshotListener
                }

                val products = snapshot?.documents?.mapNotNull { document ->
                    try {
                        val id = document.id
                        val name = document.getString("name") ?: ""
                        val categoryId = document.getString("categoryId") ?: ""
                        val categoryName = document.getString("categoryName") ?: ""
                        val quantity = document.getDouble("quantity") ?: 0.0
                        val unitString = document.getString("unit") ?: UnitOfMeasure.UNIT.name
                        val unit = try {
                            UnitOfMeasure.valueOf(unitString)
                        } catch (e: Exception) {
                            UnitOfMeasure.UNIT
                        }
                        val minStock = document.getDouble("minStock") ?: 0.0
                        val price = document.getDouble("price") ?: 0.0
                        val createdAt = (document.getTimestamp("createdAt")?.toDate() ?: Date())

                        Product(
                            id = id,
                            name = name,
                            categoryId = categoryId,
                            categoryName = categoryName,
                            quantity = quantity,
                            unit = unit,
                            minStock = minStock,
                            price = price,
                            createdAt = createdAt
                        )
                    } catch (e: Exception) {
                        null
                    }
                } ?: emptyList()

                trySend(Resource.Success(products))
            }

        awaitClose { subscription.remove() }
    }

    suspend fun getProductById(id: String): Resource<Product> {
        val productsCollection = getUserProductsCollection() ?: return Resource.Error("User not authenticated")
        
        return try {
            val document = productsCollection.document(id).get().await()
            if (document.exists()) {
                val name = document.getString("name") ?: ""
                val categoryId = document.getString("categoryId") ?: ""
                val categoryName = document.getString("categoryName") ?: ""
                val quantity = document.getDouble("quantity") ?: 0.0
                val unitString = document.getString("unit") ?: UnitOfMeasure.UNIT.name
                val unit = try {
                    UnitOfMeasure.valueOf(unitString)
                } catch (e: Exception) {
                    UnitOfMeasure.UNIT
                }
                val minStock = document.getDouble("minStock") ?: 0.0
                val price = document.getDouble("price") ?: 0.0
                val createdAt = (document.getTimestamp("createdAt")?.toDate() ?: Date())

                val product = Product(
                    id = document.id,
                    name = name,
                    categoryId = categoryId,
                    categoryName = categoryName,
                    quantity = quantity,
                    unit = unit,
                    minStock = minStock,
                    price = price,
                    createdAt = createdAt
                )
                Resource.Success(product)
            } else {
                Resource.Error("Product not found")
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Error retrieving product")
        }
    }

    suspend fun addProduct(product: Product): Resource<String> {
        val productsCollection = getUserProductsCollection() ?: return Resource.Error("User not authenticated")
        val categoriesCollection = getUserCategoriesCollection() ?: return Resource.Error("User not authenticated")
        
        return try {
            // First get the category name
            val categoryDoc = categoriesCollection.document(product.categoryId).get().await()
            val categoryName = categoryDoc.getString("name") ?: "Uncategorized"

            val productMap = hashMapOf(
                "name" to product.name,
                "categoryId" to product.categoryId,
                "categoryName" to categoryName,
                "quantity" to product.quantity,
                "unit" to product.unit.name,
                "minStock" to product.minStock,
                "price" to product.price,
                "createdAt" to Timestamp(product.createdAt)
            )

            val documentRef = productsCollection.add(productMap).await()
            Resource.Success(documentRef.id)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Error adding product")
        }
    }

    suspend fun updateProduct(product: Product): Resource<Unit> {
        val productsCollection = getUserProductsCollection() ?: return Resource.Error("User not authenticated")
        val categoriesCollection = getUserCategoriesCollection() ?: return Resource.Error("User not authenticated")
        
        return try {
            // First get the category name
            val categoryDoc = categoriesCollection.document(product.categoryId).get().await()
            val categoryName = categoryDoc.getString("name") ?: "Uncategorized"

            val productMap = hashMapOf(
                "name" to product.name,
                "categoryId" to product.categoryId,
                "categoryName" to categoryName,
                "quantity" to product.quantity,
                "unit" to product.unit.name,
                "minStock" to product.minStock,
                "price" to product.price,
                "createdAt" to Timestamp(product.createdAt)
            )

            productsCollection.document(product.id).update(productMap as Map<String, Any>).await()
            Resource.Success(Unit)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Error updating product")
        }
    }

    suspend fun deleteProduct(id: String): Resource<Unit> {
        val productsCollection = getUserProductsCollection() ?: return Resource.Error("User not authenticated")
        
        return try {
            productsCollection.document(id).delete().await()
            Resource.Success(Unit)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Error deleting product")
        }
    }

    fun searchProducts(query: String): Flow<Resource<List<Product>>> = callbackFlow {
        trySend(Resource.Loading())
        
        val productsCollection = getUserProductsCollection()
        if (productsCollection == null) {
            trySend(Resource.Error("User not authenticated"))
            close()
            return@callbackFlow
        }

        val subscription = productsCollection
            .orderBy("name")
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    trySend(Resource.Error(error.message ?: "Unknown error"))
                    return@addSnapshotListener
                }

                val products = snapshot?.documents?.mapNotNull { document ->
                    try {
                        val name = document.getString("name") ?: ""
                        
                        // Filter by query
                        if (!name.contains(query, ignoreCase = true)) {
                            return@mapNotNull null
                        }
                        
                        val id = document.id
                        val categoryId = document.getString("categoryId") ?: ""
                        val categoryName = document.getString("categoryName") ?: ""
                        val quantity = document.getDouble("quantity") ?: 0.0
                        val unitString = document.getString("unit") ?: UnitOfMeasure.UNIT.name
                        val unit = try {
                            UnitOfMeasure.valueOf(unitString)
                        } catch (e: Exception) {
                            UnitOfMeasure.UNIT
                        }
                        val minStock = document.getDouble("minStock") ?: 0.0
                        val price = document.getDouble("price") ?: 0.0
                        val createdAt = (document.getTimestamp("createdAt")?.toDate() ?: Date())

                        Product(
                            id = id,
                            name = name,
                            categoryId = categoryId,
                            categoryName = categoryName,
                            quantity = quantity,
                            unit = unit,
                            minStock = minStock,
                            price = price,
                            createdAt = createdAt
                        )
                    } catch (e: Exception) {
                        null
                    }
                } ?: emptyList()

                trySend(Resource.Success(products))
            }

        awaitClose { subscription.remove() }
    }

    fun filterProductsByCategory(categoryId: String): Flow<Resource<List<Product>>> = callbackFlow {
        trySend(Resource.Loading())
        
        val productsCollection = getUserProductsCollection()
        if (productsCollection == null) {
            trySend(Resource.Error("User not authenticated"))
            close()
            return@callbackFlow
        }

        val subscription = productsCollection
            .whereEqualTo("categoryId", categoryId)
            .orderBy("name")
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    trySend(Resource.Error(error.message ?: "Unknown error"))
                    return@addSnapshotListener
                }

                val products = snapshot?.documents?.mapNotNull { document ->
                    try {
                        val id = document.id
                        val name = document.getString("name") ?: ""
                        val categoryId = document.getString("categoryId") ?: ""
                        val categoryName = document.getString("categoryName") ?: ""
                        val quantity = document.getDouble("quantity") ?: 0.0
                        val unitString = document.getString("unit") ?: UnitOfMeasure.UNIT.name
                        val unit = try {
                            UnitOfMeasure.valueOf(unitString)
                        } catch (e: Exception) {
                            UnitOfMeasure.UNIT
                        }
                        val minStock = document.getDouble("minStock") ?: 0.0
                        val price = document.getDouble("price") ?: 0.0
                        val createdAt = (document.getTimestamp("createdAt")?.toDate() ?: Date())

                        Product(
                            id = id,
                            name = name,
                            categoryId = categoryId,
                            categoryName = categoryName,
                            quantity = quantity,
                            unit = unit,
                            minStock = minStock,
                            price = price,
                            createdAt = createdAt
                        )
                    } catch (e: Exception) {
                        null
                    }
                } ?: emptyList()

                trySend(Resource.Success(products))
            }

        awaitClose { subscription.remove() }
    }
}