package com.icerojects.icemanagment.domain.use_case

import com.google.firebase.auth.FirebaseUser
import com.icerojects.icemanagment.domain.repository.AuthRepository
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.Assert.*
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.whenever

class GetAuthStateUseCaseTest {

    @Mock
    private lateinit var authRepository: AuthRepository

    @Mock
    private lateinit var mockFirebaseUser: FirebaseUser

    private lateinit var getAuthStateUseCase: GetAuthStateUseCase

    @Before
    fun setUp() {

        MockitoAnnotations.openMocks(this)
        getAuthStateUseCase = GetAuthStateUseCase(authRepository)

    }

    @Test
    fun `getAuthState should return authenticated user`() = runTest {

        // Given
        whenever(authRepository.getAuthState()).thenReturn(flowOf(mockFirebaseUser))

        // When
        val flow = getAuthStateUseCase()
        val emissions = mutableListOf<FirebaseUser?>()
        flow.collect { emissions.add(it) }

        // Then
        assertEquals(1, emissions.size)
        assertEquals(mockFirebaseUser, emissions[0])

    }

    @Test
    fun `getAuthState should return null when user not authenticated`() = runTest {

        // Given
        whenever(authRepository.getAuthState()).thenReturn(flowOf(null))

        // When
        val flow = getAuthStateUseCase()
        val emissions = mutableListOf<FirebaseUser?>()
        flow.collect { emissions.add(it) }

        // Then
        assertEquals(1, emissions.size)
        assertNull(emissions[0])
        
    }

    @Test
    fun `getAuthState should emit multiple states`() = runTest {
        
        // Given
        whenever(authRepository.getAuthState()).thenReturn(flowOf(null, mockFirebaseUser))

        // When
        val flow = getAuthStateUseCase()
        val emissions = mutableListOf<FirebaseUser?>()
        flow.collect { emissions.add(it) }

        // Then
        assertEquals(2, emissions.size)
        assertNull(emissions[0])
        assertEquals(mockFirebaseUser, emissions[1])

    }

}