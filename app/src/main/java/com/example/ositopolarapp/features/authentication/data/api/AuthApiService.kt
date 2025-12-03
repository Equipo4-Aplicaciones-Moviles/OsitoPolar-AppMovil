package com.example.ositopolarapp.features.authentication.data.api

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
// Imports de tus DTOs (Asegúrate que coincidan con tus paquetes)
import com.example.ositopolarapp.features.authentication.data.dto.SignInRequest
import com.example.ositopolarapp.features.authentication.data.dto.SignInResponse
import com.example.ositopolarapp.features.authentication.data.dto.Verify2FARequest
import com.example.ositopolarapp.features.authentication.data.dto.UsernameRequest
import com.example.ositopolarapp.features.authentication.data.dto.Initiate2FAResponse
import com.example.ositopolarapp.features.authentication.data.dto.TwoFactorStatusResponse
import com.example.ositopolarapp.features.authentication.data.dto.CreateRegistrationCheckoutRequest
import com.example.ositopolarapp.features.authentication.data.dto.RegistrationCheckoutResponse
import com.example.ositopolarapp.features.authentication.data.dto.CompleteRegistrationRequest
import com.example.ositopolarapp.features.authentication.data.dto.CompleteRegistrationResponse

interface AuthApiService {

    // --- LOGIN ---
    @POST("authentication/sign-in")
    suspend fun signIn(@Body request: SignInRequest): Response<SignInResponse>

    // --- 2FA ---
    @POST("authentication/verify-2fa")
    suspend fun verify2FA(@Body request: Verify2FARequest): Response<Void>

    @POST("authentication/initiate-2fa")
    suspend fun initiate2FA(@Body request: UsernameRequest): Response<Initiate2FAResponse>

    @POST("authentication/enable-2fa")
    suspend fun enable2FA(@Body request: UsernameRequest): Response<Initiate2FAResponse>

    @POST("authentication/disable-2fa")
    suspend fun disable2FA(@Body request: UsernameRequest): Response<Void>

    @POST("authentication/2fa-status")
    suspend fun get2FAStatus(@Body request: UsernameRequest): Response<TwoFactorStatusResponse>

    // --- REGISTRO ---
    @POST("authentication/create-registration-checkout")
    suspend fun createRegistrationCheckout(
        @Body request: CreateRegistrationCheckoutRequest
    ): Response<RegistrationCheckoutResponse>

    @POST("authentication/complete-registration")
    suspend fun completeRegistration(
        @Body request: CompleteRegistrationRequest
    ): Response<CompleteRegistrationResponse>

    @POST("authentication/register")
    suspend fun register(@Body request: CompleteRegistrationRequest): Response<CompleteRegistrationResponse>
}