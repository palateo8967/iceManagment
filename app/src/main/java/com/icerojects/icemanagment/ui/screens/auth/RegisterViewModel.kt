package com.icerojects.icemanagment.ui.screens.auth

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.icerojects.icemanagment.data.remote.auth.FirebaseAuthManager
import com.icerojects.icemanagment.domain.model.AuthOperationResult
import com.icerojects.icemanagment.domain.model.IceCreamShop
import com.icerojects.icemanagment.domain.model.RegistrationState
import com.icerojects.icemanagment.domain.model.UserData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

sealed class RegistrationUiState {
    object Idle : RegistrationUiState()
    object Loading : RegistrationUiState()
    data class Success(val user: FirebaseUser?) : RegistrationUiState()
    data class Error(val message: String) : RegistrationUiState()
}

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val authManager: FirebaseAuthManager
) : ViewModel() {

    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    private val _registrationState = mutableStateOf(RegistrationState())
    val registrationState: State<RegistrationState> = _registrationState

    private val _uiState = mutableStateOf<RegistrationUiState>(RegistrationUiState.Idle)
    val uiState: State<RegistrationUiState> = _uiState

    // Validations
    private val _emailError = mutableStateOf<String?>(null)
    val emailError: State<String?> = _emailError

    private val _passwordError = mutableStateOf<String?>(null)
    val passwordError: State<String?> = _passwordError

    private val _confirmPasswordError = mutableStateOf<String?>(null)
    val confirmPasswordError: State<String?> = _confirmPasswordError

    private val _firstNameError = mutableStateOf<String?>(null)
    val firstNameError: State<String?> = _firstNameError

    private val _lastNameError = mutableStateOf<String?>(null)
    val lastNameError: State<String?> = _lastNameError

    private val _phoneError = mutableStateOf<String?>(null)
    val phoneError: State<String?> = _phoneError

    private val _shopNameError = mutableStateOf<String?>(null)
    val shopNameError: State<String?> = _shopNameError

    private val _streetError = mutableStateOf<String?>(null)
    val streetError: State<String?> = _streetError

    private val _streetNumberError = mutableStateOf<String?>(null)
    val streetNumberError: State<String?> = _streetNumberError

    private val _shopTypeError = mutableStateOf<String?>(null)
    val shopTypeError: State<String?> = _shopTypeError

    private val _termsError = mutableStateOf<String?>(null)
    val termsError: State<String?> = _termsError

    // Functions to update state
    fun updateEmail(email: String) {
        _registrationState.value = _registrationState.value.copy(email = email)
        validateEmail()
    }

    fun updatePassword(password: String) {
        _registrationState.value = _registrationState.value.copy(password = password)
        validatePassword()
    }

    fun updateConfirmPassword(confirmPassword: String) {
        _registrationState.value = _registrationState.value.copy(confirmPassword = confirmPassword)
        validateConfirmPassword()
    }

    fun updateFirstName(firstName: String) {
        _registrationState.value = _registrationState.value.copy(firstName = firstName)
        validateFirstName()
    }

    fun updateLastName(lastName: String) {
        _registrationState.value = _registrationState.value.copy(lastName = lastName)
        validateLastName()
    }

    fun updatePhone(phone: String) {
        _registrationState.value = _registrationState.value.copy(phone = phone)
        validatePhone()
    }

    fun updateShopName(shopName: String) {
        _registrationState.value = _registrationState.value.copy(shopName = shopName)
        validateShopName()
    }

    fun updateStreet(street: String) {
        _registrationState.value = _registrationState.value.copy(street = street)
        validateStreet()
    }

    fun updateStreetNumber(streetNumber: String) {
        _registrationState.value = _registrationState.value.copy(streetNumber = streetNumber)
        validateStreetNumber()
    }

    fun updateShopType(shopType: String) {
        _registrationState.value = _registrationState.value.copy(shopType = shopType)
        validateShopType()
    }

    fun updateAcceptTerms(acceptTerms: Boolean) {
        _registrationState.value = _registrationState.value.copy(acceptTerms = acceptTerms)
        validateTerms()
    }

    // Navigation between steps
    fun goToNextStep() {
        if (_registrationState.value.currentStep < _registrationState.value.totalSteps) {
            _registrationState.value = _registrationState.value.copy(
                currentStep = _registrationState.value.currentStep + 1
            )
        }
    }

    fun goToPreviousStep() {
        if (_registrationState.value.currentStep > 1) {
            _registrationState.value = _registrationState.value.copy(
                currentStep = _registrationState.value.currentStep - 1
            )
        }
    }

    // Validations
    private fun validateEmail() {
        val email = _registrationState.value.email
        _emailError.value = when {
            email.isBlank() -> "El correo electronico es requerido"
            !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() -> "Formato invalido"
            else -> null
        }
    }

    private fun validatePassword() {
        val password = _registrationState.value.password
        _passwordError.value = when {
            password.isBlank() -> "Contraseña requerida"
            password.length < 8 -> "La contraseña debe tener al menos 8 caracteres"
            !password.any { it.isDigit() } -> "La contraseña debe contener al menos un número"
            !password.any { it.isLetter() } -> "La contraseña debe contener al menos una letra"
            else -> null
        }
        validateConfirmPassword()
    }

    private fun validateConfirmPassword() {
        val password = _registrationState.value.password
        val confirmPassword = _registrationState.value.confirmPassword
        _confirmPasswordError.value = when {
            confirmPassword.isBlank() -> "Por favor confirma tu contraseña"
            confirmPassword != password -> "Las contraseñas no coinciden"
            else -> null
        }
    }

    private fun validateFirstName() {
        val firstName = _registrationState.value.firstName
        _firstNameError.value = when {
            firstName.isBlank() -> "Nombre requerido"
            else -> null
        }
    }

    private fun validateLastName() {
        val lastName = _registrationState.value.lastName
        _lastNameError.value = when {
            lastName.isBlank() -> "Apellido requerido"
            else -> null
        }
    }

    private fun validatePhone() {
        val phone = _registrationState.value.phone
        _phoneError.value = when {
            phone.isBlank() -> "Numero de telefono requerido"
            phone.length < 8 -> "El numero de telefono debe tener al menos 8 digitos"
            !phone.all { it.isDigit() || it == '+' || it == ' ' || it == '-' } -> "Formato de telefono invalido"
            else -> null
        }
    }

    private fun validateShopName() {
        val shopName = _registrationState.value.shopName
        _shopNameError.value = when {
            shopName.isBlank() -> "Nombre de la tienda requerido"
            else -> null
        }
    }

    private fun validateStreet() {
        val street = _registrationState.value.street
        _streetError.value = when {
            street.isBlank() -> "Direccion requerida"
            else -> null
        }
    }

    private fun validateStreetNumber() {
        val streetNumber = _registrationState.value.streetNumber
        _streetNumberError.value = when {
            streetNumber.isBlank() -> "Altura requerida"
            !streetNumber.all { it.isDigit() } -> "La altura debe ser numerica"
            else -> null
        }
    }

    private fun validateShopType() {
        val shopType = _registrationState.value.shopType
        _shopTypeError.value = when {
            shopType.isBlank() -> "Tipo de tienda requerido"
            else -> null
        }
    }

    private fun validateTerms() {
        val acceptTerms = _registrationState.value.acceptTerms
        _termsError.value = if (!acceptTerms) {
            "You must accept the terms and conditions"
        } else {
            null
        }
    }

    // Validation for step 1
    fun isStep1Valid(): Boolean {
        validateEmail()
        validatePassword()
        validateConfirmPassword()
        validateFirstName()
        validateLastName()
        validatePhone()

        return _emailError.value == null &&
                _passwordError.value == null &&
                _confirmPasswordError.value == null &&
                _firstNameError.value == null &&
                _lastNameError.value == null &&
                _phoneError.value == null
    }

    // Validation for step 2
    fun isStep2Valid(): Boolean {
        validateShopName()
        validateStreet()
        validateStreetNumber()
        validateShopType()
        validateTerms()

        return _shopNameError.value == null &&
                _streetError.value == null &&
                _streetNumberError.value == null &&
                _shopTypeError.value == null &&
                _termsError.value == null
    }

    // Check if email already exists
    suspend fun checkEmailExists(email: String): Boolean {
        return try {
            val result = auth.fetchSignInMethodsForEmail(email).await()
            result.signInMethods?.isNotEmpty() ?: false
        } catch (e: Exception) {
            false
        }
    }

    // Complete registration
    fun registerUser() {
        viewModelScope.launch {
            _uiState.value = RegistrationUiState.Loading

            try {
                // Check if email already exists
                if (checkEmailExists(_registrationState.value.email)) {
                    _uiState.value = RegistrationUiState.Error("El correo ya está registrado")
                    return@launch
                }

                // Create user in Firebase Auth
                val authResult = auth.createUserWithEmailAndPassword(
                    _registrationState.value.email,
                    _registrationState.value.password
                ).await()

                val user = authResult.user
                if (user != null) {
                    // Save user data in Firestore
                    val userData = UserData(
                        uid = user.uid,
                        email = _registrationState.value.email,
                        firstName = _registrationState.value.firstName,
                        lastName = _registrationState.value.lastName,
                        phone = _registrationState.value.phone,
                        createdAt = System.currentTimeMillis()
                    )

                    // Save ice cream shop data in Firestore
                    val shopData = IceCreamShop(
                        id = db.collection("shops").document().id,
                        name = _registrationState.value.shopName,
                        street = _registrationState.value.street,
                        streetNumber = _registrationState.value.streetNumber,
                        type = _registrationState.value.shopType,
                        userId = user.uid,
                        createdAt = System.currentTimeMillis()
                    )

                    // Save to Firestore
                    db.collection("users").document(user.uid).set(userData).await()
                    db.collection("shops").document(shopData.id).set(shopData).await()

                    _uiState.value = RegistrationUiState.Success(user)
                } else {
                    _uiState.value = RegistrationUiState.Error("Error creating user")
                }
            } catch (e: Exception) {
                _uiState.value = RegistrationUiState.Error(e.message ?: "Unknown registration error")
            }
        }
    }

    fun resetState() {
        _registrationState.value = RegistrationState()
        _uiState.value = RegistrationUiState.Idle
        _emailError.value = null
        _passwordError.value = null
        _confirmPasswordError.value = null
        _firstNameError.value = null
        _lastNameError.value = null
        _phoneError.value = null
        _shopNameError.value = null
        _streetError.value = null
        _streetNumberError.value = null
        _shopTypeError.value = null
        _termsError.value = null
    }
}