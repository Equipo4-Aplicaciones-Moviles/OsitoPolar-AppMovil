package com.example.ositopolarapp.features.servicemarketplace.domain.repository

import com.example.ositopolarapp.features.servicemarketplace.domain.model.MarketplaceServiceRequest

interface ServiceMarketplaceRepository {

    /**
     * Get all available service requests in the marketplace
     */
    suspend fun getMarketplaceRequests(): Result<List<MarketplaceServiceRequest>>

    /**
     * Accept a service request (Provider only)
     */
    suspend fun acceptServiceRequest(serviceRequestId: Int): Result<AcceptResult>
}

data class AcceptResult(
    val success: Boolean,
    val message: String
)
