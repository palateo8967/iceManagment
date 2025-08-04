package com.icerojects.icemanagment.ui.navigation

import androidx.compose.runtime.Composable
import com.icerojects.icemanagment.ui.features.presentation.PresentationScreen

import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun AppNavigation(){

    val navController =  rememberNavController()

    NavHost(

        navController = navController,
        startDestination = "presentation"

    ){
        composable("presentation"){
            PresentationScreen(navController)
        }
    }

}