package com.icerojects.icemanagment.ui.orders.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.icerojects.icemanagment.ui.components.AppTopBar
import com.icerojects.icemanagment.ui.components.OrderSummaryItem
import com.icerojects.icemanagment.ui.orders.viewmodel.OrdersViewModel
import kotlinx.coroutines.flow.collectLatest
import java.text.NumberFormat
import java.util.Locale
import com.icerojects.icemanagment.ui.navigation.AppScreens

/**
 * Screen to display the order summary
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderSummaryScreen(
    navController: NavController,
    viewModel: OrdersViewModel = hiltViewModel()
) {
    val currentOrderState = viewModel.currentOrderState.value
    val snackbarHostState = remember { SnackbarHostState() }
    val currencyFormat = NumberFormat.getCurrencyInstance(Locale("es", "AR"))
    
    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                is OrdersViewModel.UiEvent.ShowSnackbar -> {
                    snackbarHostState.showSnackbar(event.message)
                }
                is OrdersViewModel.UiEvent.NavigateToCheckout -> {
                    navController.navigate(AppScreens.CheckoutScreen.route)
                }
                is OrdersViewModel.UiEvent.NavigateBack -> {
                    navController.popBackStack()
                }
                is OrdersViewModel.UiEvent.ShowCashPaymentDialog -> {
                    // Will be implemented later
                }
            }
        }
    }
    
    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            AppTopBar(
                title = "Resumen del Pedido",
                onBackClick = { navController.popBackStack() }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            // Order items
            if (currentOrderState.orderItems.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No hay productos en el pedido",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            } else {
                Text(
                    text = "Resumen del Pedido",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) {
                    items(currentOrderState.orderItems) { orderItem ->
                        OrderSummaryItem(
                            orderItem = orderItem,
                            onRemove = { itemId ->
                                viewModel.onEvent(OrdersViewModel.OrdersEvent.RemoveProductFromOrder(itemId))
                            }
                        )
                    }
                }
            }
            
            // Total section
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        text = "Total",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    
                    Divider(modifier = Modifier.padding(vertical = 8.dp))
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Total a pagar",
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Bold
                        )
                        
                        Text(
                            text = currencyFormat.format(currentOrderState.totalAmount),
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(
                    onClick = { viewModel.onEvent(OrdersViewModel.OrdersEvent.CancelOrder) },
                    modifier = Modifier.weight(1f)
                ) {
                    Text(text = "Cancelar")
                }
                
                Spacer(modifier = Modifier.padding(horizontal = 8.dp))
                
                Button(
                    onClick = { viewModel.onEvent(OrdersViewModel.OrdersEvent.ConfirmOrder) },
                    modifier = Modifier.weight(1f),
                    enabled = currentOrderState.orderItems.isNotEmpty()
                ) {
                    Text(text = "Confirmar")
                }
            }
        }
    }
}