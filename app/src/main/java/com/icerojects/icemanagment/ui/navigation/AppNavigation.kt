package com.icerojects.icemanagment.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.icerojects.icemanagment.ui.screens.indexScreens.Inter
import com.icerojects.icemanagment.ui.screens.indexScreens.Presentation
import com.icerojects.icemanagment.ui.screens.auth.AuthViewModel
import com.icerojects.icemanagment.ui.screens.homeScreens.Home
import com.icerojects.icemanagment.ui.screens.sesionScreens.Login
import com.icerojects.icemanagment.ui.screens.sesionScreens.NewAccount

sealed class AppScreen(val route: String) {

    object Presentation : AppScreen("presentation")
    object Inter : AppScreen("inter")
    object Login : AppScreen("login")
    object NewAccount : AppScreen("newAccount")
    object Home : AppScreen("homeScreen")

}

@Composable
fun AppNavigation(

    authViewModel: AuthViewModel = hiltViewModel()

) {

    val navController = rememberNavController()
    val currentUser by authViewModel.authState.collectAsState()

    val startDestination = if (currentUser != null) AppScreen.Home.route else AppScreen.Presentation.route

    LaunchedEffect(currentUser) {

        if (currentUser != null) {

            navController.navigate(AppScreen.Home.route) {

                popUpTo(0) { inclusive = true }
                launchSingleTop = true

            }

        } else {

            if (navController.currentBackStackEntry?.destination?.route == AppScreen.Home.route) {

                navController.navigate(AppScreen.Login.route) {
                    popUpTo(0) { inclusive = true }
                    launchSingleTop = true
                }
                authViewModel.resetFormAndUiState()

            }
        }
    }

    NavHost(

        navController = navController,
        startDestination = startDestination

    ) {

        composable(AppScreen.Presentation.route) {
            Presentation(navController)
        }

        composable(AppScreen.Inter.route) {
            Inter(navController)
        }

        composable(AppScreen.Login.route) {
            Login(navController)
        }

        composable(AppScreen.NewAccount.route) {
            NewAccount(navController)
        }

        composable(AppScreen.Home.route) {
            Home(navController)
        }

    }
}
