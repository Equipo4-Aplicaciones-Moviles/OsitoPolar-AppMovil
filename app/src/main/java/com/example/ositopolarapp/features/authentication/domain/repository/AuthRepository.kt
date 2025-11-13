package com.example.ositopolarapp.features.authentication.domain.repository

import com.example.ositopolarapp.features.authentication.data.dto.CompleteRegistrationRequest
import com.example.ositopolarapp.features.authentication.domain.model.AuthenticatedUserEntity
import com.example.ositopolarapp.features.authentication.domain.model.RegistrationCheckoutEntity
import com.example.ositopolarapp.features.authentication.domain.model.LoginRequestDto
import com.example.ositopolarapp.features.authentication.domain.model.LoginResponseDto

interface AuthRepository {

    suspend fun createRegistrationCheckout(
        planId: Int,
        userType: String
    ): Result<RegistrationCheckoutEntity> // Usamos el Result de Kotlin

    suspend fun completeRegistration(
        request: CompleteRegistrationRequest // Pasamos el request completo
    ): Result<Unit> // Éxito o Fracaso

    suspend fun signIn( // <-- ¡Debe tener "suspend"!
        username: String,
        password: String
    ): Result<AuthenticatedUserEntity>


}