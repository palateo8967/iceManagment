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

class SignInUuseCaseTest {

    @Mock
    private lateinit var authRepository: AuthRepository

    @Mock 
    private lateinit var mockFirebaseUser: FirebaseUser

    private lateinit var signInUseCase: SignInUseCase

    @Before
    fun setUp() {

        MockitoAnnotations.openMocks(this) 
        signInUseCase = SignInUseCase(authRepository)

    }

    @Test
    fun `signIn with valid credentials should return success`() = runTest {

        val email = "test@example.com"
        val password = "password123"
        val expectedResult = AuthOperationResult.Succes(mockFirebaseUser)

        whenever(authRepository.SignIn(email, password)).thenReturn(expectedResult)

        //When
        val flow = signInUseCase(email, password)
        val emissions = mutableListOf<Resource<AuthOperationResult>>()
        flow.collect { emissions.add(it) }

        //Then
        assertEquals(2, emissions.size)
        assertTrue(emissions[0] is Resource.Loading)
        assertTrue(emissions[1] is Resource.Succes)
        assertEquals(expectedResult, emissions[1].data)

    }

    @Test
    fun `signIn with invalid credentials should return error`() = runTest {
        //Given
        val email = "invalid@example.com"
        val password = "wrongpassword"

        //When
        val flow = signInUseCase(email, password)
        val emissions = mutableListOf<Resource<AuthOperationResult>>()
        flow.collect { emissions.add(it) }

        //Then
        assertEquals(2, emissions.size)
        assertTrue(emissions[0] is Resource.Loading)
        assertTrue(emissions[1] is Resource.Error)
        assertEquals("Invalid credentials", emissions[1].message)

    }

    @Test
    fun `signIn with empty email should return error`() = runTest {
        // Given
        val email = ""
        val password = "password123"
        val exception = Exception("Email cannot be empty")
        whenever(authRepository.signIn(email, password)).thenThrow(exception)

        // When
        val flow = signInUseCase(email, password)
        val emissions = mutableListOf<Resource<AuthOperationResult>>()
        flow.collect { emissions.add(it) }

        // Then
        assertEquals(2, emissions.size)
        assertTrue(emissions[0] is Resource.Loading)
        assertTrue(emissions[1] is Resource.Error)
        assertEquals("Email cannot be empty", emissions[1].message)
    }

}