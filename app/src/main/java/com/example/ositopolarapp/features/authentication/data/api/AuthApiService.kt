package com.example.ositopolarapp.features.authentication.data.api

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
// Imports de tus DTOs (Asegúrate que coincidan con tus paquetes)
import com.example.ositopolarapp.features.authentication.data.dto.SignInRequest
import com.example.ositopolarapp.features.authentication.data.dto.SignInResponse
import com.example.ositopolarapp.features.authentication.data.dto.Verify2FARequest
import com.example.ositopolarapp.features.authentication.data.dto.CreateRegistrationCheckoutRequest
import com.example.ositopolarapp.features.authentication.data.dto.RegistrationCheckoutResponse
import com.example.ositopolarapp.features.authentication.data.dto.CompleteRegistrationRequest
import com.example.ositopolarapp.features.authentication.data.dto.CompleteRegistrationResponse

interface AuthApiService {

    // --- LOGIN ---
    @POST("auth/login") // O "auth/signin" según tu backend
    suspend fun signIn(@Body request: SignInRequest): Response<SignInResponse>

    // --- 2FA ---
    @POST("auth/verify-2fa")
    suspend fun verify2FA(@Body request: Verify2FARequest): Response<Void> // Void si no devuelve cuerpo

    // --- REGISTRO ---
    @POST("auth/create-registration-checkout")
    suspend fun createRegistrationCheckout(
        @Body request: CreateRegistrationCheckoutRequest
    ): Response<RegistrationCheckoutResponse>

    @POST("auth/complete-registration")
    suspend fun completeRegistration(
        @Body request: CompleteRegistrationRequest
    ): Response<CompleteRegistrationResponse>
}