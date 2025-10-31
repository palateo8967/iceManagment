package com.icerojects.icemanagment.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.icerojects.icemanagment.domain.model.Product
import java.text.NumberFormat
import java.util.Locale

/**
 * Component to display a product as a card with quantity controls
 */
@Composable
fun ProductCard(
    product: Product,
    onAddToOrder: (Product, Double) -> Unit
) {
    var quantity by remember { mutableStateOf(0.0) }
    val currencyFormat = NumberFormat.getCurrencyInstance(Locale("es", "AR"))
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(
            containerColor = com.icerojects.icemanagment.ui.theme.White
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Product name and price
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = product.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                
                Text(
                    text = currencyFormat.format(product.price),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // Category and stock
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Categoría: ${product.categoryName}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                
                Text(
                    text = "Disponible: ${product.quantity} ${product.unit.name.lowercase()}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = if (product.quantity <= product.minStock) 
                        MaterialTheme.colorScheme.error 
                    else 
                        MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            Divider()
            Spacer(modifier = Modifier.height(8.dp))
            
            // Quantity controls
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Minus button
                IconButton(
                    onClick = { 
                        if (quantity > 0) {
                            quantity -= 1
                        }
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Decrease quantity"
                    )
                }
                
                // Quantity display
                Text(
                    text = quantity.toInt().toString(),
                    style = MaterialTheme.typography.titleMedium
                )
                
                // Plus button
                IconButton(
                    onClick = { 
                        if (quantity < product.quantity) {
                            quantity += 1
                        }
                    },
                    enabled = quantity < product.quantity
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Increase quantity"
                    )
                }
                
                Spacer(modifier = Modifier.width(16.dp))
                
                // Add to order button
                androidx.compose.material3.Button(
                    onClick = { 
                        if (quantity > 0) {
                            onAddToOrder(product, quantity)
                            quantity = 0.0
                        }
                    },
                    enabled = quantity > 0 && quantity <= product.quantity,
                    modifier = Modifier.fillMaxWidth(0.5f)
                ) {
                    Text(text = "Agregar al pedido")
                }
            }
        }
    }
}