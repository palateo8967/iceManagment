package com.icerojects.icemanagment.data.repository.order

import com.icerojects.icemanagment.data.remote.firestore.order.OrderFirestoreService
import com.icerojects.icemanagment.domain.model.order.Order
import com.icerojects.icemanagment.domain.model.order.OrderItem
import com.icerojects.icemanagment.domain.model.order.Sale
import com.icerojects.icemanagment.domain.repository.OrderRepository
import com.icerojects.icemanagment.utils.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class OrderRepositoryImpl @Inject constructor(
    private val orderFirestoreService: OrderFirestoreService
) : OrderRepository {
    
    override fun getOrders(): Flow<Resource<List<Order>>> {
        return orderFirestoreService.getOrders()
    }

    override fun getOrderById(orderId: String): Flow<Resource<Order?>> {
        return orderFirestoreService.getOrderById(orderId)
    }

    override suspend fun addOrder(order: Order): Resource<String> {
        return orderFirestoreService.addOrder(order)
    }

    override suspend fun updateOrder(order: Order): Resource<Unit> {
        return orderFirestoreService.updateOrder(order)
    }

    override suspend fun deleteOrder(orderId: String): Resource<Unit> {
        return orderFirestoreService.deleteOrder(orderId)
    }

    override fun getSales(): Flow<Resource<List<Sale>>> {
        return orderFirestoreService.getSales()
    }

    override fun getSaleById(saleId: String): Flow<Resource<Sale?>> {
        return orderFirestoreService.getSaleById(saleId)
    }

    override suspend fun addSale(sale: Sale): Resource<String> {
        return orderFirestoreService.addSale(sale)
    }

    override suspend fun getNextSaleNumber(): Resource<Int> {
        return orderFirestoreService.getNextSaleNumber()
    }

    override suspend fun addOrderItem(orderId: String, item: OrderItem): Resource<String> {
        return orderFirestoreService.addOrderItem(orderId, item)
    }

    override suspend fun removeOrderItem(orderId: String, itemId: String): Resource<Unit> {
        return orderFirestoreService.removeOrderItem(orderId, itemId)
    }

    override suspend fun updateOrderItem(orderId: String, item: OrderItem): Resource<Unit> {
        return orderFirestoreService.updateOrderItem(orderId, item)
    }
}