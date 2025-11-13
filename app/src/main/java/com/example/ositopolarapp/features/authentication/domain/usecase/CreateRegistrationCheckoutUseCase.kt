package com.example.ositopolarapp.features.authentication.domain.usecase

import com.example.ositopolarapp.features.authentication.domain.repository.AuthRepository
import com.example.ositopolarapp.features.authentication.data.dto.CompleteRegistrationRequest
class CreateRegistrationCheckoutUseCase(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(planId: Int, userType: String) =
        repository.createRegistrationCheckout(planId, userType)
}





class CompleteRegistrationUseCase(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(request: CompleteRegistrationRequest) =
        repository.completeRegistration(request)
}