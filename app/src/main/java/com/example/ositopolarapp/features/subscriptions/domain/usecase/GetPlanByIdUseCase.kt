package com.example.ositopolarapp.features.subscriptions.domain.usecase

import com.example.ositopolarapp.features.subscriptions.domain.repository.SubscriptionRepository

/**
 * Use case for retrieving a subscription plan by ID.
 */
class GetPlanByIdUseCase(
    private val repository: SubscriptionRepository
) {
    suspend operator fun invoke(planId: Int) = repository.getPlanById(planId)
}
