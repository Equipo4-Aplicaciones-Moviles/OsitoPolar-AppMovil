package com.example.ositopolarapp.features.authentication.domain.usecase

import com.example.ositopolarapp.features.authentication.domain.repository.AuthRepository
import com.example.ositopolarapp.features.authentication.domain.model.AuthenticatedUserEntity
import com.example.ositopolarapp.features.authentication.data.dto.Verify2FARequest // Necesitamos este DTO
//import com.example.ositopolarapp.features.authentication.data.dto.Verify2FAResponse // Necesitamos este DTO

// NOTA: Usamos el DTO de la capa de datos como parámetro, o creamos un 'Params' limpio.
// Usaremos un 'Params' limpio para mantener la arquitectura.

/**
 * Caso de Uso para verificar un código 2FA y completar el inicio de sesión.
 */
class VerifyTwoFactorUseCase(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(username: String, code: String): Result<AuthenticatedUserEntity> =
        repository.verifyTwoFactor(username, code)
}