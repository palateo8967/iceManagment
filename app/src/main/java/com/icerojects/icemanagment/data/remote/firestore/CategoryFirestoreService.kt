package com.icerojects.icemanagment.data.remote.firestore

import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.icerojects.icemanagment.domain.model.Category
import com.icerojects.icemanagment.utils.Resource
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import java.util.Date
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CategoryFirestoreService @Inject constructor() {
    private val db: FirebaseFirestore = Firebase.firestore
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    
    // Get user-specific categories collection
    private fun getUserCategoriesCollection(): CollectionReference? {
        val userId = auth.currentUser?.uid ?: return null
        return db.collection("users").document(userId).collection("categories")
    }

    fun getCategories(): Flow<Resource<List<Category>>> = callbackFlow {
        trySend(Resource.Loading())
        
        val userId = auth.currentUser?.uid
        if (userId == null) {
            trySend(Resource.Error("User not authenticated"))
            close()
            return@callbackFlow
        }
        
        val categoriesCollection = getUserCategoriesCollection()
        if (categoriesCollection == null) {
            trySend(Resource.Error("User not authenticated"))
            close()
            return@callbackFlow
        }

        val subscription = categoriesCollection
            .orderBy("name")
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    trySend(Resource.Error(error.message ?: "Unknown error"))
                    return@addSnapshotListener
                }

                val categories = snapshot?.documents?.mapNotNull { document ->
                    try {
                        val id = document.id
                        val name = document.getString("name") ?: ""
                        val createdAt = (document.getTimestamp("createdAt")?.toDate() ?: Date())

                        Category(
                            id = id,
                            name = name,
                            createdAt = createdAt
                        )
                    } catch (e: Exception) {
                        null
                    }
                } ?: emptyList()

                trySend(Resource.Success(categories))
            }

        awaitClose { subscription.remove() }
    }

    suspend fun getCategoryById(id: String): Resource<Category> {
        val categoriesCollection = getUserCategoriesCollection() ?: return Resource.Error("User not authenticated")
        
        return try {
            val document = categoriesCollection.document(id).get().await()
            if (document.exists()) {
                val name = document.getString("name") ?: ""
                val createdAt = (document.getTimestamp("createdAt")?.toDate() ?: Date())

                val category = Category(
                    id = document.id,
                    name = name,
                    createdAt = createdAt
                )
                Resource.Success(category)
            } else {
                Resource.Error("Category not found")
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Error retrieving category")
        }
    }

    suspend fun addCategory(category: Category): Resource<String> {
        val categoriesCollection = getUserCategoriesCollection() ?: return Resource.Error("User not authenticated")
        
        return try {
            val categoryMap = hashMapOf(
                "name" to category.name,
                "createdAt" to Timestamp(category.createdAt)
            )

            val documentRef = categoriesCollection.add(categoryMap).await()
            Resource.Success(documentRef.id)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Error adding category")
        }
    }

    suspend fun updateCategory(category: Category): Resource<Unit> {
        val categoriesCollection = getUserCategoriesCollection() ?: return Resource.Error("User not authenticated")
        
        return try {
            val categoryMap = hashMapOf(
                "name" to category.name,
                "createdAt" to Timestamp(category.createdAt)
            )

            categoriesCollection.document(category.id).update(categoryMap as Map<String, Any>).await()
            Resource.Success(Unit)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Error updating category")
        }
    }

    suspend fun deleteCategory(id: String): Resource<Unit> {
        val userId = auth.currentUser?.uid ?: return Resource.Error("User not authenticated")
        val categoriesCollection = getUserCategoriesCollection() ?: return Resource.Error("User not authenticated")
        
        return try {
            // First check if there are products using this category
            val productsWithCategory = db.collection("users").document(userId).collection("products")
                .whereEqualTo("categoryId", id)
                .limit(1)
                .get()
                .await()

            if (!productsWithCategory.isEmpty) {
                return Resource.Error("Cannot delete category because there are products using it")
            }

            categoriesCollection.document(id).delete().await()
            Resource.Success(Unit)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Error deleting category")
        }
    }
}