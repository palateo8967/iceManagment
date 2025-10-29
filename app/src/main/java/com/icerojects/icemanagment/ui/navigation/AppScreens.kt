package com.icerojects.icemanagment.ui.navigation

sealed class AppScreens(val route: String) {
    object LoginScreen : AppScreens("login_screen")
    object NewAccountScreen : AppScreens("new_account_screen")
    object HomeScreen : AppScreens("home_screen")
    object StockScreen : AppScreens("stock_screen")
}