package com.example.ositopolarapp.features.servicerequests.domain.usecase

import com.example.ositopolarapp.features.servicerequests.domain.model.ServiceRequest
import com.example.ositopolarapp.features.servicerequests.domain.repository.ServiceRequestRepository

/**
 * Use Case: Add Feedback to Service Request
 */
class AddFeedbackUseCase(
    private val repository: ServiceRequestRepository
) {
    suspend operator fun invoke(serviceRequestId: Int, rating: Int): Result<ServiceRequest> {
        // Validate rating (1-5)
        if (rating < 1 || rating > 5) {
            return Result.failure(IllegalArgumentException("Rating must be between 1 and 5"))
        }

        return repository.addFeedback(serviceRequestId, rating)
    }
}
