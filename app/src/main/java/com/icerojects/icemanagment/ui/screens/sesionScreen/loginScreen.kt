package com.icerojects.icemanagment.ui.screens.sesionScreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.error
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.icerojects.icemanagment.ui.screens.auth.AuthUiState
import com.icerojects.icemanagment.ui.screens.auth.AuthViewModel

@Composable
fun Login(

    navController: NavHostController,
    authViewModel: AuthViewModel = hiltViewModel(),


    ) {

    val uiState by authViewModel.authUiState

    LaunchedEffect(uiState){

        if(uiState is AuthUiState.Success){

            navController.navigate("homeScreen")
            authViewModel.resetFormAndUiState()

        }

    }

    DisposableEffect(Unit){

        onDispose {

            authViewModel.resetFormAndUiState()

        }

    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Iniciar Sesión", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(32.dp))

        OutlinedTextField(
            value = authViewModel.email.value,
            onValueChange = { authViewModel.email.value = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth(),
            isError = uiState is AuthUiState.Error // Podrías querer marcar error en campos específicos
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = authViewModel.password.value,
            onValueChange = { authViewModel.password.value = it },
            label = { Text("Contraseña") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
            isError = uiState is AuthUiState.Error
        )
        Spacer(modifier = Modifier.height(16.dp))

        if (uiState is AuthUiState.Loading) {
            CircularProgressIndicator()
        } else {
            Button(
                onClick = { authViewModel.signIn() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Entrar")
            }
        }

        if (uiState is AuthUiState.Error) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = (uiState as AuthUiState.Error).message,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall
            )
        }

        Spacer(modifier = Modifier.height(16.dp))
        TextButton(onClick = {
            authViewModel.resetFormAndUiState() // Limpia estado antes de ir a registro
            navController.navigate("newAccount")
        }) {
            Text("¿No tienes cuenta? Regístrate aquí")
        }
    }

}