package com.example.ositopolarapp.features.payments.data.dto

/**
 * Request to create a Stripe checkout session
 * Used for: subscription upgrades, equipment rentals, service payments
 */
data class CreateCheckoutSessionRequest(
    val planId: Int? = null,           // For subscription upgrade
    val equipmentId: Int? = null,       // For equipment rental
    val serviceRequestId: Int? = null,  // For service payment
    val successUrl: String,
    val cancelUrl: String
)
