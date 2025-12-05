package com.example.ositopolarapp.features.servicepayments.data.api

import com.example.ositopolarapp.features.servicepayments.data.dto.CreateServicePaymentRequest
import com.example.ositopolarapp.features.servicepayments.data.dto.ServicePaymentCheckoutResponse
import retrofit2.Response
import retrofit2.http.*

/**
 * Service Payments API Service
 *
 * For owners to pay for completed services
 */
interface ServicePaymentApiService {

    /**
     * Create payment checkout for completed service
     * Endpoint: POST /api/v1/service-payments/create-checkout
     */
    @POST("service-payments/create-checkout")
    suspend fun createServicePaymentCheckout(
        @Body request: CreateServicePaymentRequest
    ): Response<ServicePaymentCheckoutResponse>
}
