package com.example.ositopolarapp.features.servicerequests.data.api

import com.example.ositopolarapp.features.servicerequests.data.dto.AddFeedbackRequest
import com.example.ositopolarapp.features.servicerequests.data.dto.CreateServiceRequestRequest
import com.example.ositopolarapp.features.servicerequests.data.dto.ServiceRequestDto
import retrofit2.Response
import retrofit2.http.*

/**
 * Service Request API Service
 *
 * Endpoints:
 * - GET /service-requests - Get all service requests
 * - GET /service-requests/{id} - Get service request by ID
 * - POST /service-requests - Create new service request
 * - PUT /service-requests/{id}/feedback - Add rating/feedback
 * - PUT /service-requests/{id}/status - Update status (cancel/reject)
 */
interface ServiceRequestApiService {

    @GET("service-requests")
    suspend fun getAllServiceRequests(): Response<List<ServiceRequestDto>>

    @GET("service-requests/{id}")
    suspend fun getServiceRequestById(
        @Path("id") id: Int
    ): Response<ServiceRequestDto>

    @POST("service-requests")
    suspend fun createServiceRequest(
        @Body request: CreateServiceRequestRequest
    ): Response<ServiceRequestDto>

    @PUT("service-requests/{id}/feedback")
    suspend fun addFeedback(
        @Path("id") serviceRequestId: Int,
        @Body request: AddFeedbackRequest
    ): Response<ServiceRequestDto>

    @PUT("service-requests/{id}/status")
    suspend fun updateStatus(
        @Path("id") serviceRequestId: Int,
        @Body statusUpdate: Map<String, String>
    ): Response<ServiceRequestDto>

    @DELETE("service-requests/{id}")
    suspend fun deleteServiceRequest(
        @Path("id") id: Int
    ): Response<Unit>
}
