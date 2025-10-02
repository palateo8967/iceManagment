package com.icerojects.icemanagment.ui.screens.sesionScreens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.icerojects.icemanagment.ui.screens.auth.AuthUiState
import com.icerojects.icemanagment.ui.screens.auth.AuthViewModel
import com.icerojects.icemanagment.ui.components.AppLogo
import com.icerojects.icemanagment.ui.components.IconTextField

@Composable
fun Login(
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

    DisposableEffect(Unit) {
        onDispose { authViewModel.resetFormAndUiState() }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // 🔹 Logo
        AppLogo()
        Spacer(modifier = Modifier.height(16.dp))

        // 🔹 Bienvenida
        Text("¡Bienvenido de Vuelta!", style = MaterialTheme.typography.headlineMedium)
        Text(
            "Completa los datos para continuar",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(24.dp))

        // 🔹 Email
        IconTextField(
            value = authViewModel.email.value,
            onValueChange = { authViewModel.email.value = it },
            label = "Correo electrónico",
            icon = Icons.Default.Email,
            isError = uiState is AuthUiState.Error,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
        )

        Spacer(modifier = Modifier.height(12.dp))

        // 🔹 Contraseña
        IconTextField(
            value = authViewModel.password.value,
            onValueChange = { authViewModel.password.value = it },
            label = "Contraseña",
            icon = Icons.Default.Lock,
            isError = uiState is AuthUiState.Error,
            visualTransformation = PasswordVisualTransformation()
        )

        Spacer(modifier = Modifier.height(8.dp))

        // 🔹 ¿Olvidaste tu contraseña?
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            TextButton(onClick = { /* de momento no hace nada */ }) {
                Text("¿Olvidaste tu contraseña?")
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // 🔹 Botón principal
        if (uiState is AuthUiState.Loading) {
            CircularProgressIndicator()
        } else {
            Button(
                onClick = { authViewModel.signIn() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Iniciar Sesión")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 🔹 Link de registro
        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("¿No tienes cuenta?")
            Spacer(modifier = Modifier.width(4.dp))
            TextButton(
                onClick = {
                    authViewModel.resetFormAndUiState()
                    navController.navigate("newAccount")
                }
            ) {
                Text("Regístrate aquí")
            }
        }

        // 🔹 Mensaje de error
        if (uiState is AuthUiState.Error) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = (uiState as AuthUiState.Error).message,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}
