package com.icerojects.icemanagment.ui.auth.screens

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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.icerojects.icemanagment.ui.auth.viewmodel.AuthUiState
import com.icerojects.icemanagment.ui.auth.viewmodel.AuthViewModel
import com.icerojects.icemanagment.ui.components.AppLogo
import com.icerojects.icemanagment.ui.components.IconTextField
import com.icerojects.icemanagment.ui.navigation.AppScreens
import com.icerojects.icemanagment.ui.theme.PrimaryBlue
import com.icerojects.icemanagment.ui.theme.White
import androidx.compose.foundation.clickable
import androidx.compose.material3.ButtonDefaults

@Composable
fun LoginScreen(
    navController: NavController,
    authViewModel: AuthViewModel = hiltViewModel(),
) {
    val uiState = authViewModel.authUiState.value
    
    LaunchedEffect(uiState) {
        if (uiState is AuthUiState.Success) {
            navController.navigate(AppScreens.HomeScreen.route) {
                popUpTo(0) { inclusive = true }
            }
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
        AppLogo(showText = false)
        Spacer(modifier = Modifier.height(16.dp))

        Text("¡Bienvenido de Vuelta!", style = MaterialTheme.typography.headlineMedium)
        Text(
            "Completa los datos para continuar",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(24.dp))

        IconTextField(
            value = authViewModel.email.value,
            onValueChange = { authViewModel.email.value = it },
            label = "Correo electrónico",
            icon = Icons.Default.Email,
            isError = uiState is AuthUiState.Error,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
        )

        Spacer(modifier = Modifier.height(12.dp))

        IconTextField(
            value = authViewModel.password.value,
            onValueChange = { authViewModel.password.value = it },
            label = "Contraseña",
            icon = Icons.Default.Lock,
            isError = uiState is AuthUiState.Error,
            visualTransformation = PasswordVisualTransformation()
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            TextButton(onClick = { /* de momento no hace nada */ }) {
                Text("¿Olvidaste tu contraseña?", color = PrimaryBlue)
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        if (uiState is AuthUiState.Loading) {
            CircularProgressIndicator()
        } else {
            Button(
                onClick = { authViewModel.signIn() },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = PrimaryBlue,
                    contentColor = White
                )
            ) {
                Text("Iniciar Sesión")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("¿No tienes cuenta?", modifier = Modifier.alignByBaseline())
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Regístrate aquí",
                color = PrimaryBlue,
                modifier = Modifier
                    .alignByBaseline()
                    .clickable {
                        authViewModel.resetFormAndUiState()
                        navController.navigate(AppScreens.NewAccountScreen.route)
                    }
            )
        }

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
