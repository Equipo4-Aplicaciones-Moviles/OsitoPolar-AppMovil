package com.example.ositopolarapp.features.payments.data.dto

/**
 * Request to complete equipment rental after successful payment
 */
data class CompleteRentalRequest(
    val userId: Int,
    val equipmentId: Int,
    val sessionId: String,
    val rentalMonths: Int
)
