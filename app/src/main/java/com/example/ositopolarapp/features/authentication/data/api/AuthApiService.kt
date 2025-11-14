package com.example.ositopolarapp.features.authentication.data.api

import com.example.ositopolarapp.features.authentication.data.dto.*
import retrofit2.Response
import retrofit2.http.*

interface AuthApiService {

    /**
     * Paso 1: Crea la sesión de checkout en Stripe.
     * Corresponde a tu `createRegistrationCheckout`
     */
    @POST("authentication/create-registration-checkout")
    suspend fun createRegistrationCheckout(
        @Body request: CreateRegistrationCheckoutRequest
    ): Response<RegistrationCheckoutResponse> // Devuelve la URL y el

    /**
     * Paso 2: Completa el registro después del pago.
     * Corresponde a tu `completeRegistration`
     */
    @POST("authentication/complete-registration")
    suspend fun completeRegistration(
        @Body request: CompleteRegistrationRequest
    ): Response<Unit> // No devuelve nada, solo un 200 OK

    @POST("authentication/sign-in")
    suspend fun signIn(
        @Body request: SignInRequest
    ): Response<SignInResponse>

    @POST("authentication/verify-2fa")
    suspend fun verifyTwoFactor(
        @Body request: Verify2FARequest
    ): Response<SignInResponse>

    /**
     * Initiate 2FA setup for a user
     * Endpoint: POST /api/v1/authentication/initiate-2fa
     */
    @POST("authentication/initiate-2fa")
    suspend fun initiate2FA(
        @Body request: UsernameRequest
    ): Response<Initiate2FAResponse>

    /**
     * Get 2FA status for a user
     * Endpoint: GET /api/v1/authentication/2fa-status
     * @param username User's username as query parameter
     */
    @GET("authentication/2fa-status")
    suspend fun get2FAStatus(
        @Query("username") username: String
    ): Response<TwoFactorStatusResponse>

    @POST("authentication/enable-2fa")
    suspend fun enable2FA(
        @Body request: UsernameRequest
    ): Response<Unit>

    @POST("authentication/disable-2fa")
    suspend fun disable2FA(
        @Body request: UsernameRequest
    ): Response<Unit>
}