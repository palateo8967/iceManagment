package com.icerojects.icemanagment.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun MenuButton(

    text: String,
    icon: ImageVector,
    backgroundColor: Color = MaterialTheme.colorScheme.primary,
    contentColor: Color = MaterialTheme.colorScheme.onPrimary,
    onClick: () -> Unit

) {

    Box(

        modifier = Modifier
            .size(150.dp)
            .background(backgroundColor, shape = MaterialTheme.shapes.medium)
            .clickable { onClick() }
            .padding(16.dp),
        contentAlignment = Alignment.Center

    ) {

        Column(

            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center

        ) {

            Icon(

                imageVector = icon,
                contentDescription = text,
                tint = contentColor,
                modifier = Modifier.size(48.dp)

            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(

                text = text,
                color = contentColor,
                fontSize = 16.sp,
                style = MaterialTheme.typography.bodyMedium

            )
        }
    }
}
