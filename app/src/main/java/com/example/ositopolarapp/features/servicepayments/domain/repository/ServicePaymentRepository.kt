package com.example.ositopolarapp.features.servicepayments.domain.repository

import com.example.ositopolarapp.features.servicepayments.domain.model.ServicePaymentCheckout

interface ServicePaymentRepository {

    /**
     * Create a service payment checkout
     */
    suspend fun createServicePaymentCheckout(
        workOrderId: Int,
        successUrl: String? = null,
        cancelUrl: String? = null
    ): Result<ServicePaymentCheckout>
}
