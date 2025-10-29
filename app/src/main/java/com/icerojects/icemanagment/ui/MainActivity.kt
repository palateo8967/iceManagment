package com.icerojects.icemanagment.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.icerojects.icemanagment.ui.auth.viewmodel.AuthViewModel
import com.icerojects.icemanagment.ui.navigation.AppNavigation
import com.icerojects.icemanagment.ui.navigation.AppScreens
import com.icerojects.icemanagment.ui.theme.IceManagmentTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            IceManagmentTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainScreen()
                }
            }
        }
    }
}

@Composable
fun MainScreen(authViewModel: AuthViewModel = hiltViewModel()) {
    val navController = rememberNavController()
    val isAuthenticated by authViewModel.authState.collectAsState()

    LaunchedEffect(isAuthenticated) {
        if (isAuthenticated) {
            navController.navigate(AppScreens.HomeScreen.route) {
                popUpTo(0) { inclusive = true }
                launchSingleTop = true
            }
        } else {
            if (navController.currentBackStackEntry?.destination?.route != AppScreens.LoginScreen.route &&
                navController.currentBackStackEntry?.destination?.route != AppScreens.NewAccountScreen.route) {
                navController.navigate(AppScreens.LoginScreen.route) {
                    popUpTo(0) { inclusive = true }
                    launchSingleTop = true
                }
            }
        }
    }

    AppNavigation(navController = navController)
}





