package com.icerojects.icemanagment.ui.components

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.w3c.dom.Text

@Composable
fun Customtext(

    text: String,
    fontSize: TextUnit = 16.sp,
    color: Color = Color.Black,
    fontFamily: FontFamily = FontFamily.Default

    ){

    Text(

        text = text,
        style = TextStyle(

            fontSize = fontSize,
            color = color,
            fontFamily = fontFamily

        )

    )
}