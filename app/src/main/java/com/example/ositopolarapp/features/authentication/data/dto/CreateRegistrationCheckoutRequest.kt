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

// Lo que ENVIAMOS en el Paso 2 (Y usamos para guardar el formulario localmente)
data class CompleteRegistrationRequest(
    // Campos obligatorios para el Backend (pero pueden ser nulos al guardar localmente)
    @SerializedName("sessionId") val sessionId: String? = null,

    // Datos del Usuario
    @SerializedName("firstName") val firstName: String,
    @SerializedName("lastName") val lastName: String,
    @SerializedName("email") val email: String,
    @SerializedName("username") val username: String, // Campo obligatorio

    // Dirección
    @SerializedName("street") val street: String,
    @SerializedName("number") val number: String,
    @SerializedName("city") val city: String,
    @SerializedName("country") val country: String,

    // IMPORTANTE: Mapeamos "zipCode" (código UI) a "postalCode" (Backend JSON)
    @SerializedName("postalCode") val zipCode: String,

    // Campos opcionales (Backend los pide pero la UI nueva quizás no)
    @SerializedName("companyName") val companyName: String? = null,
    @SerializedName("taxId") val taxId: String? = null,

    // Campos EXTRA transient (Para que el ViewModel sepa qué plan se eligió, pero NO se envían al backend)
    @Transient val planId: Int? = null,
    @Transient val userType: String? = null
)

// Lo que RECIBIMOS en el Paso 2 (credenciales generadas)
data class CompleteRegistrationResponse(
    @SerializedName("username") val username: String,
    @SerializedName("generatedPassword") val password: String,
    @SerializedName("message") val message: String? = null,
    @SerializedName("success") val success: Boolean? = null,
    @SerializedName("userId") val userId: Int? = null,
    @SerializedName("userType") val userType: String? = null,
    @SerializedName("email") val email: String? = null
)