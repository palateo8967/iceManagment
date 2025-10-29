package com.icerojects.icemanagment.ui.stock.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.icerojects.icemanagment.ui.stock.viewmodel.StockViewModel

@Composable
fun AddCategoryDialog(
    viewModel: StockViewModel,
    onDismiss: () -> Unit
) {
    val categoriaFormState = viewModel.categoriaFormState.value
    
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = MaterialTheme.shapes.medium
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .fillMaxWidth()
            ) {
                Text(
                    text = "Agregar Categoría",
                    style = MaterialTheme.typography.headlineSmall
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                OutlinedTextField(
                    value = categoriaFormState.nombre,
                    onValueChange = { viewModel.onEvent(StockViewModel.StockEvent.UpdateCategoriaField(StockViewModel.CategoriaField.Nombre(it))) },
                    label = { Text("Nombre de la categoría") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                
                Spacer(modifier = Modifier.height(24.dp))
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(
                        onClick = {
                            viewModel.onEvent(StockViewModel.StockEvent.ResetCategoriaForm)
                            onDismiss()
                        }
                    ) {
                        Text("Cancelar")
                    }
                    
                    Spacer(modifier = Modifier.width(8.dp))
                    
                    Button(
                        onClick = {
                            viewModel.onEvent(StockViewModel.StockEvent.AddCategory)
                        }
                    ) {
                        Text("Guardar")
                    }
                }
            }
        }
    }
}