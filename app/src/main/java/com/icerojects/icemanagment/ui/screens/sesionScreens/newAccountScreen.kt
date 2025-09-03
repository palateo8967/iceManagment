package com.icerojects.icemanagment.ui.screens.sesionScreens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.core.content.contentValuesOf
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.icerojects.icemanagment.ui.screens.auth.AuthUiState
import com.icerojects.icemanagment.ui.screens.auth.AuthViewModel
import com.icerojects.icemanagment.ui.components.IconTextField

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
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // 🔹 Logo redondo con ícono
        Surface(
            modifier = Modifier.size(80.dp),
            shape = MaterialTheme.shapes.extraLarge,
            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
        ) {
            Icon(
                imageVector = Icons.Default.Lock,
                contentDescription = "Logo registro",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(40.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 🔹 Títulos
        Text("Información Personal", style = MaterialTheme.typography.headlineSmall)
        Text(
            "Paso 1 de 2",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(24.dp))

        // 🔹 Email
        IconTextField(
            value = authViewModel.email.value,
            onValueChange = { authViewModel.email.value = it },
            label = "Correo Electrónico",
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

        Spacer(modifier = Modifier.height(20.dp))

        // 🔹 Botón Continuar
        if (uiState is AuthUiState.Loading) {
            CircularProgressIndicator()
        } else {
            Button(
                onClick = { authViewModel.signUp() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Continuar")
                Spacer(modifier = Modifier.width(8.dp))
                Icon(
                    imageVector = Icons.Default.ArrowForward,
                    contentDescription = "Continuar"
                )
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

        Spacer(modifier = Modifier.height(16.dp))

        // 🔹 Link volver al login
        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("¿Ya tienes cuenta?")
            Spacer(modifier = Modifier.width(4.dp))
            TextButton(onClick = {
                authViewModel.resetFormAndUiState()
                navController.navigate("login") {
                    popUpTo("newAccount") { inclusive = true }
                }
            }) {
                Text("Inicia Sesión")
            }
        }
    }
}
