package com.example.ositopolarapp.features.authentication.domain.usecase

import com.example.ositopolarapp.features.authentication.data.dto.CompleteRegistrationRequest
import com.example.ositopolarapp.features.authentication.domain.repository.AuthRepository

class CompleteRegistrationUseCase(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(request: CompleteRegistrationRequest): Result<Pair<String, String>> {
        return repository.completeRegistration(request)
    }
}