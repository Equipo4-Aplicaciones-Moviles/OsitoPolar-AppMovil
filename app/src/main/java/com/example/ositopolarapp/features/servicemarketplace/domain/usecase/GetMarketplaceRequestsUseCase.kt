package com.example.ositopolarapp.features.servicemarketplace.domain.usecase

import com.example.ositopolarapp.features.servicemarketplace.domain.model.MarketplaceServiceRequest
import com.example.ositopolarapp.features.servicemarketplace.domain.repository.ServiceMarketplaceRepository

class GetMarketplaceRequestsUseCase(
    private val repository: ServiceMarketplaceRepository
) {
    suspend operator fun invoke(): Result<List<MarketplaceServiceRequest>> {
        return repository.getMarketplaceRequests()
    }
}
