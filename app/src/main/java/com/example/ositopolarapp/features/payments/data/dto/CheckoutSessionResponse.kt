package com.example.ositopolarapp.features.payments.data.dto

/**
 * Response containing Stripe checkout session details
 */
data class CheckoutSessionResponse(
    val sessionId: String,
    val checkoutUrl: String
)
