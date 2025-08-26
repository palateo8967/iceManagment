package com.icerojects.icemanagment.ui.screens.homeScreens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.icerojects.icemanagment.ui.components.MenuButton
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
        horizontalAlignment = Alignment.CenterHorizontally

    ) {

        Text(

            text = "Próximamente",
            style = MaterialTheme.typography.headlineLarge

        )

        Spacer(modifier = Modifier.height(32.dp))

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
                    backgroundColor = Color(0xFF4CAF50)

                ) {
                    // TODO: Navegar a pedidos
                }

                MenuButton(

                    text = "Stock",
                    icon = Icons.Default.Info,
                    backgroundColor = Color(0xFF3F51B5)

                ) {
                    // TODO: Navegar a stock
                }
            }

            Row(

                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly

            ) {

                MenuButton(

                    text = "Finanzas",
                    icon = Icons.Default.Add,
                    backgroundColor = Color(0xFFFF9800)

                ) {
                    // TODO: Navegar a finanzas
                }

                MenuButton(

                    text = "Cerrar Sesión",
                    icon = Icons.Default.ExitToApp,
                    backgroundColor = Color(0xFFF44336)

                ) {
                    authViewModel.signOut()
                }

            }

        }

    }

}
