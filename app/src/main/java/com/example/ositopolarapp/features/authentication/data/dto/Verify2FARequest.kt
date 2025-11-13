package com.example.ositopolarapp.features.authentication.data.dto
import com.google.gson.annotations.SerializedName

/**
 * DTO para la petición de verificación de código 2FA.
 * POST /api/v1/authentication/verify-2fa
 */
data class Verify2FARequest(
    @SerializedName("username") val username: String,
    @SerializedName("code") val code: String
)