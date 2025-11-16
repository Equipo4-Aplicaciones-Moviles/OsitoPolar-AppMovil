package com.example.ositopolarapp.features.authentication.data.dto

import com.google.gson.annotations.SerializedName

// Lo que ENVIAMOS en el Paso 1
data class CreateRegistrationCheckoutRequest(
    @SerializedName("planId") val planId: Int,
    @SerializedName("userType") val userType: String,
    @SerializedName("successUrl") val successUrl: String,
    @SerializedName("cancelUrl") val cancelUrl: String
)

// Lo que RECIBIMOS en el Paso 1
data class RegistrationCheckoutResponse(
    @SerializedName("checkoutUrl") val checkoutUrl: String,
    @SerializedName("sessionId") val sessionId: String
)

// Lo que ENVIAMOS en el Paso 2
data class CompleteRegistrationRequest(
    @SerializedName("sessionId") val sessionId: String,
    @SerializedName("username") val username: String,
    @SerializedName("firstName") val firstName: String,
    @SerializedName("lastName") val lastName: String,
    @SerializedName("email") val email: String,
    @SerializedName("street") val street: String,
    @SerializedName("number") val number: String,
    @SerializedName("city") val city: String,
    @SerializedName("postalCode") val postalCode: String,
    @SerializedName("country") val country: String,
    @SerializedName("companyName") val companyName: String?,
    @SerializedName("taxId") val taxId: String?
)

// Lo que RECIBIMOS en el Paso 2 (credenciales generadas)
data class CompleteRegistrationResponse(
    @SerializedName("username") val username: String,
    @SerializedName("generatedPassword") val password: String,  // Backend usa "generatedPassword", no "password"
    @SerializedName("message") val message: String? = null,
    @SerializedName("success") val success: Boolean? = null,
    @SerializedName("userId") val userId: Int? = null,
    @SerializedName("userType") val userType: String? = null,
    @SerializedName("email") val email: String? = null
)
