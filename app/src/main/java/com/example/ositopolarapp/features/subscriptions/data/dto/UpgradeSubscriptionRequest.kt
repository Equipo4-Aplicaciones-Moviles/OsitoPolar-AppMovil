package com.example.ositopolarapp.features.subscriptions.data.dto

/**
 * Request to upgrade user's subscription plan
 */
data class UpgradeSubscriptionRequest(
    val userId: Int,
    val planId: Int
)
