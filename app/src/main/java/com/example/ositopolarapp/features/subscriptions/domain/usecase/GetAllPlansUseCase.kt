package com.example.ositopolarapp.features.subscriptions.domain.usecase

import com.example.ositopolarapp.features.subscriptions.domain.repository.SubscriptionRepository

/**
 * Use case for retrieving all subscription plans.
 */
class GetAllPlansUseCase(
    private val repository: SubscriptionRepository
) {
    suspend operator fun invoke(userType: String? = null) = repository.getAllPlans(userType)
}
