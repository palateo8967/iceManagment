package com.icerojects.icemanagment.data.remote.firestore.order

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.icerojects.icemanagment.data.remote.firestore.FirestoreManager
import com.icerojects.icemanagment.domain.model.order.Order
import com.icerojects.icemanagment.domain.model.order.OrderItem
import com.icerojects.icemanagment.domain.model.order.OrderStatus
import com.icerojects.icemanagment.domain.model.order.Sale
import com.icerojects.icemanagment.utils.Resource
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import java.util.Date
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class OrderFirestoreService @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth,
    private val firestoreManager: FirestoreManager
) {
    // Get current user ID
    private fun getCurrentUserId(): String {
        return auth.currentUser?.uid ?: throw IllegalStateException("User not authenticated")
    }

    // Orders collection reference
    private fun ordersCollection() = firestore.collection("users")
        .document(getCurrentUserId())
        .collection("orders")

    // Sales collection reference
    private fun salesCollection() = firestore.collection("users")
        .document(getCurrentUserId())
        .collection("sales")

    // Get all orders
    fun getOrders(): Flow<Resource<List<Order>>> = callbackFlow {
        trySend(Resource.Loading())

        val listener = ordersCollection()
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    trySend(Resource.Error(error.message ?: "Error fetching orders"))
                    return@addSnapshotListener
                }

                if (snapshot != null) {
                    val orders = snapshot.documents.mapNotNull { document ->
                        try {
                            val id = document.id
                            val createdAt = document.getTimestamp("createdAt")?.toDate() ?: Date()
                            val status = document.getString("status")?.let {
                                try {
                                    OrderStatus.valueOf(it)
                                } catch (e: IllegalArgumentException) {
                                    OrderStatus.PENDING
                                }
                            } ?: OrderStatus.PENDING
                            val totalAmount = document.getDouble("totalAmount") ?: 0.0

                            // Get order items
                            val itemsMap = document.get("items") as? List<Map<String, Any>> ?: emptyList()
                            val items = itemsMap.map { item ->
                                OrderItem(
                                    id = (item["id"] as? String) ?: "",
                                    productId = (item["productId"] as? String) ?: "",
                                    productName = (item["productName"] as? String) ?: "",
                                    quantity = (item["quantity"] as? Double) ?: 0.0,
                                    unitPrice = (item["unitPrice"] as? Double) ?: 0.0,
                                    totalPrice = (item["totalPrice"] as? Double) ?: 0.0
                                )
                            }

                            Order(
                                id = id,
                                items = items,
                                totalAmount = totalAmount,
                                createdAt = createdAt,
                                status = status
                            )
                        } catch (e: Exception) {
                            null
                        }
                    }
                    trySend(Resource.Success(orders))
                }
            }

        awaitClose { listener.remove() }
    }

    // Get order by ID
    fun getOrderById(orderId: String): Flow<Resource<Order?>> = callbackFlow {
        trySend(Resource.Loading())

        val listener = ordersCollection().document(orderId)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    trySend(Resource.Error(error.message ?: "Error fetching order"))
                    return@addSnapshotListener
                }

                if (snapshot != null && snapshot.exists()) {
                    try {
                        val id = snapshot.id
                        val createdAt = snapshot.getTimestamp("createdAt")?.toDate() ?: Date()
                        val status = snapshot.getString("status")?.let {
                            try {
                                OrderStatus.valueOf(it)
                            } catch (e: IllegalArgumentException) {
                                OrderStatus.PENDING
                            }
                        } ?: OrderStatus.PENDING
                        val totalAmount = snapshot.getDouble("totalAmount") ?: 0.0

                        // Get order items
                        val itemsMap = snapshot.get("items") as? List<Map<String, Any>> ?: emptyList()
                        val items = itemsMap.map { item ->
                            OrderItem(
                                id = (item["id"] as? String) ?: "",
                                productId = (item["productId"] as? String) ?: "",
                                productName = (item["productName"] as? String) ?: "",
                                quantity = (item["quantity"] as? Double) ?: 0.0,
                                unitPrice = (item["unitPrice"] as? Double) ?: 0.0,
                                totalPrice = (item["totalPrice"] as? Double) ?: 0.0
                            )
                        }

                        val order = Order(
                            id = id,
                            items = items,
                            totalAmount = totalAmount,
                            createdAt = createdAt,
                            status = status
                        )
                        trySend(Resource.Success(order))
                    } catch (e: Exception) {
                        trySend(Resource.Error(e.message ?: "Error parsing order"))
                    }
                } else {
                    trySend(Resource.Success(null))
                }
            }

        awaitClose { listener.remove() }
    }

    // Add new order
    suspend fun addOrder(order: Order): Resource<String> {
        return try {
            val orderData = hashMapOf(
                "createdAt" to order.createdAt,
                "status" to order.status.name,
                "totalAmount" to order.totalAmount,
                "items" to order.items.map { item ->
                    hashMapOf(
                        "id" to item.id,
                        "productId" to item.productId,
                        "productName" to item.productName,
                        "quantity" to item.quantity,
                        "unitPrice" to item.unitPrice,
                        "totalPrice" to item.totalPrice
                    )
                }
            )

            val documentRef = ordersCollection().add(orderData).await()
            Resource.Success(documentRef.id)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Error adding order")
        }
    }

    // Update order
    suspend fun updateOrder(order: Order): Resource<Unit> {
        return try {
            val orderData = hashMapOf(
                "status" to order.status.name,
                "totalAmount" to order.totalAmount,
                "items" to order.items.map { item ->
                    hashMapOf(
                        "id" to item.id,
                        "productId" to item.productId,
                        "productName" to item.productName,
                        "quantity" to item.quantity,
                        "unitPrice" to item.unitPrice,
                        "totalPrice" to item.totalPrice
                    )
                }
            )

            ordersCollection().document(order.id).update(orderData as Map<String, Any>).await()
            Resource.Success(Unit)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Error updating order")
        }
    }

    // Delete order
    suspend fun deleteOrder(orderId: String): Resource<Unit> {
        return try {
            ordersCollection().document(orderId).delete().await()
            Resource.Success(Unit)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Error deleting order")
        }
    }

    // Add order item
    suspend fun addOrderItem(orderId: String, item: OrderItem): Resource<String> {
        return try {
            val order = getOrderByIdSync(orderId) ?: return Resource.Error("Order not found")
            
            val newItems = order.items.toMutableList()
            val newItem = item.copy(id = firestoreManager.generateId())
            newItems.add(newItem)
            
            val newTotalAmount = order.items.sumOf { it.totalPrice } + item.totalPrice
            
            val orderData = hashMapOf(
                "totalAmount" to newTotalAmount,
                "items" to newItems.map { orderItem ->
                    hashMapOf(
                        "id" to orderItem.id,
                        "productId" to orderItem.productId,
                        "productName" to orderItem.productName,
                        "quantity" to orderItem.quantity,
                        "unitPrice" to orderItem.unitPrice,
                        "totalPrice" to orderItem.totalPrice
                    )
                }
            )
            
            ordersCollection().document(orderId).update(orderData as Map<String, Any>).await()
            Resource.Success(newItem.id)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Error adding order item")
        }
    }

    // Remove order item
    suspend fun removeOrderItem(orderId: String, itemId: String): Resource<Unit> {
        return try {
            val order = getOrderByIdSync(orderId) ?: return Resource.Error("Order not found")
            
            val newItems = order.items.filter { it.id != itemId }
            val newTotalAmount = newItems.sumOf { it.totalPrice }
            
            val orderData = hashMapOf(
                "totalAmount" to newTotalAmount,
                "items" to newItems.map { orderItem ->
                    hashMapOf(
                        "id" to orderItem.id,
                        "productId" to orderItem.productId,
                        "productName" to orderItem.productName,
                        "quantity" to orderItem.quantity,
                        "unitPrice" to orderItem.unitPrice,
                        "totalPrice" to orderItem.totalPrice
                    )
                }
            )
            
            ordersCollection().document(orderId).update(orderData as Map<String, Any>).await()
            Resource.Success(Unit)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Error removing order item")
        }
    }

    // Update order item
    suspend fun updateOrderItem(orderId: String, item: OrderItem): Resource<Unit> {
        return try {
            val order = getOrderByIdSync(orderId) ?: return Resource.Error("Order not found")
            
            val newItems = order.items.map { 
                if (it.id == item.id) item else it 
            }
            
            val newTotalAmount = newItems.sumOf { it.totalPrice }
            
            val orderData = hashMapOf(
                "totalAmount" to newTotalAmount,
                "items" to newItems.map { orderItem ->
                    hashMapOf(
                        "id" to orderItem.id,
                        "productId" to orderItem.productId,
                        "productName" to orderItem.productName,
                        "quantity" to orderItem.quantity,
                        "unitPrice" to orderItem.unitPrice,
                        "totalPrice" to orderItem.totalPrice
                    )
                }
            )
            
            ordersCollection().document(orderId).update(orderData as Map<String, Any>).await()
            Resource.Success(Unit)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Error updating order item")
        }
    }

    // Get sales
    fun getSales(): Flow<Resource<List<Sale>>> = callbackFlow {
        trySend(Resource.Loading())

        val listener = salesCollection()
            .orderBy("date", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    trySend(Resource.Error(error.message ?: "Error fetching sales"))
                    return@addSnapshotListener
                }

                if (snapshot != null) {
                    val sales = snapshot.documents.mapNotNull { document ->
                        try {
                            val id = document.id
                            val saleNumber = document.getLong("saleNumber")?.toInt() ?: 0
                            val orderId = document.getString("orderId") ?: ""
                            val customerName = document.getString("customerName") ?: ""
                            val date = document.getTimestamp("date")?.toDate() ?: Date()
                            val totalAmount = document.getDouble("totalAmount") ?: 0.0
                            val profit = document.getDouble("profit") ?: 0.0
                            val paymentMethod = document.getString("paymentMethod") ?: "CASH"

                            // Get sale items
                            val itemsMap = document.get("items") as? List<Map<String, Any>> ?: emptyList()
                            val items = itemsMap.map { item ->
                                OrderItem(
                                    id = (item["id"] as? String) ?: "",
                                    productId = (item["productId"] as? String) ?: "",
                                    productName = (item["productName"] as? String) ?: "",
                                    quantity = (item["quantity"] as? Double) ?: 0.0,
                                    unitPrice = (item["unitPrice"] as? Double) ?: 0.0,
                                    totalPrice = (item["totalPrice"] as? Double) ?: 0.0
                                )
                            }

                            Sale(
                                id = id,
                                saleNumber = saleNumber,
                                orderId = orderId,
                                customerName = customerName,
                                items = items,
                                totalAmount = totalAmount,
                                profit = profit,
                                paymentMethod = try {
                                    com.icerojects.icemanagment.domain.model.order.PaymentMethod.valueOf(paymentMethod)
                                } catch (e: IllegalArgumentException) {
                                    com.icerojects.icemanagment.domain.model.order.PaymentMethod.CASH
                                },
                                date = date
                            )
                        } catch (e: Exception) {
                            null
                        }
                    }
                    trySend(Resource.Success(sales))
                }
            }

        awaitClose { listener.remove() }
    }

    // Get sale by ID
    fun getSaleById(saleId: String): Flow<Resource<Sale?>> = callbackFlow {
        trySend(Resource.Loading())

        val listener = salesCollection().document(saleId)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    trySend(Resource.Error(error.message ?: "Error fetching sale"))
                    return@addSnapshotListener
                }

                if (snapshot != null && snapshot.exists()) {
                    try {
                        val id = snapshot.id
                        val saleNumber = snapshot.getLong("saleNumber")?.toInt() ?: 0
                        val orderId = snapshot.getString("orderId") ?: ""
                        val customerName = snapshot.getString("customerName") ?: ""
                        val date = snapshot.getTimestamp("date")?.toDate() ?: Date()
                        val totalAmount = snapshot.getDouble("totalAmount") ?: 0.0
                        val profit = snapshot.getDouble("profit") ?: 0.0
                        val paymentMethod = snapshot.getString("paymentMethod") ?: "CASH"

                        // Get sale items
                        val itemsMap = snapshot.get("items") as? List<Map<String, Any>> ?: emptyList()
                        val items = itemsMap.map { item ->
                            OrderItem(
                                id = (item["id"] as? String) ?: "",
                                productId = (item["productId"] as? String) ?: "",
                                productName = (item["productName"] as? String) ?: "",
                                quantity = (item["quantity"] as? Double) ?: 0.0,
                                unitPrice = (item["unitPrice"] as? Double) ?: 0.0,
                                totalPrice = (item["totalPrice"] as? Double) ?: 0.0
                            )
                        }

                        val sale = Sale(
                            id = id,
                            saleNumber = saleNumber,
                            orderId = orderId,
                            customerName = customerName,
                            items = items,
                            totalAmount = totalAmount,
                            profit = profit,
                            paymentMethod = try {
                                com.icerojects.icemanagment.domain.model.order.PaymentMethod.valueOf(paymentMethod)
                            } catch (e: IllegalArgumentException) {
                                com.icerojects.icemanagment.domain.model.order.PaymentMethod.CASH
                            },
                            date = date
                        )
                        trySend(Resource.Success(sale))
                    } catch (e: Exception) {
                        trySend(Resource.Error(e.message ?: "Error parsing sale"))
                    }
                } else {
                    trySend(Resource.Success(null))
                }
            }

        awaitClose { listener.remove() }
    }

    // Add new sale
    suspend fun addSale(sale: Sale): Resource<String> {
        return try {
            val saleData = hashMapOf(
                "saleNumber" to sale.saleNumber,
                "orderId" to sale.orderId,
                "customerName" to sale.customerName,
                "date" to sale.date,
                "totalAmount" to sale.totalAmount,
                "profit" to sale.profit,
                "paymentMethod" to sale.paymentMethod.name,
                "items" to sale.items.map { item ->
                    hashMapOf(
                        "id" to item.id,
                        "productId" to item.productId,
                        "productName" to item.productName,
                        "quantity" to item.quantity,
                        "unitPrice" to item.unitPrice,
                        "totalPrice" to item.totalPrice
                    )
                }
            )

            val documentRef = salesCollection().add(saleData).await()
            Resource.Success(documentRef.id)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Error adding sale")
        }
    }

    // Get next sale number
    suspend fun getNextSaleNumber(): Resource<Int> {
        return try {
            val snapshot = salesCollection()
                .orderBy("saleNumber", Query.Direction.DESCENDING)
                .limit(1)
                .get()
                .await()

            val nextNumber = if (snapshot.isEmpty) {
                1
            } else {
                val lastSaleNumber = snapshot.documents[0].getLong("saleNumber")?.toInt() ?: 0
                lastSaleNumber + 1
            }

            Resource.Success(nextNumber)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Error getting next sale number")
        }
    }

    // Helper method to get order by ID synchronously
    private suspend fun getOrderByIdSync(orderId: String): Order? {
        return try {
            val document = ordersCollection().document(orderId).get().await()
            if (document.exists()) {
                val id = document.id
                val createdAt = document.getTimestamp("createdAt")?.toDate() ?: Date()
                val status = document.getString("status")?.let {
                    try {
                        OrderStatus.valueOf(it)
                    } catch (e: IllegalArgumentException) {
                        OrderStatus.PENDING
                    }
                } ?: OrderStatus.PENDING
                val totalAmount = document.getDouble("totalAmount") ?: 0.0

                // Get order items
                val itemsMap = document.get("items") as? List<Map<String, Any>> ?: emptyList()
                val items = itemsMap.map { item ->
                    OrderItem(
                        id = (item["id"] as? String) ?: "",
                        productId = (item["productId"] as? String) ?: "",
                        productName = (item["productName"] as? String) ?: "",
                        quantity = (item["quantity"] as? Double) ?: 0.0,
                        unitPrice = (item["unitPrice"] as? Double) ?: 0.0,
                        totalPrice = (item["totalPrice"] as? Double) ?: 0.0
                    )
                }

                Order(
                    id = id,
                    items = items,
                    totalAmount = totalAmount,
                    createdAt = createdAt,
                    status = status
                )
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }
}