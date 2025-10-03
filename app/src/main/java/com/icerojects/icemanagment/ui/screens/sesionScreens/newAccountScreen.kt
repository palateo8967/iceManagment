package com.icerojects.icemanagment.ui.screens.sesionScreens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.icerojects.icemanagment.ui.components.IconTextField
import com.icerojects.icemanagment.ui.components.StepIndicator
import com.icerojects.icemanagment.ui.screens.auth.RegisterViewModel
import com.icerojects.icemanagment.ui.screens.auth.RegistrationUiState

@Composable
fun NewAccount(
    navController: NavHostController,
    viewModel: RegisterViewModel = hiltViewModel()
) {
    val registrationState = viewModel.registrationState.value
    val uiState = viewModel.uiState.value
    
    // Observe UI state changes
    LaunchedEffect(uiState) {
        if (uiState is RegistrationUiState.Success) {
            navController.navigate("homeScreen") {
                popUpTo(0) { inclusive = true }
            }
        }
    }
    
    // Main container
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Header with icon and title
        Surface(
            modifier = Modifier.size(80.dp),
            shape = MaterialTheme.shapes.extraLarge,
            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
        ) {
            Icon(
                imageVector = if (registrationState.currentStep == 1) Icons.Default.Person else Icons.Default.Store,
                contentDescription = "Registration icon",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(40.dp)
            )
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Title based on current step
        Text(
            text = if (registrationState.currentStep == 1) "Personal Information" else "Ice Cream Shop Information",
            style = MaterialTheme.typography.headlineSmall
        )
        
        // Step indicator
        StepIndicator(
            currentStep = registrationState.currentStep,
            totalSteps = registrationState.totalSteps,
            modifier = Modifier.padding(vertical = 16.dp)
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        // Content based on current step
        when (registrationState.currentStep) {
            1 -> PersonalInformationStep(viewModel)
            2 -> IceCreamShopInformationStep(viewModel)
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Navigation buttons
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Back button (only visible in step 2)
            if (registrationState.currentStep > 1) {
                OutlinedButton(
                    onClick = { viewModel.goToPreviousStep() },
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back"
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Back")
                }
                Spacer(modifier = Modifier.width(16.dp))
            }
            
            // Continue/Create Account button
            Button(
                onClick = {
                    if (registrationState.currentStep == 1) {
                        if (viewModel.isStep1Valid()) {
                            viewModel.goToNextStep()
                        }
                    } else {
                        if (viewModel.isStep2Valid()) {
                            viewModel.registerUser()
                        }
                    }
                },
                modifier = Modifier.weight(1f)
            ) {
                Text(if (registrationState.currentStep < registrationState.totalSteps) "Continue" else "Create My Account")
                if (registrationState.currentStep < registrationState.totalSteps) {
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = "Continue"
                    )
                }
            }
        }
        
        // Show general error
        if (uiState is RegistrationUiState.Error) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = uiState.message,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
        
        // Show loading
        if (uiState is RegistrationUiState.Loading) {
            Spacer(modifier = Modifier.height(16.dp))
            CircularProgressIndicator()
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Link to login
        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Already have an account?")
            Spacer(modifier = Modifier.width(4.dp))
            TextButton(onClick = {
                viewModel.resetState()
                navController.navigate("login") {
                    popUpTo("newAccount") { inclusive = true }
                }
            }) {
                Text("Sign In")
            }
        }
    }
}
