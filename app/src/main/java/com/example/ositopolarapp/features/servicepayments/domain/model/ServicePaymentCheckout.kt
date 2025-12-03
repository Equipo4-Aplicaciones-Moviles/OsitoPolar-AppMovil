package com.example.ositopolarapp.features.servicepayments.domain.model

/**
 * Domain model for service payment checkout
 */
data class ServicePaymentCheckout(
    val checkoutUrl: String,
    val sessionId: String,
    val totalAmount: Double,
    val platformFee: Double,
    val providerAmount: Double,
    val platformFeePercentage: Double,
    val workOrderId: Int,
    val workOrderNumber: String,
    val workOrderTitle: String,
    val providerName: String
)
