package com.icerojects.icemanagment.ui.screens.homeScreens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.icerojects.icemanagment.ui.components.AppLogo
import com.icerojects.icemanagment.ui.components.MenuButton
import com.icerojects.icemanagment.ui.screens.auth.AuthViewModel
import com.icerojects.icemanagment.ui.theme.BlueStock
import com.icerojects.icemanagment.ui.theme.GreenPedido
import com.icerojects.icemanagment.ui.theme.OrangeFinanzas
import com.icerojects.icemanagment.ui.theme.RedCerrarSesion

/**
 * Pantalla principal de la aplicación
 * @param navController Controlador de navegación
 * @param authViewModel ViewModel de autenticación
 */
@Composable
fun Home(
    navController: NavHostController,
    authViewModel: AuthViewModel = hiltViewModel()
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Cabecera
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
                    text = "Panel de Control",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    textAlign = TextAlign.Center
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // GRID de botones
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                MenuButton(
                    text = "Nuevo Pedido",
                    icon = Icons.Default.ShoppingCart,
                    backgroundColor = GreenPedido,
                    onClick = {
                        // TODO: Navegar a pedidos
                    }
                )

                MenuButton(
                    text = "Stock",
                    icon = Icons.Default.Info,
                    backgroundColor = BlueStock,
                    onClick = {
                        // TODO: Navegar a stock
                    }
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                MenuButton(
                    text = "Finanzas",
                    icon = Icons.Default.Add,
                    backgroundColor = OrangeFinanzas,
                    onClick = {
                        // TODO: Navegar a finanzas
                    }
                )

                MenuButton(
                    text = "Cerrar Sesión",
                    icon = Icons.AutoMirrored.Filled.ExitToApp,
                    backgroundColor = RedCerrarSesion,
                    onClick = {
                        authViewModel.signOut()
                    }
                )
            }
        }
    }
}
