package com.example.ositopolarapp.features.authentication.domain.repository

import com.example.ositopolarapp.features.authentication.data.dto.*
import com.example.ositopolarapp.features.authentication.domain.model.AuthenticatedUserEntity

interface AuthRepository {
    // --- LOGIN & SESSION ---
    suspend fun login(request: SignInRequest): Result<SignInResponse>
    suspend fun logout(): Result<Unit>
    suspend fun checkAuth(): Result<Boolean>
    suspend fun getCurrentUser(): Result<AuthenticatedUserEntity>

    // --- 2FA (Seguridad) ---
    suspend fun enable2FA(username: String): Result<String>
    suspend fun disable2FA(username: String): Result<Unit>
    suspend fun verifyTwoFactor(request: Verify2FARequest): Result<Boolean>

    // ESTA ES LA FUNCIÓN QUE TE DABA ERROR:
    suspend fun getTwoFactorStatus(username: String): Result<Boolean>

    // --- REGISTRO ---
    suspend fun createRegistrationCheckout(request: CreateRegistrationCheckoutRequest): Result<RegistrationCheckoutResponse>
    suspend fun completeRegistration(request: CompleteRegistrationRequest): Result<Pair<String, String>>
}