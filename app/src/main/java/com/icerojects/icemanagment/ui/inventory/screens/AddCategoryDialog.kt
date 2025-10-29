package com.icerojects.icemanagment.ui.inventory.screens

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
import com.icerojects.icemanagment.ui.inventory.viewmodel.InventoryViewModel

/**
 * Dialog for adding categories
 */
@Composable
fun AddCategoryDialog(
    viewModel: InventoryViewModel,
    onDismiss: () -> Unit
) {
    val categoryFormState = viewModel.categoryFormState.value
    
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
                    text = "Add Category",
                    style = MaterialTheme.typography.headlineSmall
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                OutlinedTextField(
                    value = categoryFormState.name,
                    onValueChange = { viewModel.onEvent(InventoryViewModel.InventoryEvent.UpdateCategoryField(InventoryViewModel.CategoryField.Name(it))) },
                    label = { Text("Category name") },
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
                            viewModel.onEvent(InventoryViewModel.InventoryEvent.ResetCategoryForm)
                            onDismiss()
                        }
                    ) {
                        Text("Cancel")
                    }
                    
                    Spacer(modifier = Modifier.width(8.dp))
                    
                    Button(
                        onClick = {
                            viewModel.onEvent(InventoryViewModel.InventoryEvent.AddCategory)
                        }
                    ) {
                        Text("Save")
                    }
                }
            }
        }
    }
}