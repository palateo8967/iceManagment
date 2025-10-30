package com.icerojects.icemanagment.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import java.text.NumberFormat
import java.util.Locale

/**
 * Dialog for cash payment with change calculation
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CashPaymentDialog(
    totalAmount: Double,
    onAmountPaidChange: (Double) -> Unit,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    amountPaid: Double = 0.0,
    change: Double = 0.0
) {
    val currencyFormat = NumberFormat.getCurrencyInstance(Locale("es", "AR"))
    var amountPaidText by remember { mutableStateOf(if (amountPaid > 0) amountPaid.toString() else "") }
    
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = MaterialTheme.shapes.large
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp)
            ) {
                Text(
                    text = "Calcula el cambio de tu venta",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Total amount
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Text(
                            text = "Total a pagar",
                            style = MaterialTheme.typography.bodyLarge
                        )
                        
                        Text(
                            text = currencyFormat.format(totalAmount),
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Amount paid input
                OutlinedTextField(
                    value = amountPaidText,
                    onValueChange = { text ->
                        amountPaidText = text
                        text.toDoubleOrNull()?.let { amount ->
                            onAmountPaidChange(amount)
                        }
                    },
                    label = { Text("Monto recibido") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Change calculation
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = if (change >= 0) MaterialTheme.colorScheme.secondaryContainer else MaterialTheme.colorScheme.errorContainer
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Text(
                            text = if (change >= 0) "Cambio a devolver" else "Falta por pagar",
                            style = MaterialTheme.typography.bodyLarge
                        )
                        
                        Text(
                            text = currencyFormat.format(if (change >= 0) change else -change),
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = if (change >= 0) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.error
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(24.dp))
                
                Divider()
                
                Spacer(modifier = Modifier.height(24.dp))
                
                // Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    Button(
                        onClick = onDismiss,
                        modifier = Modifier.padding(end = 8.dp)
                    ) {
                        Text("Cancelar")
                    }
                    
                    Button(
                        onClick = onConfirm,
                        enabled = amountPaid >= totalAmount
                    ) {
                        Text("Continuar")
                    }
                }
            }
        }
    }
}