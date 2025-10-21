package com.icerojects.icemanagment.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.icerojects.icemanagment.ui.navigation.AppNavigation
import com.icerojects.icemanagment.ui.theme.IceManagmentTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            IceManagmentTheme {
                AppNavigation()
            }
        }
    }
}





