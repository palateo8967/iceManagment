package com.icerojects.icemanagment.ui.screens.homeScreens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.icerojects.icemanagment.ui.components.AppLogo
import com.icerojects.icemanagment.ui.screens.auth.AuthViewModel

/**
 * Pantalla principal de la aplicación
 * @param navController Controlador de navegación
 * @param authViewModel ViewModel de autenticación
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Home(
    navController: NavHostController,
    authViewModel: AuthViewModel = hiltViewModel()
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Panel de Control") },
                actions = {
                    IconButton(onClick = { /* TODO: Ir al perfil del usuario */ }) {
                        Icon(
                            imageVector = Icons.Filled.Person,
                            contentDescription = "Usuario"
                        )
                    }
                    IconButton(onClick = { authViewModel.signOut() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                            contentDescription = "Cerrar sesión"
                        )
                    }
                }
            )
        },
        bottomBar = {
            val items = listOf(
                NavItem("Pedidos", Icons.Filled.ShoppingCart),
                NavItem("Stock", Icons.Filled.Info),
                NavItem("Finanzas", Icons.Filled.Add)
            )
            val selectedIndexState = remember { mutableStateOf(0) }
            NavigationBar {
                items.forEachIndexed { index, item ->
                    NavigationBarItem(
                        selected = selectedIndexState.value == index,
                        onClick = {
                            selectedIndexState.value = index
                            // TODO: Navegar a la sección correspondiente (Pedidos/Stock/Finanzas)
                            // navController.navigate("ruta_${item.label.lowercase()}")
                        },
                        icon = {
                            Icon(imageVector = item.icon, contentDescription = item.label)
                        },
                        label = { Text(text = item.label) }
                    )
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 4.dp
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    AppLogo(size = 80)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Bienvenido",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary,
                        textAlign = TextAlign.Center
                    )
                }
            }
            // Contenido principal de la Home (placeholder)
            // Aquí puedes mostrar resúmenes, tarjetas o información relevante
        }
    }
}

data class NavItem(val label: String, val icon: androidx.compose.ui.graphics.vector.ImageVector)
