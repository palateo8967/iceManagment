package com.icerojects.icemanagment.domain.use_case

import com.icerojects.icemanagment.domain.repository.AuthRepository
import com.icerojects.icemanagment.utils.Resource
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.Assert.*
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.doThrow
import org.mockito.kotlin.whenever

class SignOutUseCaseTest {

    @Mock
    private lateinit var authRepository: AuthRepository

    private lateinit var signOutUseCase: SignOutUseCase

    @Before
    fun setUp() {

        MockitoAnnotations.openMocks(this)
        signOutUseCase = SignOutUseCase(authRepository)

    }

    @Test
    fun `signOut should return success`() = runTest {

        // Given - repository.signOut() no lanza excepción

        // When
        val flow = signOutUseCase()
        val emissions = mutableListOf<Resource<Unit>>()
        flow.collect { emissions.add(it) }

        // Then
        assertEquals(2, emissions.size)
        assertTrue(emissions[0] is Resource.Loading)
        assertTrue(emissions[1] is Resource.Succes) 

    }

    @Test
    fun `signOut should return error when repository throws exception`() = runTest {

        // Given
        val exception = Exception("Sign out failed")
        doThrow(exception).whenever(authRepository).signOut()

        // When
        val flow = signOutUseCase()
        val emissions = mutableListOf<Resource<Unit>>()
        flow.collect { emissions.add(it) }

        // Then
        assertEquals(2, emissions.size)
        assertTrue(emissions[0] is Resource.Loading)
        assertTrue(emissions[1] is Resource.Error)
        assertEquals("Sign out failed", emissions[1].message)

    }
    
}