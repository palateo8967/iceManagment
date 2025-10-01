package com.icerojects.icemanagment.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.icerojects.icemanagment.R

/**
 * Logo de la aplicación con texto opcional
 * @param modifier Modificador opcional
 * @param showText Indica si se debe mostrar el texto "Ice Management" debajo del logo
 * @param size Tamaño del logo en dp
 */
@Composable
fun AppLogo(
    modifier: Modifier = Modifier,
    showText: Boolean = true,
    size: Int = 100
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_launcher_foreground),
            contentDescription = "Logo IceManagement",
            modifier = Modifier.size(size.dp)
        )
        
        if (showText) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Ice Management",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                fontSize = (size / 4).sp
            )
        }
    }
}
