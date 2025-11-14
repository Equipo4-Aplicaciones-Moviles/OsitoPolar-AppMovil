package com.example.ositopolarapp.features.subscriptions.data.api

import com.example.ositopolarapp.features.subscriptions.data.dto.PlanDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

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
}
