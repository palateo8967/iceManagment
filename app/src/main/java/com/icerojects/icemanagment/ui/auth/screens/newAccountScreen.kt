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
    
    LaunchedEffect(uiState) {
        if (uiState is RegistrationUiState.Success) {
            navController.navigate("homeScreen") {
                popUpTo(0) { inclusive = true }
            }
        }
    }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        
        Surface(
            modifier = Modifier.size(80.dp),
            shape = MaterialTheme.shapes.extraLarge,
            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
        ) {
            Icon(
                imageVector = if (registrationState.currentStep == 1) Icons.Default.Person else Icons.Default.Lock,
                contentDescription = "Registration icon",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(40.dp)
            )
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Text(
            text = if (registrationState.currentStep == 1) "Personal Information" else "Ice Cream Shop Information",
            style = MaterialTheme.typography.headlineSmall
        )

        StepIndicator(
            currentStep = registrationState.currentStep,
            totalSteps = registrationState.totalSteps,
            modifier = Modifier.padding(vertical = 16.dp)
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        when (registrationState.currentStep) {
            1 -> PersonalInformationStep(viewModel)
            2 -> IceCreamShopInformationStep(viewModel)
        }
        
        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

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
        
        if (uiState is RegistrationUiState.Loading) {
            Spacer(modifier = Modifier.height(16.dp))
            CircularProgressIndicator()
        }
        
        Spacer(modifier = Modifier.height(16.dp))
    
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
