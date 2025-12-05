package com.example.ositopolarapp.features.payments.data.dto

/**
 * Request to complete subscription plan upgrade after successful payment
 */
data class CompleteUpgradeRequest(
    val userId: Int,
    val planId: Int,
    val sessionId: String
)
