package com.icerojects.icemanagment.ui.features.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import com.icerojects.icemanagment.ui.navigation.AppNavigation

@Composable
fun PresentationScreen(navController: NavController){

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Bienvenido a IceManagement")
        Button(onClick = { navController.navigate("login") }) {
            Text("Comenzar")
        }
    }

}

@Composable
fun PresentationScree(){

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Bienvenido a IceManagement")
        Button(onClick = {}) {
            Text("Comenzar")
        }
    }

}

@Preview(showBackground = true)
@Composable
fun Preview(){

    PresentationScree()

}

