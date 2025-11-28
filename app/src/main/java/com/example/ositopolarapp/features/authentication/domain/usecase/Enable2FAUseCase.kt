package com.example.ositopolarapp.features.authentication.domain.usecase

import com.example.ositopolarapp.features.authentication.domain.repository.AuthRepository

class Enable2FAUseCase(private val repository: AuthRepository) {
    // Ahora devuelve Result<String> (la URL del QR)
    suspend operator fun invoke(username: String): Result<String> {
        return repository.enable2FA(username)
    }
}