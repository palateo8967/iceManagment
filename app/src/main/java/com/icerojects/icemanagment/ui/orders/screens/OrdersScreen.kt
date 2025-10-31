package com.icerojects.icemanagment.ui.orders.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.icerojects.icemanagment.domain.model.Category
import com.icerojects.icemanagment.domain.model.Product
import com.icerojects.icemanagment.ui.components.AppTopBar
import com.icerojects.icemanagment.ui.components.CategoryChip
import com.icerojects.icemanagment.ui.components.ProductCard
import com.icerojects.icemanagment.ui.orders.viewmodel.OrdersViewModel
import kotlinx.coroutines.flow.collectLatest
import com.icerojects.icemanagment.ui.navigation.AppScreens
import com.icerojects.icemanagment.ui.theme.PrimaryBlue
import com.icerojects.icemanagment.ui.theme.White

/**
 * Main screen for orders management
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrdersScreen(
    navController: NavController,
    viewModel: OrdersViewModel = hiltViewModel()
) {
    val categoriesState = viewModel.categoriesState.value
    val productsState = viewModel.productsState.value
    val selectedCategoryId = viewModel.selectedCategoryId.value
    val currentOrderState = viewModel.currentOrderState.value
    
    val snackbarHostState = remember { SnackbarHostState() }
    
    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                is OrdersViewModel.UiEvent.ShowSnackbar -> {
                    snackbarHostState.showSnackbar(event.message)
                }
                is OrdersViewModel.UiEvent.NavigateToCheckout -> {
                    navController.navigate(AppScreens.OrderSummaryScreen.route)
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
                title = "Pedidos",
                onBackClick = { navController.popBackStack() },
                onAddClick = { /* Will be implemented later */ }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            // Categories section
            Text(
                text = "Seleccionar categoría",
                style = MaterialTheme.typography.titleMedium,
                color = PrimaryBlue
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            if (categoriesState.isLoading) {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else if (categoriesState.categories.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No hay categorías disponibles",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            } else {
                CategoriesList(
                    categories = categoriesState.categories,
                    selectedCategoryId = selectedCategoryId,
                    onCategorySelected = { categoryId ->
                        viewModel.onEvent(OrdersViewModel.OrdersEvent.SelectCategory(categoryId))
                    }
                )
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Products list
            if (productsState.isLoading) {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else {
                ProductsList(
                    products = productsState.products,
                    onAddToOrder = { product, quantity ->
                        viewModel.onEvent(OrdersViewModel.OrdersEvent.AddProductToOrder(product, quantity))
                    }
                )
            }
            
            // Confirm order button
            if (currentOrderState.orderItems.isNotEmpty()) {
                Spacer(modifier = Modifier.height(16.dp))
                
                Button(
                    onClick = { viewModel.onEvent(OrdersViewModel.OrdersEvent.ConfirmOrder) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = PrimaryBlue,
                        contentColor = White
                    )
                ) {
                    Text(text = "Confirmar pedido")
                }
            }
        }
    }
}

/**
 * Component to display a horizontal list of categories
 */
@Composable
fun CategoriesList(
    categories: List<Category>,
    selectedCategoryId: String?,
    onCategorySelected: (String?) -> Unit
) {
    LazyRow(
        modifier = Modifier.fillMaxWidth()
    ) {
        // "All" category
        item {
            CategoryChip(
                name = "Todos",
                iconName = "fastfood",
                isSelected = selectedCategoryId == null,
                onClick = { onCategorySelected(null) }
            )
        }
        
        // Category items
        items(categories) { category ->
            val iconName = getCategoryIconName(category.name)
            CategoryChip(
                name = category.name,
                iconName = iconName,
                isSelected = category.id == selectedCategoryId,
                onClick = { onCategorySelected(category.id) }
            )
        }
    }
}

/**
 * Component to display a vertical list of products
 */
@Composable
fun ProductsList(
    products: List<Product>,
    onAddToOrder: (Product, Double) -> Unit
) {
    if (products.isEmpty()) {
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "No hay productos disponibles",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.error
            )
        }
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxWidth()
        ) {
            items(products) { product ->
                ProductCard(
                    product = product,
                    onAddToOrder = onAddToOrder
                )
                
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

/**
 * Helper function to get an icon name based on category name
 */
fun getCategoryIconName(categoryName: String): String {
    val lowerCaseName = categoryName.lowercase()
    
    return when {
        lowerCaseName.contains("helado") -> "icecream"
        lowerCaseName.contains("postre") -> "cake"
        lowerCaseName.contains("bebida") -> "local_drink"
        lowerCaseName.contains("extra") -> "add_circle"
        lowerCaseName.contains("combo") -> "fastfood"
        lowerCaseName.contains("promo") -> "star"
        else -> "fastfood" // Default icon
    }
}