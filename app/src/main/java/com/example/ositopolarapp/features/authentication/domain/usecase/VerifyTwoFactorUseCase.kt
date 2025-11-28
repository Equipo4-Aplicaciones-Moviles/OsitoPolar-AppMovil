package com.example.ositopolarapp.features.authentication.domain.usecase

import com.example.ositopolarapp.features.authentication.data.dto.Verify2FARequest
import com.example.ositopolarapp.features.authentication.domain.repository.AuthRepository

class VerifyTwoFactorUseCase(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(username: String, code: String): Result<Boolean> {
        // 1. Creamos el objeto request
        val request = Verify2FARequest(username, code)

        // 2. Llamamos al repositorio
        return repository.verifyTwoFactor(request)
    }
}