package com.example.ositopolarapp.features.authentication.data.dto

/**
 * Response containing 2FA status for a user
 */
data class TwoFactorStatusResponse(
    val enabled: Boolean,
    val username: String
)
