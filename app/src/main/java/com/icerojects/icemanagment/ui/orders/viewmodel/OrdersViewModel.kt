package com.icerojects.icemanagment.ui.orders.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.icerojects.icemanagment.domain.model.Category
import com.icerojects.icemanagment.domain.model.Product
import com.icerojects.icemanagment.domain.model.order.CashPayment
import com.icerojects.icemanagment.domain.model.order.Order
import com.icerojects.icemanagment.domain.model.order.OrderItem
import com.icerojects.icemanagment.domain.model.order.OrderStatus
import com.icerojects.icemanagment.domain.model.order.PaymentMethod
import com.icerojects.icemanagment.domain.model.order.Sale
import com.icerojects.icemanagment.domain.use_case.category.GetCategoriesUseCase
import com.icerojects.icemanagment.domain.use_case.order.AddOrderItemUseCase
import com.icerojects.icemanagment.domain.use_case.order.AddOrderUseCase
import com.icerojects.icemanagment.domain.use_case.order.AddSaleUseCase
import com.icerojects.icemanagment.domain.use_case.order.GetNextSaleNumberUseCase
import com.icerojects.icemanagment.domain.use_case.order.GetOrdersUseCase
import com.icerojects.icemanagment.domain.use_case.order.RemoveOrderItemUseCase
import com.icerojects.icemanagment.domain.use_case.order.UpdateOrderItemUseCase
import com.icerojects.icemanagment.domain.use_case.product.FilterProductsByCategoryUseCase
import com.icerojects.icemanagment.domain.use_case.product.GetProductsUseCase
import com.icerojects.icemanagment.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.util.Date
import java.util.UUID
import javax.inject.Inject

/**
 * ViewModel for orders management
 */
@HiltViewModel
class OrdersViewModel @Inject constructor(
    private val getCategoriesUseCase: GetCategoriesUseCase,
    private val getProductsUseCase: GetProductsUseCase,
    private val filterProductsByCategoryUseCase: FilterProductsByCategoryUseCase,
    private val getOrdersUseCase: GetOrdersUseCase,
    private val addOrderUseCase: AddOrderUseCase,
    private val addOrderItemUseCase: AddOrderItemUseCase,
    private val removeOrderItemUseCase: RemoveOrderItemUseCase,
    private val updateOrderItemUseCase: UpdateOrderItemUseCase,
    private val addSaleUseCase: AddSaleUseCase,
    private val getNextSaleNumberUseCase: GetNextSaleNumberUseCase
) : ViewModel() {

    // State for categories
    private val _categoriesState = mutableStateOf<CategoriesState>(CategoriesState())
    val categoriesState: State<CategoriesState> = _categoriesState

    // State for products
    private val _productsState = mutableStateOf<ProductsState>(ProductsState())
    val productsState: State<ProductsState> = _productsState

    // State for orders
    private val _ordersState = mutableStateOf<OrdersState>(OrdersState())
    val ordersState: State<OrdersState> = _ordersState

    // State for current order - usando companion object para mantener el estado entre pantallas
    companion object {
        private val _sharedCurrentOrderState = mutableStateOf<CurrentOrderState>(CurrentOrderState())
    }
    private val _currentOrderState = _sharedCurrentOrderState
    val currentOrderState: State<CurrentOrderState> = _currentOrderState

    // State for checkout
    private val _checkoutState = mutableStateOf<CheckoutState>(CheckoutState())
    val checkoutState: State<CheckoutState> = _checkoutState

    // State for cash payment
    private val _cashPaymentState = mutableStateOf<CashPaymentState>(CashPaymentState())
    val cashPaymentState: State<CashPaymentState> = _cashPaymentState

    // State for selected category
    private val _selectedCategoryId = mutableStateOf<String?>(null)
    val selectedCategoryId: State<String?> = _selectedCategoryId

    // UI events
    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow: SharedFlow<UiEvent> = _eventFlow

    init {
        loadCategories()
        loadProducts()
        loadOrders()
    }

    // Functions to load data
    private fun loadCategories() {
        getCategoriesUseCase().onEach { result ->
            when (result) {
                is Resource.Success -> {
                    _categoriesState.value = CategoriesState(
                        categories = result.data ?: emptyList(),
                        isLoading = false
                    )
                }
                is Resource.Error -> {
                    _categoriesState.value = CategoriesState(
                        error = result.message ?: "Unknown error",
                        isLoading = false
                    )
                    _eventFlow.emit(UiEvent.ShowSnackbar(result.message ?: "Unknown error"))
                }
                is Resource.Loading -> {
                    _categoriesState.value = CategoriesState(
                        categories = result.data ?: emptyList(),
                        isLoading = true
                    )
                }
            }
        }.launchIn(viewModelScope)
    }

    private fun loadProducts() {
        getProductsUseCase().onEach { result ->
            when (result) {
                is Resource.Success -> {
                    _productsState.value = ProductsState(
                        products = result.data ?: emptyList(),
                        isLoading = false
                    )
                }
                is Resource.Error -> {
                    _productsState.value = ProductsState(
                        error = result.message ?: "Unknown error",
                        isLoading = false
                    )
                    _eventFlow.emit(UiEvent.ShowSnackbar(result.message ?: "Unknown error"))
                }
                is Resource.Loading -> {
                    _productsState.value = ProductsState(
                        products = result.data ?: emptyList(),
                        isLoading = true
                    )
                }
            }
        }.launchIn(viewModelScope)
    }

    private fun loadOrders() {
        getOrdersUseCase().onEach { result ->
            when (result) {
                is Resource.Success -> {
                    _ordersState.value = OrdersState(
                        orders = result.data ?: emptyList(),
                        isLoading = false
                    )
                }
                is Resource.Error -> {
                    _ordersState.value = OrdersState(
                        error = result.message ?: "Unknown error",
                        isLoading = false
                    )
                    _eventFlow.emit(UiEvent.ShowSnackbar(result.message ?: "Unknown error"))
                }
                is Resource.Loading -> {
                    _ordersState.value = OrdersState(
                        orders = result.data ?: emptyList(),
                        isLoading = true
                    )
                }
            }
        }.launchIn(viewModelScope)
    }

    // Functions to handle UI events
    fun onEvent(event: OrdersEvent) {
        when (event) {
            is OrdersEvent.SelectCategory -> {
                _selectedCategoryId.value = event.categoryId
                if (event.categoryId != null) {
                    filterProductsByCategoryUseCase(event.categoryId).onEach { result ->
                        when (result) {
                            is Resource.Success -> {
                                _productsState.value = ProductsState(
                                    products = result.data ?: emptyList(),
                                    isLoading = false
                                )
                            }
                            is Resource.Error -> {
                                _productsState.value = ProductsState(
                                    error = result.message ?: "Filter error",
                                    isLoading = false
                                )
                            }
                            is Resource.Loading -> {
                                _productsState.value = ProductsState(
                                    products = _productsState.value.products,
                                    isLoading = true
                                )
                            }
                        }
                    }.launchIn(viewModelScope)
                } else {
                    loadProducts()
                }
            }
            
            is OrdersEvent.AddProductToOrder -> {
                val product = event.product
                val quantity = event.quantity
                
                if (quantity <= 0) {
                    viewModelScope.launch {
                        _eventFlow.emit(UiEvent.ShowSnackbar("La cantidad debe ser mayor que 0"))
                    }
                    return
                }
                
                // Verificar si hay suficiente stock
                if (quantity > product.quantity) {
                    viewModelScope.launch {
                        _eventFlow.emit(UiEvent.ShowSnackbar("No hay suficiente stock disponible"))
                    }
                    return
                }
                
                val orderItems = _currentOrderState.value.orderItems.toMutableList()
                val existingItem = orderItems.find { it.productId == product.id }
                
                if (existingItem != null) {
                    // Update existing item
                    val updatedItem = existingItem.copy(
                        quantity = existingItem.quantity + quantity,
                        totalPrice = (existingItem.quantity + quantity) * existingItem.unitPrice
                    )
                    val index = orderItems.indexOf(existingItem)
                    orderItems[index] = updatedItem
                } else {
                    // Add new item
                    val newItem = OrderItem(
                        id = UUID.randomUUID().toString(),
                        productId = product.id,
                        productName = product.name,
                        quantity = quantity,
                        unitPrice = product.price,
                        totalPrice = quantity * product.price
                    )
                    orderItems.add(newItem)
                }
                
                val totalAmount = orderItems.sumOf { it.totalPrice }
                
                _currentOrderState.value = _currentOrderState.value.copy(
                    orderItems = orderItems,
                    totalAmount = totalAmount
                )
                
                // Actualizar el producto en tiempo real (reducir stock)
                val updatedProduct = product.copy(
                    quantity = product.quantity - quantity
                )
                
                // Actualizar la lista de productos
                val updatedProducts = _productsState.value.products.map {
                    if (it.id == product.id) updatedProduct else it
                }
                
                _productsState.value = _productsState.value.copy(
                    products = updatedProducts
                )
            }
            
            is OrdersEvent.RemoveProductFromOrder -> {
                val orderItems = _currentOrderState.value.orderItems.toMutableList()
                val itemToRemove = orderItems.find { it.id == event.orderItemId }
                
                if (itemToRemove != null) {
                    // Restaurar el stock del producto
                    val productId = itemToRemove.productId
                    val quantityToRestore = itemToRemove.quantity
                    
                    // Actualizar la lista de productos
                    val updatedProducts = _productsState.value.products.map { product ->
                        if (product.id == productId) {
                            product.copy(quantity = product.quantity + quantityToRestore)
                        } else {
                            product
                        }
                    }
                    
                    _productsState.value = _productsState.value.copy(
                        products = updatedProducts
                    )
                    
                    // Eliminar el item del pedido
                    orderItems.remove(itemToRemove)
                    
                    val totalAmount = orderItems.sumOf { it.totalPrice }
                    
                    _currentOrderState.value = _currentOrderState.value.copy(
                        orderItems = orderItems,
                        totalAmount = totalAmount
                    )
                }
            }
            
            is OrdersEvent.UpdateProductQuantity -> {
                val orderItems = _currentOrderState.value.orderItems.toMutableList()
                val itemToUpdate = orderItems.find { it.id == event.orderItemId }
                
                if (itemToUpdate != null) {
                    val newQuantity = itemToUpdate.quantity + event.quantityChange
                    
                    if (newQuantity <= 0) {
                        // Remove item if quantity becomes 0 or negative
                        // Restaurar el stock del producto
                        val productId = itemToUpdate.productId
                        val quantityToRestore = itemToUpdate.quantity
                        
                        // Actualizar la lista de productos
                        val updatedProducts = _productsState.value.products.map { product ->
                            if (product.id == productId) {
                                product.copy(quantity = product.quantity + quantityToRestore)
                            } else {
                                product
                            }
                        }
                        
                        _productsState.value = _productsState.value.copy(
                            products = updatedProducts
                        )
                        
                        orderItems.remove(itemToUpdate)
                    } else {
                        // Encontrar el producto correspondiente
                        val product = _productsState.value.products.find { it.id == itemToUpdate.productId }
                        
                        if (product != null) {
                            val quantityDifference = event.quantityChange
                            
                            // Verificar si hay suficiente stock para aumentar la cantidad
                            if (quantityDifference > 0 && quantityDifference > product.quantity) {
                                viewModelScope.launch {
                                    _eventFlow.emit(UiEvent.ShowSnackbar("No hay suficiente stock disponible"))
                                }
                                return
                            }
                            
                            // Actualizar el stock del producto
                            val updatedProduct = product.copy(
                                quantity = product.quantity - quantityDifference
                            )
                            
                            // Actualizar la lista de productos
                            val updatedProducts = _productsState.value.products.map {
                                if (it.id == product.id) updatedProduct else it
                            }
                            
                            _productsState.value = _productsState.value.copy(
                                products = updatedProducts
                            )
                            
                            // Update item quantity
                            val updatedItem = itemToUpdate.copy(
                                quantity = newQuantity,
                                totalPrice = newQuantity * itemToUpdate.unitPrice
                            )
                            val index = orderItems.indexOf(itemToUpdate)
                            orderItems[index] = updatedItem
                        }
                    }
                    
                    val totalAmount = orderItems.sumOf { it.totalPrice }
                    
                    _currentOrderState.value = _currentOrderState.value.copy(
                        orderItems = orderItems,
                        totalAmount = totalAmount
                    )
                }
            }
            
            is OrdersEvent.ConfirmOrder -> {
                if (_currentOrderState.value.orderItems.isEmpty()) {
                    viewModelScope.launch {
                        _eventFlow.emit(UiEvent.ShowSnackbar("No hay productos en el pedido"))
                    }
                    return
                }
                
                // Guardamos el estado actual antes de navegar
                val savedOrderItems = _currentOrderState.value.orderItems
                val savedTotalAmount = _currentOrderState.value.totalAmount
                
                viewModelScope.launch {
                    _eventFlow.emit(UiEvent.NavigateToCheckout)
                }
            }
            
            is OrdersEvent.UpdateCheckoutField -> {
                when (event.field) {
                    is CheckoutField.CustomerName -> {
                        _checkoutState.value = _checkoutState.value.copy(
                            customerName = event.field.value
                        )
                    }
                    is CheckoutField.Date -> {
                        _checkoutState.value = _checkoutState.value.copy(
                            date = event.field.value
                        )
                    }
                    is CheckoutField.PaymentMethod -> {
                        _checkoutState.value = _checkoutState.value.copy(
                            paymentMethod = event.field.value
                        )
                        
                        if (event.field.value == PaymentMethod.CASH) {
                            _cashPaymentState.value = CashPaymentState(
                                totalAmount = _currentOrderState.value.totalAmount
                            )
                            viewModelScope.launch {
                                _eventFlow.emit(UiEvent.ShowCashPaymentDialog)
                            }
                        }
                    }
                }
            }
            
            is OrdersEvent.UpdateCashPayment -> {
                val amountPaid = event.amountPaid
                val totalAmount = _currentOrderState.value.totalAmount
                val change = amountPaid - totalAmount
                
                _cashPaymentState.value = CashPaymentState(
                    totalAmount = totalAmount,
                    amountPaid = amountPaid,
                    change = change
                )
            }
            
            OrdersEvent.CompleteSale -> {
                viewModelScope.launch {
                    if (_checkoutState.value.customerName.isBlank()) {
                        _eventFlow.emit(UiEvent.ShowSnackbar("Por favor ingresa el nombre del cliente"))
                        return@launch
                    }
                    if (_checkoutState.value.paymentMethod == null) {
                        _eventFlow.emit(UiEvent.ShowSnackbar("Selecciona un método de pago"))
                        return@launch
                    }
                    
                    if (_checkoutState.value.paymentMethod == PaymentMethod.CASH && 
                        _cashPaymentState.value.amountPaid < _currentOrderState.value.totalAmount) {
                        _eventFlow.emit(UiEvent.ShowSnackbar("El monto pagado debe ser igual o mayor al total"))
                        return@launch
                    }
                    
                    // Create order
                    val order = Order(
                        id = UUID.randomUUID().toString(),
                        items = _currentOrderState.value.orderItems,
                        totalAmount = _currentOrderState.value.totalAmount,
                        createdAt = Date(),
                        status = OrderStatus.COMPLETED
                    )
                    
                    // Get next sale number
                    val nextSaleNumberResult = getNextSaleNumberUseCase()
                    if (nextSaleNumberResult is Resource.Error) {
                        _eventFlow.emit(UiEvent.ShowSnackbar(nextSaleNumberResult.message ?: "Error al obtener el número de venta"))
                        return@launch
                    }
                    
                    val nextSaleNumber = (nextSaleNumberResult as Resource.Success).data ?: 1
                    
                    // Calculate profit (simplified - in a real app this would be more complex)
                    val profit = _currentOrderState.value.totalAmount * 0.3 // 30% profit margin
                    
                    // Create sale
                    val sale = Sale(
                        id = UUID.randomUUID().toString(),
                        saleNumber = nextSaleNumber,
                        orderId = order.id,
                        customerName = _checkoutState.value.customerName,
                        items = _currentOrderState.value.orderItems,
                        totalAmount = _currentOrderState.value.totalAmount,
                        profit = profit,
                        paymentMethod = _checkoutState.value.paymentMethod!!,
                        date = _checkoutState.value.date
                    )
                    
                    // Save order
                    val orderResult = addOrderUseCase(order)
                    if (orderResult is Resource.Error) {
                        _eventFlow.emit(UiEvent.ShowSnackbar(orderResult.message ?: "Error al guardar el pedido"))
                        return@launch
                    }
                    
                    // Save sale
                    val saleResult = addSaleUseCase(sale)
                    if (saleResult is Resource.Error) {
                        _eventFlow.emit(UiEvent.ShowSnackbar(saleResult.message ?: "Error al guardar la venta"))
                        return@launch
                    }
                    
                    // Reset states
                    _currentOrderState.value = CurrentOrderState()
                    _checkoutState.value = CheckoutState()
                    _cashPaymentState.value = CashPaymentState()
                    _selectedCategoryId.value = null
                    
                    _eventFlow.emit(UiEvent.ShowSnackbar("¡Venta realizada con éxito!"))
                    _eventFlow.emit(UiEvent.NavigateBack)
                }
            }
            
            OrdersEvent.CancelOrder -> {
                _currentOrderState.value = CurrentOrderState()
                _checkoutState.value = CheckoutState()
                _cashPaymentState.value = CashPaymentState()
                _selectedCategoryId.value = null
                
                viewModelScope.launch {
                    _eventFlow.emit(UiEvent.NavigateBack)
                }
            }
        }
    }

    // Classes to handle state
    data class CategoriesState(
        val categories: List<Category> = emptyList(),
        val isLoading: Boolean = false,
        val error: String? = null
    )

    data class ProductsState(
        val products: List<Product> = emptyList(),
        val isLoading: Boolean = false,
        val error: String? = null
    )

    data class OrdersState(
        val orders: List<Order> = emptyList(),
        val isLoading: Boolean = false,
        val error: String? = null
    )

    data class CurrentOrderState(
        val orderItems: List<OrderItem> = emptyList(),
        val totalAmount: Double = 0.0
    )

    data class CheckoutState(
        val customerName: String = "",
        val date: Date = Date(),
        val paymentMethod: PaymentMethod? = null
    )

    data class CashPaymentState(
        val totalAmount: Double = 0.0,
        val amountPaid: Double = 0.0,
        val change: Double = 0.0
    )

    // UI events
    sealed class UiEvent {
        data class ShowSnackbar(val message: String) : UiEvent()
        object NavigateToCheckout : UiEvent()
        object NavigateBack : UiEvent()
        object ShowCashPaymentDialog : UiEvent()
    }

    // Orders events
    sealed class OrdersEvent {
        data class SelectCategory(val categoryId: String?) : OrdersEvent()
        data class AddProductToOrder(val product: Product, val quantity: Double) : OrdersEvent()
        data class RemoveProductFromOrder(val orderItemId: String) : OrdersEvent()
        data class UpdateProductQuantity(val orderItemId: String, val quantityChange: Double) : OrdersEvent()
        object ConfirmOrder : OrdersEvent()
        data class UpdateCheckoutField(val field: CheckoutField) : OrdersEvent()
        data class UpdateCashPayment(val amountPaid: Double) : OrdersEvent()
        object CompleteSale : OrdersEvent()
        object CancelOrder : OrdersEvent()
    }

    // Checkout form fields
    sealed class CheckoutField {
        data class CustomerName(val value: String) : CheckoutField()
        data class Date(val value: java.util.Date) : CheckoutField()
        data class PaymentMethod(val value: com.icerojects.icemanagment.domain.model.order.PaymentMethod) : CheckoutField()
    }
}