package com.icerojects.icemanagment.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel

import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.icerojects.icemanagment.ui.screens.IndexScreens.Inter
import com.icerojects.icemanagment.ui.screens.IndexScreens.Presentation
import com.icerojects.icemanagment.ui.screens.auth.AuthViewModel
import com.icerojects.icemanagment.ui.screens.homeScreens.Home
import com.icerojects.icemanagment.ui.screens.sesionScreens.Login
import com.icerojects.icemanagment.ui.screens.sesionScreens.NewAccount

object AppScreen{

    const val PRESENTATION = "presentation"
   const val INTER = "inter"
    const val LOGIN = "login"
    const val NEW_ACCOUNT = "newAccount"
    const val HOMESCREEN = "homeScreen"
}

@Composable
fun AppNavigation(

    authViewModel: AuthViewModel = hiltViewModel()

){

    val navController =  rememberNavController()
    val currentUser by authViewModel.authState.collectAsState()
    val startDestination = if (currentUser != null) AppScreen.HOMESCREEN else AppScreen.PRESENTATION

    LaunchedEffect(currentUser) {

        if(currentUser != null){

            if (navController.currentBackStackEntry?.destination?.route != AppScreen.HOMESCREEN){

                navController.navigate(AppScreen.HOMESCREEN){

                    popUpTo(navController.graph.startDestinationId) { inclusive = true }
                    launchSingleTop = true

                }

            }

        } else {

            val currentRoute = navController.currentBackStackEntry?.destination?.route
            if(currentRoute == AppScreen.HOMESCREEN){

                navController.navigate(AppScreen.LOGIN){

                    popUpTo(AppScreen.HOMESCREEN) { inclusive = true }
                    launchSingleTop = true

                }

                authViewModel.resetFormAndUiState()

            }

        }

    }

    NavHost(

        navController = navController,
        startDestination = "presentation"

    ){
        composable(AppScreen.PRESENTATION){

            Presentation(navController)
        }

        composable(AppScreen.INTER){
            Inter(navController)
        }

        composable(AppScreen.LOGIN){
            Login(navController)
        }

        composable(AppScreen.NEW_ACCOUNT){
            NewAccount(navController)
        }

        composable(AppScreen.HOMESCREEN){
            Home(navController)
        }
    }

}