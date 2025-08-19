package com.icerojects.icemanagment.ui.screens.IndexScreens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.ui.res.painterResource
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.icerojects.icemanagment.R
import com.icerojects.icemanagment.core.components.Customtext

//Pantalla de presentacion
@Composable
fun Presentation(navController: NavController){

    Box(modifier = Modifier.fillMaxSize()){
        //Imagen de fondo
        Image(
            painter = painterResource(id = R.drawable.fondo1),
            contentDescription = "Fondo de pantalla",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        // Capa de oscurecimiento
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.4f))
        )


    }
    //Seccion Principal
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        //Titulo
        Customtext(
            text = "Bienvenido a",
            fontSize = 20.sp,
            color = Color.White
        )
        //Sub Titulo
        Customtext(
            text = "Ice Managment",
            fontSize = 28.sp,
            color = Color(0xFFBFDBFE)
        )

        Spacer(modifier = Modifier.height(16.dp))

        //Parte de abajo
        Text(
            text = "La solución completa para administrar tu heladería de manera profesional y eficiente",
            fontSize = 15.sp,
            color = Color.White,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(24.dp))

        //Parte de Beneficios
        Column(
            horizontalAlignment  = Alignment.Start
        ){

            Text(
                text = "• Control de inventario en tiempo real",
                fontSize = 15.sp,
                color = Color.White
            )
            Text(
                text = "• Gestión de ventas y reportes",
                fontSize = 15.sp,
                color = Color.White
            )
            Text(
                text = "• Administración desde cualquier lugar",
                fontSize = 15.sp,
                color = Color.White
            )

        }

        Spacer(modifier = Modifier.height(16.dp))

        //Juan(boton) para continuar a la siguiente seccion
        Button(
            onClick = { navController.navigate("inter") },

            modifier = Modifier
                .fillMaxWidth(0.8f)
                .height(48.dp),

            colors = ButtonDefaults.buttonColors(
                containerColor = Color.White,
                contentColor = Color(0xFF2563EB)
            ),

            shape = MaterialTheme.shapes.medium

        ) {
            Text(
                text = "Continuar",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
        }
        
        Spacer(modifier = Modifier.height(16.dp))

        //Parte de cosas legales :v
        Text(
            text = "Al continuar, aceptas nuestros términos de servicio y política de privacidad",
            fontSize = 9.sp,
            color = Color.White
        )
        
    }

}
