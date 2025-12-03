package com.example.ositopolarapp.features.servicemarketplace.data.api

import com.example.ositopolarapp.features.servicemarketplace.data.dto.MarketplaceServiceRequestDto
import com.example.ositopolarapp.features.servicemarketplace.data.dto.AcceptServiceRequestResponse
import retrofit2.Response
import retrofit2.http.*

/**
 * Service Marketplace API Service (Uber-style)
 *
 * For Providers: Browse and accept available service requests
 * For Owners: Their requests appear here for providers to accept
 */
interface ServiceMarketplaceApiService {

    /**
     * Get all available service requests in the marketplace (Providers only)
     * Endpoint: GET /api/v1/service-requests/marketplace
     */
    @GET("service-requests/marketplace")
    suspend fun getMarketplaceServiceRequests(): Response<List<MarketplaceServiceRequestDto>>

    /**
     * Provider accepts a service request from the marketplace
     * Endpoint: POST /api/v1/service-requests/{id}/accept
     */
    @POST("service-requests/{serviceRequestId}/accept")
    suspend fun acceptServiceRequest(
        @Path("serviceRequestId") serviceRequestId: Int
    ): Response<AcceptServiceRequestResponse>
}
