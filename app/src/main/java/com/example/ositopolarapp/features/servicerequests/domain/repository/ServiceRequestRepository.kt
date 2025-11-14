package com.example.ositopolarapp.features.servicerequests.domain.repository

import com.example.ositopolarapp.features.servicerequests.domain.model.ServiceRequest

/**
 * Service Request Repository Interface
 *
 * Defines the contract for service request data operations.
 */
interface ServiceRequestRepository {

    suspend fun getAllServiceRequests(): Result<List<ServiceRequest>>

    suspend fun getServiceRequestById(id: Int): Result<ServiceRequest>

    suspend fun createServiceRequest(
        orderNumber: String,
        title: String,
        description: String,
        issueDetails: String?,
        equipmentId: Int,
        reportedByUserId: Int,
        serviceType: String,
        priority: String,
        urgency: String,
        isEmergency: Boolean,
        scheduledDate: String?,
        timeSlot: String?,
        serviceAddress: String
    ): Result<ServiceRequest>

    suspend fun addFeedback(serviceRequestId: Int, rating: Int): Result<ServiceRequest>

    suspend fun cancelServiceRequest(serviceRequestId: Int): Result<ServiceRequest>

    suspend fun deleteServiceRequest(id: Int): Result<Unit>
}
