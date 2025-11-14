package com.example.ositopolarapp.features.authentication.domain.usecase

import com.example.ositopolarapp.features.authentication.domain.repository.AuthRepository

class Enable2FAUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(username: String): Result<Unit> {
        return authRepository.enable2FA(username)
    }
}
