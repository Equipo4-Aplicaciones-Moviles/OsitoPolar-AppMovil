package com.example.ositopolarapp.features.authentication.data.dto

/**
 * Response when initiating 2FA setup
 */
data class Initiate2FAResponse(
    val qrCodeUrl: String,
    val secret: String,
    val backupCodes: List<String>? = null
)
