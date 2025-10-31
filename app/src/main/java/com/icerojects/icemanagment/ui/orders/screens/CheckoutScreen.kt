package com.icerojects.icemanagment.ui.orders.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.icerojects.icemanagment.domain.model.order.PaymentMethod
import com.icerojects.icemanagment.ui.components.AppTopBar
import com.icerojects.icemanagment.ui.components.CashPaymentDialog
import com.icerojects.icemanagment.ui.components.PaymentMethodButton
import com.icerojects.icemanagment.ui.orders.viewmodel.OrdersViewModel
import com.icerojects.icemanagment.ui.theme.PrimaryBlue
import com.icerojects.icemanagment.ui.theme.White
import kotlinx.coroutines.flow.collectLatest
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Screen for order checkout and payment
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckoutScreen(
    navController: NavController,
    viewModel: OrdersViewModel = hiltViewModel()
) {
    val currentOrderState = viewModel.currentOrderState.value
    val checkoutState = viewModel.checkoutState.value
    val cashPaymentState = viewModel.cashPaymentState.value
    val snackbarHostState = remember { SnackbarHostState() }
    val currencyFormat = NumberFormat.getCurrencyInstance(Locale("es", "AR"))
    val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    
    var showCashPaymentDialog by remember { mutableStateOf(false) }
    
    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                is OrdersViewModel.UiEvent.ShowSnackbar -> {
                    snackbarHostState.showSnackbar(event.message)
                }
                is OrdersViewModel.UiEvent.NavigateToCheckout -> {
                    // Already in checkout
                }
                is OrdersViewModel.UiEvent.NavigateBack -> {
                    navController.popBackStack()
                }
                is OrdersViewModel.UiEvent.ShowCashPaymentDialog -> {
                    showCashPaymentDialog = true
                }
            }
        }
    }
    
    if (showCashPaymentDialog) {
        CashPaymentDialog(
            totalAmount = currentOrderState.totalAmount,
            onAmountPaidChange = { amount ->
                viewModel.onEvent(OrdersViewModel.OrdersEvent.UpdateCashPayment(amount))
            },
            onConfirm = {
                showCashPaymentDialog = false
                viewModel.onEvent(OrdersViewModel.OrdersEvent.CompleteSale)
            },
            onDismiss = {
                showCashPaymentDialog = false
            },
            amountPaid = cashPaymentState.amountPaid,
            change = cashPaymentState.change
        )
    }
    
    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            AppTopBar(
                title = "Confirmar Pedido",
                onBackClick = { navController.popBackStack() }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            // Date input
            OutlinedTextField(
                value = dateFormat.format(checkoutState.date),
                onValueChange = { /* Date is not editable directly */ },
                label = { Text("Fecha") },
                modifier = Modifier.fillMaxWidth(),
                readOnly = true
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Customer name input
            OutlinedTextField(
                value = checkoutState.customerName,
                onValueChange = { name ->
                    viewModel.onEvent(
                        OrdersViewModel.OrdersEvent.UpdateCheckoutField(
                            OrdersViewModel.CheckoutField.CustomerName(name)
                        )
                    )
                },
                label = { Text("Nombre del cliente") },
                modifier = Modifier.fillMaxWidth()
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Payment method selection
            Text(
                text = "Selección del método de pago",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                val paymentMethods = PaymentMethod.values()
                items(paymentMethods) { method ->
                    PaymentMethodButton(
                        paymentMethod = method,
                        isSelected = checkoutState.paymentMethod == method,
                        onClick = {
                            viewModel.onEvent(
                                OrdersViewModel.OrdersEvent.UpdateCheckoutField(
                                    OrdersViewModel.CheckoutField.PaymentMethod(method)
                                )
                            )
                        }
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Payment details
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                colors = CardDefaults.cardColors(
                    containerColor = White
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        text = "Detalle del pago",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    
                    Divider(modifier = Modifier.padding(vertical = 8.dp))
                    
                    // Items count
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Cantidad de productos",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        
                        Text(
                            text = currentOrderState.orderItems.size.toString(),
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    // Payment method
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Método de pago",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        
                        Text(
                            text = when (checkoutState.paymentMethod) {
                                PaymentMethod.CASH -> "Efectivo"
                                PaymentMethod.CARD -> "Tarjeta"
                                PaymentMethod.BANK_TRANSFER -> "Transferencia"
                                PaymentMethod.MERCADO_PAGO -> "Mercado Pago"
                                PaymentMethod.QR -> "QR"
                                PaymentMethod.OTHER -> "Otro"
                                null -> "Selecciona un metodo"
                            },
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    // Total
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Total",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        
                        Text(
                            text = currencyFormat.format(currentOrderState.totalAmount),
                            style = MaterialTheme.typography.titleLarge,
                            color = PrimaryBlue,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Create sale button
            Button(
                onClick = {
                    if (checkoutState.paymentMethod == PaymentMethod.CASH) {
                        viewModel.onEvent(
                            OrdersViewModel.OrdersEvent.UpdateCheckoutField(
                                OrdersViewModel.CheckoutField.PaymentMethod(PaymentMethod.CASH)
                            )
                        )
                    } else {
                        viewModel.onEvent(OrdersViewModel.OrdersEvent.CompleteSale)
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = checkoutState.customerName.isNotBlank()
            ,
                colors = ButtonDefaults.buttonColors(
                    containerColor = PrimaryBlue,
                    contentColor = White
                )
            ) {
                Text(text = "Crear venta")
            }
        }
    }
}