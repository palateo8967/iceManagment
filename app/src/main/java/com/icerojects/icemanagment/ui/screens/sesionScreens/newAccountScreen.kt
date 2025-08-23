package com.icerojects.icemanagment.ui.screens.sesionScreens

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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.icerojects.icemanagment.ui.screens.auth.AuthUiState
import com.icerojects.icemanagment.ui.screens.auth.AuthViewModel

@Composable
fun NewAccount(

    navController: NavHostController,
    authViewModel: AuthViewModel = hiltViewModel(),

) {

    val uiState by authViewModel.authUiState

    LaunchedEffect(uiState) {
        if (uiState is AuthUiState.Success) {

            navController.navigate("homeScreen")
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

        Text("Crear Nueva Cuenta", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(32.dp))

        OutlinedTextField(

            value = authViewModel.email.value,
            onValueChange = { authViewModel.email.value = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth(),
            isError = uiState is AuthUiState.Error

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

        // añadir un campo de "Confirmar Contraseña" aquí y la lógica en el ViewModel mas adelante, cuandos se entregue la beta

        Spacer(modifier = Modifier.height(16.dp))

        if (uiState is AuthUiState.Loading) {

            CircularProgressIndicator()

        } else {

            Button(

                onClick = { authViewModel.signUp() },
                modifier = Modifier.fillMaxWidth()

            ) {
                Text("Registrarse")
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

            authViewModel.resetFormAndUiState()
            navController.navigate("login") {
                popUpTo("newAccount") { inclusive = true }

            }

        }) {
            Text("¿Ya tienes cuenta? Inicia Sesión")
        }
    }
}