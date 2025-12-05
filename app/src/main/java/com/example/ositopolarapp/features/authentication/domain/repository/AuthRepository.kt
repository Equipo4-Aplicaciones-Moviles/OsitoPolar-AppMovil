package com.example.ositopolarapp.features.authentication.domain.repository

import com.example.ositopolarapp.features.authentication.data.dto.CompleteRegistrationRequest
import com.example.ositopolarapp.features.authentication.domain.model.AuthenticatedUserEntity
import com.example.ositopolarapp.features.authentication.domain.model.RegistrationCheckoutEntity
import com.example.ositopolarapp.features.authentication.domain.model.LoginRequestDto
import com.example.ositopolarapp.features.authentication.domain.model.LoginResponseDto
import kotlinx.coroutines.flow.Flow

interface AuthRepository {

    suspend fun createRegistrationCheckout(
        planId: Int,
        userType: String
    ): Result<RegistrationCheckoutEntity> // Usamos el Result de Kotlin

    suspend fun completeRegistration(
        request: CompleteRegistrationRequest // Pasamos el request completo
    ): Result<Pair<String, String>> // Devuelve (username, password)

    suspend fun signIn( // <-- ¡Debe tener "suspend"!
        username: String,
        password: String
    ): Result<AuthenticatedUserEntity>

    fun getSessionToken(): Flow<String?>

    // Get current authenticated user data
    fun getCurrentUser(): Flow<AuthenticatedUserEntity?>

    // Para cerrar la sesión
    suspend fun signOut()

    suspend fun verifyTwoFactor(
        username: String,
        code: String
    ): Result<AuthenticatedUserEntity>

    suspend fun enable2FA(username: String): Result<Unit>

    suspend fun disable2FA(username: String): Result<Unit>
}