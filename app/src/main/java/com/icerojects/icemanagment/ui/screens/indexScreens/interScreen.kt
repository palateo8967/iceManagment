package com.icerojects.icemanagment.ui.screens.indexScreens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.icerojects.icemanagment.R
import com.icerojects.icemanagment.ui.theme.BlackOverlay

@Composable
fun Inter(navController: NavController) {

    Box(modifier = Modifier.fillMaxWidth()) {

        // Imagen de fondo
        Image(

            painter = painterResource(id = R.drawable.fondo1),
            contentDescription = "Fondo de pantalla",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()

        )

        // Capa oscura (usamos BlackOverlay desde theme)
        Box(

            modifier = Modifier
                .fillMaxSize()
                .background(BlackOverlay)

        )
    }

    // Contenido
    Column(

        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center

    ) {

        // Título
        Text(

            text = "Ice Managment",
            fontSize = 28.sp,
            color = MaterialTheme.colorScheme.secondary

        )

        Spacer(modifier = Modifier.height(16.dp))

        // Subtítulo
        Text(

            text = "Elige una opción para comenzar",
            fontSize = 15.sp,
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center

        )

        Spacer(modifier = Modifier.height(70.dp))

        // Botón de Iniciar sesión
        Button(

            onClick = { navController.navigate("login") },
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .height(48.dp),

            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.onPrimary,
                contentColor = MaterialTheme.colorScheme.primary
            ),

            shape = MaterialTheme.shapes.medium

        ) {

            Text(
                text = "Iniciar Sesión",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )

        }

        Spacer(modifier = Modifier.height(20.dp))

        // Botón de Crear cuenta
        Button(

            onClick = { navController.navigate("newAccount") },
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .height(48.dp),

            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.background.copy(alpha = 0f),
                contentColor = MaterialTheme.colorScheme.onPrimary
            ),

            border = BorderStroke(1.dp, MaterialTheme.colorScheme.onPrimary),

            shape = MaterialTheme.shapes.medium

        ) {

            Text(
                text = "Crear nueva cuenta",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )

        }
    }
}
