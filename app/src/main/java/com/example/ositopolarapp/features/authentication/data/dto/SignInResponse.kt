package com.example.ositopolarapp.features.authentication.data.dto
import com.google.gson.annotations.SerializedName

/**
 * Lo que RECIBIMOS de: POST /api/v1/authentication/sign-in
 * (Basado en tu AuthResponse.js)
 *
 * Note: The backend returns different response structures based on authentication state:
 * - For 2FA setup: includes requiresTwoFactorSetup, qrCodeDataUrl, manualEntryKey
 * - For 2FA verification: includes requires2FA
 * - For successful auth: includes id, token, userType, profileId
 */
data class SignInResponse(
    @SerializedName("id") val id: Int? = null,
    @SerializedName("username") val username: String,
    @SerializedName("token") val token: String? = null,
    @SerializedName("userType") val userType: String? = null,
    @SerializedName("profileId") val profileId: Int? = null,

    // Campos de 2FA
    @SerializedName("requiresTwoFactorSetup") val requiresTwoFactorSetup: Boolean = false,
    @SerializedName("requires2FA") val requires2FA: Boolean = false,
    @SerializedName("qrCodeDataUrl") val qrCodeDataUrl: String? = null,
    @SerializedName("manualEntryKey") val manualEntryKey: String? = null,

    // Additional fields for Owner/Provider
    @SerializedName("balance") val balance: Double? = null,
    @SerializedName("planId") val planId: Int? = null,
    @SerializedName("maxUnits") val maxUnits: Int? = null,
    @SerializedName("maxClients") val maxClients: Int? = null,
    @SerializedName("companyName") val companyName: String? = null
)