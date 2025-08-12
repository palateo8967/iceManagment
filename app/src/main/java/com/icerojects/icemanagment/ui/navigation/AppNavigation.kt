package com.icerojects.icemanagment.ui.navigation

import androidx.compose.runtime.Composable

import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.icerojects.icemanagment.ui.features.presentation.IndexScreens.Inter
import com.icerojects.icemanagment.ui.features.presentation.IndexScreens.Presentation
import com.icerojects.icemanagment.ui.features.presentation.sesionScreen.Login
import com.icerojects.icemanagment.ui.features.presentation.sesionScreen.NewAccount

@Composable
fun AppNavigation(){

    val navController =  rememberNavController()

    NavHost(

        navController = navController,
        startDestination = "presentation"

    ){
        composable("presentation"){
            Presentation(navController)
        }

        composable("inter"){
            Inter(navController)
        }

        composable("login"){
            Login(navController)
        }

        composable("newAccount"){
            NewAccount(navController)
        }
    }

}