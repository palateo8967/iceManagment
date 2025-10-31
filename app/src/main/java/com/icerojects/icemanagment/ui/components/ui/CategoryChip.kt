package com.icerojects.icemanagment.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.icerojects.icemanagment.ui.theme.PrimaryBlue
import com.icerojects.icemanagment.ui.theme.White

/**
 * Component to display a category as a chip with an icon
 */
@Composable
fun CategoryChip(
    name: String,
    iconName: String = "",
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .clickable(onClick = onClick),
        color = if (isSelected) PrimaryBlue else White
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val icon = getCategoryIcon(iconName)
            Icon(
                imageVector = icon,
                contentDescription = name,
                modifier = Modifier.size(20.dp),
                tint = if (isSelected) White else PrimaryBlue
            )
            
            Text(
                text = name,
                style = MaterialTheme.typography.bodyMedium,
                color = if (isSelected) White else PrimaryBlue,
                modifier = Modifier.padding(start = 8.dp)
            )
        }
    }
}

/**
 * Get the appropriate icon for a category based on its icon name
 */
@Composable
fun getCategoryIcon(iconName: String): ImageVector {
    return when (iconName.lowercase()) {
        "icecream" -> Icons.Default.ShoppingCart
        "cake" -> Icons.Default.List
        "local_drink" -> Icons.Default.AccountCircle
        "add_circle" -> Icons.Default.Add
        "fastfood" -> Icons.Default.ShoppingCart
        "star" -> Icons.Default.Star
        else -> Icons.Default.ShoppingCart // Default icon
    }
}