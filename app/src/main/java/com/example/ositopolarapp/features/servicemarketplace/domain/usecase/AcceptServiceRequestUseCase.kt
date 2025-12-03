package com.example.ositopolarapp.features.servicemarketplace.domain.usecase

import com.example.ositopolarapp.features.servicemarketplace.domain.repository.AcceptResult
import com.example.ositopolarapp.features.servicemarketplace.domain.repository.ServiceMarketplaceRepository

class AcceptServiceRequestUseCase(
    private val repository: ServiceMarketplaceRepository
) {
    suspend operator fun invoke(serviceRequestId: Int): Result<AcceptResult> {
        return repository.acceptServiceRequest(serviceRequestId)
    }
}
