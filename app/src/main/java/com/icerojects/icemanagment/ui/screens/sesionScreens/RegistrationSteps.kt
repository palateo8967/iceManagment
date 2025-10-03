package com.icerojects.icemanagment.ui.screens.sesionScreens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.icerojects.icemanagment.ui.components.IconTextField
import com.icerojects.icemanagment.ui.screens.auth.RegisterViewModel

@Composable
fun PersonalInformationStep(viewModel: RegisterViewModel) {
    val registrationState = viewModel.registrationState.value
    
    // Row for First Name and Last Name
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // First Name
        OutlinedTextField(
            value = registrationState.firstName,
            onValueChange = { viewModel.updateFirstName(it) },
            label = { Text("First Name") },
            leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
            modifier = Modifier.weight(1f),
            isError = viewModel.firstNameError.value != null,
            singleLine = true
        )
        
        // Last Name
        OutlinedTextField(
            value = registrationState.lastName,
            onValueChange = { viewModel.updateLastName(it) },
            label = { Text("Last Name") },
            leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
            modifier = Modifier.weight(1f),
            isError = viewModel.lastNameError.value != null,
            singleLine = true
        )
    }
    
    // Error messages for First Name and Last Name
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        viewModel.firstNameError.value?.let {
            Text(
                text = it,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.weight(1f)
            )
        } ?: Spacer(modifier = Modifier.weight(1f))
        
        viewModel.lastNameError.value?.let {
            Text(
                text = it,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.weight(1f)
            )
        } ?: Spacer(modifier = Modifier.weight(1f))
    }
    
    Spacer(modifier = Modifier.height(8.dp))
    
    // Email
    IconTextField(
        value = registrationState.email,
        onValueChange = { viewModel.updateEmail(it) },
        label = "Email",
        icon = Icons.Default.Email,
        isError = viewModel.emailError.value != null,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
    )
    
    viewModel.emailError.value?.let {
        Text(
            text = it,
            color = MaterialTheme.colorScheme.error,
            style = MaterialTheme.typography.bodySmall
        )
    }
    
    Spacer(modifier = Modifier.height(8.dp))
    
    // Phone
    IconTextField(
        value = registrationState.phone,
        onValueChange = { viewModel.updatePhone(it) },
        label = "Phone",
        icon = Icons.Default.Phone,
        isError = viewModel.phoneError.value != null,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
    )
    
    viewModel.phoneError.value?.let {
        Text(
            text = it,
            color = MaterialTheme.colorScheme.error,
            style = MaterialTheme.typography.bodySmall
        )
    }
    
    Spacer(modifier = Modifier.height(8.dp))
    
    // Password
    IconTextField(
        value = registrationState.password,
        onValueChange = { viewModel.updatePassword(it) },
        label = "Password",
        icon = Icons.Default.Lock,
        isError = viewModel.passwordError.value != null,
        visualTransformation = PasswordVisualTransformation()
    )
    
    viewModel.passwordError.value?.let {
        Text(
            text = it,
            color = MaterialTheme.colorScheme.error,
            style = MaterialTheme.typography.bodySmall
        )
    }
    
    Spacer(modifier = Modifier.height(8.dp))
    
    // Confirm password
    IconTextField(
        value = registrationState.confirmPassword,
        onValueChange = { viewModel.updateConfirmPassword(it) },
        label = "Confirm Password",
        icon = Icons.Default.Lock,
        isError = viewModel.confirmPasswordError.value != null,
        visualTransformation = PasswordVisualTransformation()
    )
    
    viewModel.confirmPasswordError.value?.let {
        Text(
            text = it,
            color = MaterialTheme.colorScheme.error,
            style = MaterialTheme.typography.bodySmall
        )
    }
}

@Composable
fun IceCreamShopInformationStep(viewModel: RegisterViewModel) {
    val registrationState = viewModel.registrationState.value
    
    // Shop name
    IconTextField(
        value = registrationState.shopName,
        onValueChange = { viewModel.updateShopName(it) },
        label = "Ice Cream Shop Name",
        icon = Icons.Default.Store,
        isError = viewModel.shopNameError.value != null
    )
    
    viewModel.shopNameError.value?.let {
        Text(
            text = it,
            color = MaterialTheme.colorScheme.error,
            style = MaterialTheme.typography.bodySmall
        )
    }
    
    Spacer(modifier = Modifier.height(8.dp))
    
    // Address: Street and Number
    Text(
        text = "Address",
        style = MaterialTheme.typography.bodyMedium,
        modifier = Modifier.align(Alignment.Start)
    )
    
    Spacer(modifier = Modifier.height(4.dp))
    
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Street
        OutlinedTextField(
            value = registrationState.street,
            onValueChange = { viewModel.updateStreet(it) },
            label = { Text("Street") },
            leadingIcon = { Icon(Icons.Default.LocationOn, contentDescription = null) },
            modifier = Modifier.weight(2f),
            isError = viewModel.streetError.value != null,
            singleLine = true
        )
        
        // Street Number
        OutlinedTextField(
            value = registrationState.streetNumber,
            onValueChange = { viewModel.updateStreetNumber(it) },
            label = { Text("Number") },
            modifier = Modifier.weight(1f),
            isError = viewModel.streetNumberError.value != null,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            singleLine = true
        )
    }
    
    // Error messages for Street and Street Number
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        viewModel.streetError.value?.let {
            Text(
                text = it,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.weight(2f)
            )
        } ?: Spacer(modifier = Modifier.weight(2f))
        
        viewModel.streetNumberError.value?.let {
            Text(
                text = it,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.weight(1f)
            )
        } ?: Spacer(modifier = Modifier.weight(1f))
    }
    
    Spacer(modifier = Modifier.height(16.dp))
    
    // Shop type
    Text(
        text = "Shop Type",
        style = MaterialTheme.typography.bodyMedium,
        modifier = Modifier.align(Alignment.Start)
    )
    
    Spacer(modifier = Modifier.height(4.dp))
    
    var expanded by remember { mutableStateOf(false) }
    
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = Modifier.fillMaxWidth()
    ) {
        OutlinedTextField(
            value = registrationState.shopType,
            onValueChange = { },
            readOnly = true,
            trailingIcon = { Icon(Icons.Default.ArrowDropDown, null) },
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth(),
            label = { Text("Select type") },
            isError = viewModel.shopTypeError.value != null
        )
        
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            listOf("Artisanal", "Industrial", "Mixed").forEach { type ->
                DropdownMenuItem(
                    text = { Text(type) },
                    onClick = { 
                        viewModel.updateShopType(type)
                        expanded = false
                    }
                )
            }
        }
    }
    
    viewModel.shopTypeError.value?.let {
        Text(
            text = it,
            color = MaterialTheme.colorScheme.error,
            style = MaterialTheme.typography.bodySmall
        )
    }
    
    Spacer(modifier = Modifier.height(16.dp))
    
    // Terms and conditions
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Checkbox(
            checked = registrationState.acceptTerms,
            onCheckedChange = { viewModel.updateAcceptTerms(it) }
        )
        
        Text(
            text = "I accept the terms and conditions and privacy policy",
            style = MaterialTheme.typography.bodySmall
        )
    }
    
    viewModel.termsError.value?.let {
        Text(
            text = it,
            color = MaterialTheme.colorScheme.error,
            style = MaterialTheme.typography.bodySmall
        )
    }
}