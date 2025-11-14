package com.example.ositopolarapp.features.authentication.domain.usecase

import com.example.ositopolarapp.features.authentication.domain.model.AuthenticatedUserEntity
import com.example.ositopolarapp.features.authentication.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow

/**
 * Use Case to get the current authenticated user data
 */
class GetCurrentUserUseCase(
    private val authRepository: AuthRepository
) {
    operator fun invoke(): Flow<AuthenticatedUserEntity?> {
        return authRepository.getCurrentUser()
    }
}
