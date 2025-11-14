package com.example.ositopolarapp.features.payments.data.dto

/**
 * Response for verifying checkout session status
 */
data class VerifySessionResponse(
    val sessionId: String,
    val status: String,  // "complete", "open", "expired"
    val paymentStatus: String  // "paid", "unpaid", "no_payment_required"
)
