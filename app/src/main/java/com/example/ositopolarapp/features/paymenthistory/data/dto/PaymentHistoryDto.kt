package com.example.ositopolarapp.features.paymenthistory.data.dto

/**
 * Payment history record
 */
data class PaymentHistoryDto(
    val id: Int,
    val amount: Double,
    val currency: String,  // "USD", "PEN", etc.
    val type: String,  // "SUBSCRIPTION", "RENTAL", "SERVICE"
    val description: String,
    val status: String,  // "COMPLETED", "PENDING", "FAILED"
    val paymentMethod: String,  // "STRIPE", "CULQI", "IZIPAY"
    val transactionId: String,
    val createdAt: String,
    val paidAt: String?
)
