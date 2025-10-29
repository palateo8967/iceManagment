package com.icerojects.icemanagment.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.icerojects.icemanagment.ui.auth.screens.LoginScreen
import com.icerojects.icemanagment.ui.auth.screens.NewAccountScreen
import com.icerojects.icemanagment.ui.home.screens.Home
import com.icerojects.icemanagment.ui.inventory.screens.InventoryScreen

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
    }
}
