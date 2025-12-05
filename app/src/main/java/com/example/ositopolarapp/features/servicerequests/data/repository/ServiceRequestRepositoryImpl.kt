package com.example.ositopolarapp.features.servicerequests.data.repository

import com.example.ositopolarapp.features.servicerequests.data.api.ServiceRequestApiService
import com.example.ositopolarapp.features.servicerequests.data.dto.AddFeedbackRequest
import com.example.ositopolarapp.features.servicerequests.data.dto.CreateServiceRequestRequest
import com.example.ositopolarapp.features.servicerequests.data.mapper.ServiceRequestMapper
import com.example.ositopolarapp.features.servicerequests.domain.model.ServiceRequest
import com.example.ositopolarapp.features.servicerequests.domain.repository.ServiceRequestRepository

/**
 * Service Request Repository Implementation
 *
 * Handles API communication and data mapping for service requests.
 */
class ServiceRequestRepositoryImpl(
    private val apiService: ServiceRequestApiService
) : ServiceRequestRepository {

    override suspend fun getAllServiceRequests(): Result<List<ServiceRequest>> {
        return try {
            val response = apiService.getAllServiceRequests()
            if (response.isSuccessful && response.body() != null) {
                val serviceRequests = ServiceRequestMapper.toDomainList(response.body()!!)
                Result.success(serviceRequests)
            } else {
                Result.failure(Exception("Failed to fetch service requests: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getServiceRequestById(id: Int): Result<ServiceRequest> {
        return try {
            val response = apiService.getServiceRequestById(id)
            if (response.isSuccessful && response.body() != null) {
                val serviceRequest = ServiceRequestMapper.toDomain(response.body()!!)
                Result.success(serviceRequest)
            } else {
                Result.failure(Exception("Failed to fetch service request: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun createServiceRequest(
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
    ): Result<ServiceRequest> {
        return try {
            val request = CreateServiceRequestRequest(
                orderNumber = orderNumber,
                title = title,
                description = description,
                issueDetails = issueDetails,
                clientId = null,
                companyId = null,
                equipmentId = equipmentId,
                reportedByUserId = reportedByUserId,
                serviceType = serviceType,
                priority = priority,
                urgency = urgency,
                isEmergency = isEmergency,
                scheduledDate = scheduledDate,
                timeSlot = timeSlot,
                serviceAddress = serviceAddress
            )

            val response = apiService.createServiceRequest(request)
            if (response.isSuccessful && response.body() != null) {
                val serviceRequest = ServiceRequestMapper.toDomain(response.body()!!)
                Result.success(serviceRequest)
            } else {
                Result.failure(Exception("Failed to create service request: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun addFeedback(serviceRequestId: Int, rating: Int): Result<ServiceRequest> {
        return try {
            val request = AddFeedbackRequest(rating)
            val response = apiService.addFeedback(serviceRequestId, request)
            if (response.isSuccessful && response.body() != null) {
                val serviceRequest = ServiceRequestMapper.toDomain(response.body()!!)
                Result.success(serviceRequest)
            } else {
                Result.failure(Exception("Failed to add feedback: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun cancelServiceRequest(serviceRequestId: Int): Result<ServiceRequest> {
        return try {
            val statusUpdate = mapOf("newStatus" to "cancelled")
            val response = apiService.updateStatus(serviceRequestId, statusUpdate)
            if (response.isSuccessful && response.body() != null) {
                val serviceRequest = ServiceRequestMapper.toDomain(response.body()!!)
                Result.success(serviceRequest)
            } else {
                Result.failure(Exception("Failed to cancel service request: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun deleteServiceRequest(id: Int): Result<Unit> {
        return try {
            val response = apiService.deleteServiceRequest(id)
            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                Result.failure(Exception("Failed to delete service request: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
