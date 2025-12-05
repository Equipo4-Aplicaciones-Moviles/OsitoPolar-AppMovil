package com.example.ositopolarapp.features.servicerequests.domain.usecase

import com.example.ositopolarapp.features.servicerequests.domain.model.ServiceRequest
import com.example.ositopolarapp.features.servicerequests.domain.repository.ServiceRequestRepository

/**
 * Use Case: Create Service Request
 */
class CreateServiceRequestUseCase(
    private val repository: ServiceRequestRepository
) {
    suspend operator fun invoke(
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
        return repository.createServiceRequest(
            orderNumber = orderNumber,
            title = title,
            description = description,
            issueDetails = issueDetails,
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
    }
}
