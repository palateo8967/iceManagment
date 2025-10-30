package com.icerojects.icemanagment.ui.navigation

sealed class AppScreens(val route: String) {
    object LoginScreen : AppScreens("login_screen")
    object NewAccountScreen : AppScreens("new_account_screen")
    object HomeScreen : AppScreens("home_screen")
    object InventoryScreen : AppScreens("inventory_screen")
    object OrdersScreen : AppScreens("orders_screen")
    object OrderSummaryScreen : AppScreens("order_summary_screen")
    object CheckoutScreen : AppScreens("checkout_screen")
}