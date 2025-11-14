package com.example.ositopolarapp.features.authentication.data.api

import com.example.ositopolarapp.features.authentication.data.dto.CompleteRegistrationRequest
import com.example.ositopolarapp.features.authentication.data.dto.CreateRegistrationCheckoutRequest
import com.example.ositopolarapp.features.authentication.data.dto.RegistrationCheckoutResponse
import com.example.ositopolarapp.features.authentication.data.dto.SignInRequest
import com.example.ositopolarapp.features.authentication.data.dto.SignInResponse
import com.example.ositopolarapp.features.authentication.data.dto.Verify2FARequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
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

    @POST("authentication/enable-2fa")
    suspend fun enable2FA(
        @Body request: com.example.ositopolarapp.features.authentication.data.dto.UsernameRequest
    ): Response<Unit>

    @POST("authentication/disable-2fa")
    suspend fun disable2FA(
        @Body request: com.example.ositopolarapp.features.authentication.data.dto.UsernameRequest
    ): Response<Unit>
}