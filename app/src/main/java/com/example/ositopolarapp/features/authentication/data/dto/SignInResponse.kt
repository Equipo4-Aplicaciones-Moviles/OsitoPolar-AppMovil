package com.example.ositopolarapp.features.authentication.data.dto
import com.google.gson.annotations.SerializedName

/**
 * Lo que RECIBIMOS de: POST /api/v1/authentication/sign-in
 * (Basado en tu AuthResponse.js)
 */
data class SignInResponse(
    @SerializedName("id") val id: Int,
    @SerializedName("username") val username: String,
    @SerializedName("token") val token: String,
    @SerializedName("userType") val userType: String,
    @SerializedName("profileId") val profileId: Int,

    // Campos de 2FA
    @SerializedName("requiresTwoFactorSetup") val requiresTwoFactorSetup: Boolean = false,
    @SerializedName("requires2FA") val requires2FA: Boolean = false,
    @SerializedName("qrCodeDataUrl") val qrCodeDataUrl: String? = null,
    @SerializedName("manualEntryKey") val manualEntryKey: String? = null
)