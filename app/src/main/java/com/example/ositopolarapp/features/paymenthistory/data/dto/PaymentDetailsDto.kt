package com.example.ositopolarapp.features.paymenthistory.data.dto

/**
 * Detailed payment information
 */
data class PaymentDetailsDto(
    val id: Int,
    val amount: Double,
    val currency: String,
    val type: String,
    val description: String,
    val status: String,
    val paymentMethod: String,
    val transactionId: String,
    val createdAt: String,
    val paidAt: String?,
    val receiptUrl: String?,
    val relatedEntityId: Int?,  // ID of subscription, rental, or service
    val relatedEntityType: String?  // "SUBSCRIPTION", "RENTAL", "SERVICE"
)
