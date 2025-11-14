package com.example.ositopolarapp.features.servicerequests.domain.usecase

import com.example.ositopolarapp.features.servicerequests.domain.model.ServiceRequest
import com.example.ositopolarapp.features.servicerequests.domain.repository.ServiceRequestRepository

/**
 * Use Case: Get All Service Requests
 */
class GetAllServiceRequestsUseCase(
    private val repository: ServiceRequestRepository
) {
    suspend operator fun invoke(): Result<List<ServiceRequest>> {
        return repository.getAllServiceRequests()
    }
}
