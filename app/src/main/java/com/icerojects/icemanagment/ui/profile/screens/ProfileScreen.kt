package com.icerojects.icemanagment.ui.profile.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import com.icerojects.icemanagment.ui.auth.viewmodel.AuthViewModel
import com.icerojects.icemanagment.ui.navigation.AppScreens
import com.icerojects.icemanagment.ui.theme.PrimaryBlue
import com.icerojects.icemanagment.ui.theme.White
import androidx.compose.material3.ButtonDefaults
import kotlinx.coroutines.launch
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    navController: NavController,
    authViewModel: AuthViewModel = hiltViewModel()
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    
    // Estado para los campos editables
    var isEditing by remember { mutableStateOf(false) }
    var name by remember { mutableStateOf(TextFieldValue("")) }
    var email by remember { mutableStateOf(TextFieldValue("")) }
    var phone by remember { mutableStateOf(TextFieldValue("")) }
    var userLoading by remember { mutableStateOf(false) }

    // Estados de información de la heladería
    var shopName by remember { mutableStateOf(TextFieldValue("")) }
    var shopStreet by remember { mutableStateOf(TextFieldValue("")) }
    var shopStreetNumber by remember { mutableStateOf(TextFieldValue("")) }
    var shopType by remember { mutableStateOf(TextFieldValue("")) }
    var shopLoading by remember { mutableStateOf(false) }
    
    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text("Perfil de Usuario", color = White) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = PrimaryBlue,
                    titleContentColor = White,
                    navigationIconContentColor = White,
                    actionIconContentColor = White
                ),
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver",
                            tint = White
                        )
                    }
                },
                actions = {
                    if (isEditing) {
                        IconButton(onClick = {
                            isEditing = false
                            scope.launch {
                                snackbarHostState.showSnackbar("Cambios guardados correctamente")
                            }
                        }) {
                            Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Guardar cambios",
                        tint = White
                    )
                        }
                    } else {
                        IconButton(onClick = { isEditing = true }) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = "Editar perfil",
                                tint = White
                            )
                        }
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState()),
            contentAlignment = Alignment.TopCenter
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                // Avatar del usuario
                Icon(
                    imageVector = Icons.Default.AccountCircle,
                    contentDescription = null,
                    modifier = Modifier
                        .size(120.dp)
                        .clip(CircleShape),
                    tint = PrimaryBlue
                )
                
                Spacer(modifier = Modifier.height(24.dp))
                
                // Información del usuario
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                    colors = CardDefaults.cardColors(containerColor = White)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Text(
                            text = "Información Personal",
                            style = MaterialTheme.typography.titleLarge
                        )
                        if (userLoading) {
                            Box(
                                modifier = Modifier.fillMaxWidth(),
                                contentAlignment = Alignment.Center
                            ) {
                                androidx.compose.material3.CircularProgressIndicator(color = PrimaryBlue)
                            }
                        } else {
                            // Campo de nombre
                            OutlinedTextField(
                                value = name,
                                onValueChange = { if (isEditing) name = it },
                                label = { Text("Nombre") },
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Default.Person,
                                        contentDescription = null,
                                        tint = PrimaryBlue
                                    )
                                },
                                modifier = Modifier.fillMaxWidth(),
                                readOnly = !isEditing
                            )

                            // Campo de email
                            OutlinedTextField(
                                value = email,
                                onValueChange = { if (isEditing) email = it },
                                label = { Text("Email") },
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Default.Email,
                                        contentDescription = null,
                                        tint = PrimaryBlue
                                    )
                                },
                                modifier = Modifier.fillMaxWidth(),
                                readOnly = !isEditing
                            )

                            // Campo de teléfono
                            OutlinedTextField(
                                value = phone,
                                onValueChange = { if (isEditing) phone = it },
                                label = { Text("Teléfono") },
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Default.Phone,
                                        contentDescription = null,
                                        tint = PrimaryBlue
                                    )
                                },
                                modifier = Modifier.fillMaxWidth(),
                                readOnly = !isEditing
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Información de la Heladería
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                    colors = CardDefaults.cardColors(containerColor = White)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Text(
                            text = "Información de la Heladería",
                            style = MaterialTheme.typography.titleLarge
                        )

                        if (shopLoading) {
                            Box(
                                modifier = Modifier.fillMaxWidth(),
                                contentAlignment = Alignment.Center
                            ) {
                                androidx.compose.material3.CircularProgressIndicator(color = PrimaryBlue)
                            }
                        } else {
                            // Nombre
                            OutlinedTextField(
                                value = shopName,
                                onValueChange = { if (isEditing) shopName = it },
                                label = { Text("Nombre de la heladería") },
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Default.AccountCircle,
                                        contentDescription = null,
                                        tint = PrimaryBlue
                                    )
                                },
                                modifier = Modifier.fillMaxWidth(),
                                readOnly = !isEditing
                            )

                            // Calle
                            OutlinedTextField(
                                value = shopStreet,
                                onValueChange = { if (isEditing) shopStreet = it },
                                label = { Text("Calle") },
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Default.Person,
                                        contentDescription = null,
                                        tint = PrimaryBlue
                                    )
                                },
                                modifier = Modifier.fillMaxWidth(),
                                readOnly = !isEditing
                            )

                            // Número
                            OutlinedTextField(
                                value = shopStreetNumber,
                                onValueChange = { if (isEditing) shopStreetNumber = it },
                                label = { Text("Número") },
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Default.Person,
                                        contentDescription = null,
                                        tint = PrimaryBlue
                                    )
                                },
                                modifier = Modifier.fillMaxWidth(),
                                readOnly = !isEditing
                            )

                            // Tipo
                            OutlinedTextField(
                                value = shopType,
                                onValueChange = { if (isEditing) shopType = it },
                                label = { Text("Tipo de heladería") },
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Default.Person,
                                        contentDescription = null,
                                        tint = PrimaryBlue
                                    )
                                },
                                modifier = Modifier.fillMaxWidth(),
                                readOnly = !isEditing
                            )
                        }
                    }
                }
                
                // Botón de cerrar sesión
                Button(
                    onClick = {
                        authViewModel.signOut()
                        navController.navigate(AppScreens.LoginScreen.route) {
                            popUpTo(0) { inclusive = true }
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = PrimaryBlue,
                        contentColor = White
                    )
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                        contentDescription = null,
                        modifier = Modifier.padding(end = 8.dp)
                    )
                    Text("Cerrar Sesión")
                }
            }
        }
    }

    // Cargar información de la heladería desde Firestore
    LaunchedEffect(key1 = true) {
        try {
            shopLoading = true
            val uid = FirebaseAuth.getInstance().currentUser?.uid
            if (uid != null) {
                val snapshot = Firebase.firestore
                    .collection("shops")
                    .whereEqualTo("userId", uid)
                    .limit(1)
                    .get()
                    .await()

                val doc = snapshot.documents.firstOrNull()
                if (doc != null) {
                    shopName = TextFieldValue(doc.getString("name") ?: "")
                    shopStreet = TextFieldValue(doc.getString("street") ?: "")
                    shopStreetNumber = TextFieldValue(doc.getString("streetNumber") ?: "")
                    shopType = TextFieldValue(doc.getString("type") ?: "")
                }
            }
        } catch (e: Exception) {
            // podríamos mostrar un snackbar si falla
        } finally {
            shopLoading = false
        }
    }

    // Cargar información del usuario desde Firestore
    LaunchedEffect(key1 = "user_data") {
        try {
            userLoading = true
            val uid = FirebaseAuth.getInstance().currentUser?.uid
            if (uid != null) {
                val userDoc = Firebase.firestore
                    .collection("users")
                    .document(uid)
                    .get()
                    .await()

                if (userDoc.exists()) {
                    val firstName = userDoc.getString("firstName") ?: ""
                    val lastName = userDoc.getString("lastName") ?: ""
                    val displayName = listOf(firstName, lastName).filter { it.isNotBlank() }.joinToString(" ")
                    name = TextFieldValue(displayName)
                    email = TextFieldValue(userDoc.getString("email") ?: "")
                    phone = TextFieldValue(userDoc.getString("phone") ?: "")
                }
            }
        } catch (e: Exception) {
            scope.launch { snackbarHostState.showSnackbar("No se pudo cargar la información del usuario") }
        } finally {
            userLoading = false
        }
    }
}