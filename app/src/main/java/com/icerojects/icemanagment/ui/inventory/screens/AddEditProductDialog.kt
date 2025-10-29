package com.icerojects.icemanagment.ui.inventory.screens

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
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.icerojects.icemanagment.domain.model.UnitOfMeasure
import com.icerojects.icemanagment.ui.inventory.viewmodel.InventoryViewModel

/**
 * Dialog for adding or editing products
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditProductDialog(
    viewModel: InventoryViewModel,
    onDismiss: () -> Unit
) {
    val productFormState = viewModel.productFormState.value
    val categoriesState = viewModel.categoriesState.value
    
    var expandedCategoryDropdown by remember { mutableStateOf(false) }
    var expandedUnitDropdown by remember { mutableStateOf(false) }
    
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
                    text = if (isEditing) "Edit Product" else "Add Product",
                    style = MaterialTheme.typography.headlineSmall
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Name
                OutlinedTextField(
                    value = productFormState.name,
                    onValueChange = { viewModel.onEvent(InventoryViewModel.InventoryEvent.UpdateProductField(InventoryViewModel.ProductField.Name(it))) },
                    label = { Text("Name") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                // Category
                ExposedDropdownMenuBox(
                    expanded = expandedCategoryDropdown,
                    onExpandedChange = { expandedCategoryDropdown = it }
                ) {
                    OutlinedTextField(
                        value = categoriesState.categories.find { it.id == productFormState.categoryId }?.name ?: "",
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Category") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedCategoryDropdown) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor()
                    )
                    
                    ExposedDropdownMenu(
                        expanded = expandedCategoryDropdown,
                        onDismissRequest = { expandedCategoryDropdown = false }
                    ) {
                        categoriesState.categories.forEach { category ->
                            DropdownMenuItem(
                                text = { Text(category.name) },
                                onClick = {
                                    viewModel.onEvent(InventoryViewModel.InventoryEvent.UpdateProductField(InventoryViewModel.ProductField.CategoryId(category.id)))
                                    expandedCategoryDropdown = false
                                }
                            )
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(8.dp))
                
                // Quantity and Unit
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    OutlinedTextField(
                        value = if (productFormState.quantity == 0.0) "" else productFormState.quantity.toString(),
                        onValueChange = { 
                            val quantity = it.toDoubleOrNull() ?: 0.0
                            viewModel.onEvent(InventoryViewModel.InventoryEvent.UpdateProductField(InventoryViewModel.ProductField.Quantity(quantity)))
                        },
                        label = { Text("Quantity") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        modifier = Modifier.weight(1f)
                    )
                    
                    Spacer(modifier = Modifier.width(8.dp))
                    
                    ExposedDropdownMenuBox(
                        expanded = expandedUnitDropdown,
                        onExpandedChange = { expandedUnitDropdown = it },
                        modifier = Modifier.weight(1f)
                    ) {
                        OutlinedTextField(
                            value = productFormState.unit.name,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Unit") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedUnitDropdown) },
                            modifier = Modifier.menuAnchor()
                        )
                        
                        ExposedDropdownMenu(
                            expanded = expandedUnitDropdown,
                            onDismissRequest = { expandedUnitDropdown = false }
                        ) {
                            UnitOfMeasure.values().forEach { unit ->
                                DropdownMenuItem(
                                    text = { Text(unit.name) },
                                    onClick = {
                                        viewModel.onEvent(InventoryViewModel.InventoryEvent.UpdateProductField(InventoryViewModel.ProductField.Unit(unit)))
                                        expandedUnitDropdown = false
                                    }
                                )
                            }
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(8.dp))
                
                // Minimum stock
                OutlinedTextField(
                    value = if (productFormState.minStock == 0.0) "" else productFormState.minStock.toString(),
                    onValueChange = { 
                        val minStock = it.toDoubleOrNull() ?: 0.0
                        viewModel.onEvent(InventoryViewModel.InventoryEvent.UpdateProductField(InventoryViewModel.ProductField.MinStock(minStock)))
                    },
                    label = { Text("Minimum stock") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    modifier = Modifier.fillMaxWidth()
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                // Price
                OutlinedTextField(
                    value = if (productFormState.price == 0.0) "" else productFormState.price.toString(),
                    onValueChange = { 
                        val price = it.toDoubleOrNull() ?: 0.0
                        viewModel.onEvent(InventoryViewModel.InventoryEvent.UpdateProductField(InventoryViewModel.ProductField.Price(price)))
                    },
                    label = { Text("Price") },
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
                            viewModel.onEvent(InventoryViewModel.InventoryEvent.ResetProductForm)
                            onDismiss()
                        }
                    ) {
                        Text("Cancel")
                    }
                    
                    Spacer(modifier = Modifier.width(8.dp))
                    
                    Button(
                        onClick = {
                            viewModel.onEvent(InventoryViewModel.InventoryEvent.SaveProduct)
                        }
                    ) {
                        Text("Save")
                    }
                }
            }
        }
    }
}