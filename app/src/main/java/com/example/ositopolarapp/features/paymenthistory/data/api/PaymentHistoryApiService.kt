package com.example.ositopolarapp.features.paymenthistory.data.api

import com.example.ositopolarapp.features.paymenthistory.data.dto.PaymentDetailsDto
import com.example.ositopolarapp.features.paymenthistory.data.dto.PaymentHistoryDto
import retrofit2.Response
import retrofit2.http.*

/**
 * Payment History API Service
 *
 * Endpoints:
 * - GET /payment-history/owner - Get owner payment history
 * - GET /payment-history/{id} - Get payment details
 */
interface PaymentHistoryApiService {

    /**
     * Get owner's payment history
     * Endpoint: GET /api/v1/payment-history/owner
     */
    @GET("payment-history/owner")
    suspend fun getOwnerPaymentHistory(): Response<List<PaymentHistoryDto>>

    /**
     * Get payment details by ID
     * Endpoint: GET /api/v1/payment-history/{id}
     */
    @GET("payment-history/{id}")
    suspend fun getPaymentDetails(
        @Path("id") paymentId: Int
    ): Response<PaymentDetailsDto>
}
