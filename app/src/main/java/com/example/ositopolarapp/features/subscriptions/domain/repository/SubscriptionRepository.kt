package com.example.ositopolarapp.features.subscriptions.domain.repository

import com.example.ositopolarapp.features.subscriptions.domain.model.Plan

/**
 * Repository interface for Subscription/Plan operations.
 */
interface SubscriptionRepository {

    /**
     * Retrieves all subscription plans.
     * @param userType Optional filter: "Owner" or "Provider"
     * @return Result containing list of plans or error
     */
    suspend fun getAllPlans(userType: String? = null): Result<List<Plan>>

    /**
     * Retrieves a single plan by ID.
     * @param planId The plan ID
     * @return Result containing the plan or error
     */
    suspend fun getPlanById(planId: Int): Result<Plan>
}
