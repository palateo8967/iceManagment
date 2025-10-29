package com.icerojects.icemanagment.ui.stock.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.icerojects.icemanagment.domain.model.UnidadMedida
import com.icerojects.icemanagment.ui.stock.viewmodel.StockViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditProductDialog(
    viewModel: StockViewModel,
    onDismiss: () -> Unit
) {
    val productFormState = viewModel.productFormState.value
    val categoriasState = viewModel.categoriasState.value
    
    var expandedCategoriaDropdown by remember { mutableStateOf(false) }
    var expandedUnidadDropdown by remember { mutableStateOf(false) }
    
    val isEditing = productFormState.id.isNotBlank()
    
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
                    text = if (isEditing) "Editar Producto" else "Agregar Producto",
                    style = MaterialTheme.typography.headlineSmall
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Nombre
                OutlinedTextField(
                    value = productFormState.nombre,
                    onValueChange = { viewModel.onEvent(StockViewModel.StockEvent.UpdateProductField(StockViewModel.ProductField.Nombre(it))) },
                    label = { Text("Nombre") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                // Categoría
                ExposedDropdownMenuBox(
                    expanded = expandedCategoriaDropdown,
                    onExpandedChange = { expandedCategoriaDropdown = it }
                ) {
                    OutlinedTextField(
                        value = categoriasState.categorias.find { it.id == productFormState.categoriaId }?.nombre ?: "",
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Categoría") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedCategoriaDropdown) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor()
                    )
                    
                    ExposedDropdownMenu(
                        expanded = expandedCategoriaDropdown,
                        onDismissRequest = { expandedCategoriaDropdown = false }
                    ) {
                        categoriasState.categorias.forEach { categoria ->
                            DropdownMenuItem(
                                text = { Text(categoria.nombre) },
                                onClick = {
                                    viewModel.onEvent(StockViewModel.StockEvent.UpdateProductField(StockViewModel.ProductField.CategoriaId(categoria.id)))
                                    expandedCategoriaDropdown = false
                                }
                            )
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(8.dp))
                
                // Cantidad y Unidad
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    OutlinedTextField(
                        value = if (productFormState.cantidad == 0.0) "" else productFormState.cantidad.toString(),
                        onValueChange = { 
                            val cantidad = it.toDoubleOrNull() ?: 0.0
                            viewModel.onEvent(StockViewModel.StockEvent.UpdateProductField(StockViewModel.ProductField.Cantidad(cantidad)))
                        },
                        label = { Text("Cantidad") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        modifier = Modifier.weight(1f)
                    )
                    
                    Spacer(modifier = Modifier.width(8.dp))
                    
                    ExposedDropdownMenuBox(
                        expanded = expandedUnidadDropdown,
                        onExpandedChange = { expandedUnidadDropdown = it },
                        modifier = Modifier.weight(1f)
                    ) {
                        OutlinedTextField(
                            value = productFormState.unidad.name,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Unidad") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedUnidadDropdown) },
                            modifier = Modifier.menuAnchor()
                        )
                        
                        ExposedDropdownMenu(
                            expanded = expandedUnidadDropdown,
                            onDismissRequest = { expandedUnidadDropdown = false }
                        ) {
                            UnidadMedida.values().forEach { unidad ->
                                DropdownMenuItem(
                                    text = { Text(unidad.name) },
                                    onClick = {
                                        viewModel.onEvent(StockViewModel.StockEvent.UpdateProductField(StockViewModel.ProductField.Unidad(unidad)))
                                        expandedUnidadDropdown = false
                                    }
                                )
                            }
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(8.dp))
                
                // Stock mínimo
                OutlinedTextField(
                    value = if (productFormState.stockMinimo == 0.0) "" else productFormState.stockMinimo.toString(),
                    onValueChange = { 
                        val stockMinimo = it.toDoubleOrNull() ?: 0.0
                        viewModel.onEvent(StockViewModel.StockEvent.UpdateProductField(StockViewModel.ProductField.StockMinimo(stockMinimo)))
                    },
                    label = { Text("Stock mínimo") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    modifier = Modifier.fillMaxWidth()
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                // Precio
                OutlinedTextField(
                    value = if (productFormState.precio == 0.0) "" else productFormState.precio.toString(),
                    onValueChange = { 
                        val precio = it.toDoubleOrNull() ?: 0.0
                        viewModel.onEvent(StockViewModel.StockEvent.UpdateProductField(StockViewModel.ProductField.Precio(precio)))
                    },
                    label = { Text("Precio") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    modifier = Modifier.fillMaxWidth()
                )
                
                Spacer(modifier = Modifier.height(24.dp))
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(
                        onClick = {
                            viewModel.onEvent(StockViewModel.StockEvent.ResetProductForm)
                            onDismiss()
                        }
                    ) {
                        Text("Cancelar")
                    }
                    
                    Spacer(modifier = Modifier.width(8.dp))
                    
                    Button(
                        onClick = {
                            viewModel.onEvent(StockViewModel.StockEvent.SaveProduct)
                        }
                    ) {
                        Text("Guardar")
                    }
                }
            }
        }
    }
}