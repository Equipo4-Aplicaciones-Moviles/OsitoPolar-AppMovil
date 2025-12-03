package com.example.ositopolarapp.features.servicepayments.domain.usecase

import com.example.ositopolarapp.features.servicepayments.domain.model.ServicePaymentCheckout
import com.example.ositopolarapp.features.servicepayments.domain.repository.ServicePaymentRepository

class CreateServicePaymentUseCase(
    private val repository: ServicePaymentRepository
) {
    suspend operator fun invoke(
        workOrderId: Int,
        successUrl: String? = null,
        cancelUrl: String? = null
    ): Result<ServicePaymentCheckout> {
        return repository.createServicePaymentCheckout(workOrderId, successUrl, cancelUrl)
    }
}
