package com.example.ositopolarapp.features.subscriptions.data.api

import com.example.ositopolarapp.features.subscriptions.data.dto.PlanDto
import com.example.ositopolarapp.features.subscriptions.data.dto.UpgradeSubscriptionRequest
import retrofit2.Response
import retrofit2.http.*

/**
 * Retrofit API service for Subscription/Plan endpoints.
 */
interface SubscriptionApiService {

    /**
     * Get all subscription plans.
     * Endpoint: GET /api/v1/subscriptions?userType={userType}
     * @param userType Optional filter: "Owner" or "Provider"
     */
    @GET("subscriptions")
    suspend fun getAllPlans(
        @Query("userType") userType: String? = null
    ): Response<List<PlanDto>>

    /**
     * Get subscription plan by ID.
     * Endpoint: GET /api/v1/subscriptions/{id}
     */
    @GET("subscriptions/{id}")
    suspend fun getPlanById(
        @Path("id") planId: Int
    ): Response<PlanDto>

    /**
     * Upgrade subscription plan.
     * Endpoint: PATCH /api/v1/subscriptions/{id}
     */
    @PATCH("subscriptions/{id}")
    suspend fun upgradeSubscription(
        @Path("id") subscriptionId: Int,
        @Body request: UpgradeSubscriptionRequest
    ): Response<PlanDto>
}
