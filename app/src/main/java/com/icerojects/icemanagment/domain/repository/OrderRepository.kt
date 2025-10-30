package com.icerojects.icemanagment.domain.repository

import com.icerojects.icemanagment.domain.model.order.Order
import com.icerojects.icemanagment.domain.model.order.OrderItem
import com.icerojects.icemanagment.domain.model.order.Sale
import com.icerojects.icemanagment.utils.Resource
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for order operations
 */
interface OrderRepository {
    fun getOrders(): Flow<Resource<List<Order>>>
    fun getOrderById(orderId: String): Flow<Resource<Order?>>
    suspend fun addOrder(order: Order): Resource<String>
    suspend fun updateOrder(order: Order): Resource<Unit>
    suspend fun deleteOrder(orderId: String): Resource<Unit>
    
    fun getSales(): Flow<Resource<List<Sale>>>
    fun getSaleById(saleId: String): Flow<Resource<Sale?>>
    suspend fun addSale(sale: Sale): Resource<String>
    suspend fun getNextSaleNumber(): Resource<Int>
    
    suspend fun addOrderItem(orderId: String, item: OrderItem): Resource<String>
    suspend fun removeOrderItem(orderId: String, itemId: String): Resource<Unit>
    suspend fun updateOrderItem(orderId: String, item: OrderItem): Resource<Unit>
}