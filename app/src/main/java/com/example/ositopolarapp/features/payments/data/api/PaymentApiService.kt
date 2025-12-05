package com.example.ositopolarapp.features.payments.data.api

import com.example.ositopolarapp.features.payments.data.dto.*
import retrofit2.Response
import retrofit2.http.*

/**
 * Payment API Service for Stripe integration
 *
 * Endpoints:
 * - POST /payments/create-checkout-session - Create Stripe checkout
 * - GET /payments/verify/{sessionId} - Verify checkout session
 * - POST /payments/complete-upgrade - Complete plan upgrade
 * - POST /payments/complete-rental - Complete equipment rental
 */
interface PaymentApiService {

    /**
     * Create a Stripe checkout session for subscription upgrade or rental
     * Endpoint: POST /api/v1/payments/create-checkout-session
     */
    @POST("payments/create-checkout-session")
    suspend fun createCheckoutSession(
        @Body request: CreateCheckoutSessionRequest
    ): Response<CheckoutSessionResponse>

    /**
     * Verify a checkout session status
     * Endpoint: GET /api/v1/payments/verify/{sessionId}
     */
    @GET("payments/verify/{sessionId}")
    suspend fun verifySession(
        @Path("sessionId") sessionId: String
    ): Response<VerifySessionResponse>

    /**
     * Complete subscription plan upgrade after successful payment
     * Endpoint: POST /api/v1/payments/complete-upgrade
     */
    @POST("payments/complete-upgrade")
    suspend fun completeUpgrade(
        @Body request: CompleteUpgradeRequest
    ): Response<Unit>

    /**
     * Complete equipment rental after successful payment
     * Endpoint: POST /api/v1/payments/complete-rental
     */
    @POST("payments/complete-rental")
    suspend fun completeRental(
        @Body request: CompleteRentalRequest
    ): Response<Unit>
}
