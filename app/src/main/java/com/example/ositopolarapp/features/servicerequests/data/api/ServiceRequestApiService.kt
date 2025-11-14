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
 * - GET /serviceRequests - Get all service requests
 * - GET /serviceRequests/{id} - Get service request by ID
 * - POST /serviceRequests - Create new service request
 * - PUT /serviceRequests/{id}/feedback - Add rating/feedback
 * - PUT /serviceRequests/{id}/status - Update status (cancel/reject)
 */
interface ServiceRequestApiService {

    @GET("serviceRequests")
    suspend fun getAllServiceRequests(): Response<List<ServiceRequestDto>>

    @GET("serviceRequests/{id}")
    suspend fun getServiceRequestById(
        @Path("id") id: Int
    ): Response<ServiceRequestDto>

    @POST("serviceRequests")
    suspend fun createServiceRequest(
        @Body request: CreateServiceRequestRequest
    ): Response<ServiceRequestDto>

    @PUT("serviceRequests/{id}/feedback")
    suspend fun addFeedback(
        @Path("id") serviceRequestId: Int,
        @Body request: AddFeedbackRequest
    ): Response<ServiceRequestDto>

    @PUT("serviceRequests/{id}/status")
    suspend fun updateStatus(
        @Path("id") serviceRequestId: Int,
        @Body statusUpdate: Map<String, String>
    ): Response<ServiceRequestDto>

    @DELETE("serviceRequests/{id}")
    suspend fun deleteServiceRequest(
        @Path("id") id: Int
    ): Response<Unit>
}
