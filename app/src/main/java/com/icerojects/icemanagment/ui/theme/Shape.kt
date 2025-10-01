package com.icerojects.icemanagment.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

/**
 * Formas personalizadas para la aplicación
 */
val Shapes = Shapes(
    // Formas pequeñas (botones pequeños, chips, etc.)
    small = RoundedCornerShape(4.dp),
    
    // Formas medianas (tarjetas, campos de texto, etc.)
    medium = RoundedCornerShape(8.dp),
    
    // Formas grandes (hojas inferiores, diálogos, etc.)
    large = RoundedCornerShape(12.dp),
    
    // Formas extra grandes (tarjetas destacadas, etc.)
    extraLarge = RoundedCornerShape(16.dp)
)