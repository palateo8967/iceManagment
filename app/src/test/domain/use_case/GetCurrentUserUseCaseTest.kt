package com.icerojects.icemanagment.domain.use_case

import com.google.firebase.auth.FirebaseUser
import com.icerojects.icemanagment.domain.repository.AuthRepository
import org.junit.Before
import org.junit.Test
import org.junit.Assert.*
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

class GetCurrentUserUseCaseTest {

    @Mock
    private lateinit var authRepository: AuthRepository

    @Mock
    private lateinit var mockFirebaseUser: FirebaseUser

    private lateinit var getCurrentUserUseCase: GetCurrentUserUseCase

    @Before
    fun setUp() {

        MockitoAnnotations.openMocks(this)
        getCurrentUserUseCase = GetCurrentUserUseCase(authRepository)

    }

    @Test
    fun `getCurrentUser should return authenticated user`() {

        // Given
        whenever(authRepository.getCurrentUser()).thenReturn(mockFirebaseUser)

        // When
        val result = getCurrentUserUseCase()

        // Then
        assertEquals(mockFirebaseUser, result)
        verify(authRepository).getCurrentUser()

    }

    @Test
    fun `getCurrentUser should return null when no user authenticated`() {

        // Given
        whenever(authRepository.getCurrentUser()).thenReturn(null)

        // When
        val result = getCurrentUserUseCase()

        // Then
        assertNull(result)
        verify(authRepository).getCurrentUser()

    }
    
}