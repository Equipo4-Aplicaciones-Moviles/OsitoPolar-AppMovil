package com.example.ositopolarapp.features.authentication.domain.usecase

import com.example.ositopolarapp.features.authentication.domain.model.AuthenticatedUserEntity
import com.example.ositopolarapp.features.authentication.domain.repository.AuthRepository

class GetCurrentUserUseCase(private val repository: AuthRepository) {
    suspend operator fun invoke(): Result<AuthenticatedUserEntity> {
        return repository.getCurrentUser()
    }
}