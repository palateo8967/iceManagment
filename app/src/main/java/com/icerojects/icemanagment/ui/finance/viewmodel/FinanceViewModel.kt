package com.icerojects.icemanagment.ui.finance.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.icerojects.icemanagment.domain.model.order.Order
import com.icerojects.icemanagment.domain.model.order.Sale
import com.icerojects.icemanagment.domain.repository.OrderRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FinanceViewModel @Inject constructor(
    private val orderRepository: OrderRepository
) : ViewModel() {
    
    private val _orders = MutableStateFlow<List<Order>>(emptyList())
    val orders: StateFlow<List<Order>> = _orders.asStateFlow()
    
    private val _selectedOrder = MutableStateFlow<Order?>(null)
    val selectedOrder: StateFlow<Order?> = _selectedOrder.asStateFlow()

    // Loading state for fetching a single order
    private val _orderLoading = MutableStateFlow(false)
    val orderLoading: StateFlow<Boolean> = _orderLoading.asStateFlow()
    
    private val _sales = MutableStateFlow<List<Sale>>(emptyList())
    val sales: StateFlow<List<Sale>> = _sales.asStateFlow()

    private val _selectedSale = MutableStateFlow<Sale?>(null)
    val selectedSale: StateFlow<Sale?> = _selectedSale.asStateFlow()
    
    init {
        loadOrders()
        loadSales()
    }
    
    private fun loadOrders() {
        viewModelScope.launch {
            try {
                orderRepository.getOrders().collect { resource ->
                    if (resource.data != null) {
                        _orders.value = resource.data
                    }
                }
            } catch (e: Exception) {
                // Manejar error
            }
        }
    }
    
    fun getOrderById(orderId: String) {
        viewModelScope.launch {
            try {
                orderRepository.getOrderById(orderId).collect { resource ->
                    when (resource) {
                        is com.icerojects.icemanagment.utils.Resource.Loading -> {
                            _orderLoading.value = true
                        }
                        is com.icerojects.icemanagment.utils.Resource.Success -> {
                            _orderLoading.value = false
                            _selectedOrder.value = resource.data
                        }
                        is com.icerojects.icemanagment.utils.Resource.Error -> {
                            _orderLoading.value = false
                            _selectedOrder.value = null
                        }
                    }
                }
            } catch (e: Exception) {
                // Manejar error
                _orderLoading.value = false
            }
        }
    }

    private fun loadSales() {
        viewModelScope.launch {
            try {
                orderRepository.getSales().collect { resource ->
                    if (resource.data != null) {
                        _sales.value = resource.data
                    }
                }
            } catch (e: Exception) {
                // Manejar error
            }
        }
    }

    fun getSaleByOrderId(orderId: String) {
        viewModelScope.launch {
            try {
                // If sales are already loaded, find locally first
                val sale = _sales.value.firstOrNull { it.orderId == orderId }
                if (sale != null) {
                    _selectedSale.value = sale
                    return@launch
                }
                // Fallback: refresh sales and try again
                orderRepository.getSales().collect { resource ->
                    val list = resource.data ?: emptyList()
                    _sales.value = list
                    _selectedSale.value = list.firstOrNull { it.orderId == orderId }
                }
            } catch (e: Exception) {
                // Manejar error
            }
        }
    }
}