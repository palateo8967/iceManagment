package com.icerojects.icemanagment.domain.use_case

import com.google.firebase.auth.FirebaseUser
import com.icerojects.icemanagment.domain.model.AuthOperationResult
import com.icerojects.icemanagment.domain.repository.AuthRepository
import com.icerojects.icemanagment.utils.Resource
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.Assert.*
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.whenever

class SignUpUseCaseTest {

    @Mock
    private lateinit var authRepository: AuthRepository

    @Mock
    private lateinit var mockFirebaseUser: FirebaseUser

    private lateinit var signUpUseCase: SignUpUseCase

    @Before
    fun setUp() {

        MockitoAnnotations.openMocks(this)
        signUpUseCase = SignUpUseCase(authRepository)

    }

    @Test
    fun `signUp with valid data should return success`() = runTest {

        // Given
        val email = "newuser@example.com"
        val password = "password123"
        val expectedResult = AuthOperationResult.Succes(mockFirebaseUser)
        whenever(authRepository.signUp(email, password)).thenReturn(expectedResult)

        // When
        val flow = signUpUseCase(email, password)
        val emissions = mutableListOf<Resource<AuthOperationResult>>()
        flow.collect { emissions.add(it) }

        // Then
        assertEquals(2, emissions.size)
        assertTrue(emissions[0] is Resource.Loading)
        assertTrue(emissions[1] is Resource.Success)
        assertEquals(expectedResult, emissions[1].data)

    }

    @Test
    fun `signUp with existing email should return error`() = runTest {

        // Given
        val email = "existing@example.com"
        val password = "password123"
        val exception = Exception("Email already exists")
        whenever(authRepository.signUp(email, password)).thenThrow(exception)

        // When
        val flow = signUpUseCase(email, password)
        val emissions = mutableListOf<Resource<AuthOperationResult>>()
        flow.collect { emissions.add(it) }

        // Then
        assertEquals(2, emissions.size)
        assertTrue(emissions[0] is Resource.Loading)
        assertTrue(emissions[1] is Resource.Error)
        assertEquals("Email already exists", emissions[1].message)

    }

    @Test
    fun `signUp with weak password should return error`() = runTest {

        // Given
        val email = "test@example.com"
        val password = "123"
        val exception = Exception("Password too weak")
        whenever(authRepository.signUp(email, password)).thenThrow(exception)

        // When
        val flow = signUpUseCase(email, password)
        val emissions = mutableListOf<Resource<AuthOperationResult>>()
        flow.collect { emissions.add(it) }

        // Then
        assertEquals(2, emissions.size)
        assertTrue(emissions[0] is Resource.Loading)
        assertTrue(emissions[1] is Resource.Error)
        assertEquals("Password too weak", emissions[1].message)

    }
    
}