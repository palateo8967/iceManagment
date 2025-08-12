package com.icerojects.icemanagment.ui.features.presentation.IndexScreens

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.icerojects.icemanagment.R
import com.icerojects.icemanagment.core.components.Customtext

@Composable
fun Inter(navController: NavController){

   Box(modifier = Modifier.fillMaxWidth()){
       //Parte de la imagen de fondo
       Image(
           painter = painterResource(id = R.drawable.fondo1),
           contentDescription = "Fondo de pantalla",
           contentScale = ContentScale.Crop,
           modifier = Modifier.fillMaxSize()
       )

       Box(
           modifier = Modifier
               .fillMaxSize()
               .background(Color.Black.copy(alpha = 0.4f))
       )

   }
    //Parte de arriba
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ){

        //Titulo
        Customtext(
            text = "Ice Managment",
            fontSize = 28.sp,
            color = Color(0xFFBFDBFE)
        )

        Spacer(modifier = Modifier.height(16.dp))

        //Parte de abajo
        Text(
            text = "Elige una opcion para comenzar",
            fontSize = 15.sp,
            color = Color.White,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(70.dp))

        Button(
            onClick = { navController.navigate("login") },

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
                text = "Iniciar Sesion",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = { navController.navigate("newAccount") },

            modifier = Modifier
                .fillMaxWidth(0.8f)
                .height(48.dp),

            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent,
                contentColor = Color.White
            ),

            border = BorderStroke(1.dp, Color.White),
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