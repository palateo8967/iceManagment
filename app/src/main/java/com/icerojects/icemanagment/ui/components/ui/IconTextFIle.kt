package com.icerojects.icemanagment.ui.components

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Icon
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.Color
import com.icerojects.icemanagment.ui.theme.PrimaryBlue
import com.icerojects.icemanagment.ui.theme.White

/**
 * Campo de texto con icono que utiliza el tema de la aplicación
 * @param value Valor actual del campo
 * @param onValueChange Callback cuando cambia el valor
 * @param label Etiqueta del campo
 * @param icon Icono que se muestra al inicio del campo
 * @param modifier Modificador opcional
 * @param isError Indica si hay un error en el campo
 * @param visualTransformation Transformación visual (ej. para contraseñas)
 * @param keyboardOptions Opciones del teclado
 */
@Composable
fun IconTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    icon: ImageVector,
    modifier: Modifier = Modifier,
    isError: Boolean = false,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        leadingIcon = { 
            Icon(
                imageVector = icon, 
                contentDescription = null,
                tint = PrimaryBlue
            ) 
        },
        modifier = modifier.fillMaxWidth(),
        isError = isError,
        visualTransformation = visualTransformation,
        keyboardOptions = keyboardOptions,
        singleLine = true,
        colors = TextFieldDefaults.colors(
            focusedContainerColor = White,
            unfocusedContainerColor = White,
            disabledContainerColor = White,
            errorContainerColor = White,
            // Mantener borde negro tanto en estado normal como en error
            focusedIndicatorColor = Color.Black,
            unfocusedIndicatorColor = Color.Black,
            errorIndicatorColor = Color.Black,
            focusedLabelColor = PrimaryBlue,
            unfocusedLabelColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
            errorLabelColor = MaterialTheme.colorScheme.error,
            cursorColor = PrimaryBlue
        )
    )
}
