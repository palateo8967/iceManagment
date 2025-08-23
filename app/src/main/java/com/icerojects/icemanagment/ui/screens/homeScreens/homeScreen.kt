package com.icerojects.icemanagment.ui.screens.homeScreens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.icerojects.icemanagment.ui.screens.auth.AuthViewModel

@Composable
fun Home(

    navController: NavHostController,
    authViewModel: AuthViewModel = hiltViewModel()

) {
    Column(

        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally

    ) {

        Text(

            text = "Próximamente",
            style = MaterialTheme.typography.headlineLarge

        )

        Spacer(modifier = Modifier.height(32.dp))

        Button(onClick = {

            authViewModel.signOut()

        }) {
            Text("Cerrar sesión")
        }

    }
}
