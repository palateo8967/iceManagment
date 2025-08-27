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
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.icerojects.icemanagment.ui.components.MenuButton
import com.icerojects.icemanagment.ui.screens.auth.AuthViewModel
import com.icerojects.icemanagment.ui.theme.BlueStock
import com.icerojects.icemanagment.ui.theme.GreenPedido
import com.icerojects.icemanagment.ui.theme.OrangeFinanzas
import com.icerojects.icemanagment.ui.theme.RedCerrarSesion

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
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.onBackground

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
                    backgroundColor = GreenPedido

                ) {
                    // TODO: Navegar a pedidos
                }

                MenuButton(

                    text = "Stock",
                    icon = Icons.Default.Info,
                    backgroundColor = BlueStock

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
                    backgroundColor = OrangeFinanzas

                ) {
                    // TODO: Navegar a finanzas
                }

                MenuButton(

                    text = "Cerrar Sesión",
                    icon = Icons.Default.ExitToApp,
                    backgroundColor = RedCerrarSesion

                ) {
                    authViewModel.signOut()
                }

            }
        }
    }
}
