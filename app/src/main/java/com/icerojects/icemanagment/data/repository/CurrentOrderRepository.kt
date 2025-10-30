package com.icerojects.icemanagment.data.repository

import com.icerojects.icemanagment.domain.model.order.OrderItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Repository para mantener el estado del pedido actual entre pantallas
 */
@Singleton
class CurrentOrderRepository @Inject constructor() {
    
    private val _currentOrderItems = MutableStateFlow<List<OrderItem>>(emptyList())
    val currentOrderItems: StateFlow<List<OrderItem>> = _currentOrderItems.asStateFlow()
    
    private val _totalAmount = MutableStateFlow(0.0)
    val totalAmount: StateFlow<Double> = _totalAmount.asStateFlow()
    
    fun updateOrderItems(items: List<OrderItem>, total: Double) {
        _currentOrderItems.value = items
        _totalAmount.value = total
    }
    
    fun clearOrder() {
        _currentOrderItems.value = emptyList()
        _totalAmount.value = 0.0
    }
}