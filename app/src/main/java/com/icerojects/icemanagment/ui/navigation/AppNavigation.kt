package com.icerojects.icemanagment.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.icerojects.icemanagment.ui.auth.screens.LoginScreen
import com.icerojects.icemanagment.ui.auth.screens.NewAccountScreen
import com.icerojects.icemanagment.ui.finance.screens.FinanceScreen
import com.icerojects.icemanagment.ui.finance.screens.OrderDetailScreen
import com.icerojects.icemanagment.ui.home.screens.Home
import com.icerojects.icemanagment.ui.inventory.screens.InventoryScreen
import com.icerojects.icemanagment.ui.orders.screens.CheckoutScreen
import com.icerojects.icemanagment.ui.orders.screens.OrderSummaryScreen
import com.icerojects.icemanagment.ui.orders.screens.OrdersScreen
import com.icerojects.icemanagment.ui.profile.screens.ProfileScreen

@Composable
fun AppNavigation(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = AppScreens.LoginScreen.route
    ) {
        composable(route = AppScreens.LoginScreen.route) {
            LoginScreen(navController)
        }
        
        composable(route = AppScreens.NewAccountScreen.route) {
            NewAccountScreen(navController)
        }
        
        composable(route = AppScreens.HomeScreen.route) {
            Home(navController)
        }
        
        composable(route = AppScreens.InventoryScreen.route) {
            InventoryScreen(navController)
        }
        
        composable(route = AppScreens.OrdersScreen.route) {
            OrdersScreen(navController)
        }
        
        composable(route = AppScreens.OrderSummaryScreen.route) {
            OrderSummaryScreen(navController)
        }
        
        composable(route = AppScreens.CheckoutScreen.route) {
            CheckoutScreen(navController)
        }
        
        composable(route = AppScreens.ProfileScreen.route) {
            ProfileScreen(navController)
        }
        
        composable(route = AppScreens.FinanceScreen.route) {
            FinanceScreen(navController)
        }
        
        composable(
            route = "order_detail_screen/{orderId}",
            arguments = listOf(navArgument("orderId") { type = NavType.StringType })
        ) { backStackEntry ->
            val orderId = backStackEntry.arguments?.getString("orderId") ?: ""
            OrderDetailScreen(navController, orderId)
        }
    }
}
