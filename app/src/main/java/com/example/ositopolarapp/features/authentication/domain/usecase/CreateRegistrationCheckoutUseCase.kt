package com.example.ositopolarapp.features.authentication.domain.usecase

import com.example.ositopolarapp.features.authentication.data.dto.CreateRegistrationCheckoutRequest
import com.example.ositopolarapp.features.authentication.data.dto.RegistrationCheckoutResponse
import com.example.ositopolarapp.features.authentication.domain.repository.AuthRepository

class CreateRegistrationCheckoutUseCase(
    private val repository: AuthRepository
) {

    // Clase para recibir los parámetros desde el ViewModel de forma ordenada
    data class Params(
        val planId: Int,
        val userType: String
    )

    suspend operator fun invoke(params: Params): Result<RegistrationCheckoutResponse> {

        // 1. Construimos el objeto que pide el Repositorio
        //    Aquí definimos las URLs a las que Stripe debe volver
        val request = CreateRegistrationCheckoutRequest(
            planId = params.planId,
            userType = params.userType,
            // Esta URL debe coincidir con el Deep Link de tu AndroidManifest
            successUrl = "ositopolar://registration?session_id={CHECKOUT_SESSION_ID}",
            cancelUrl = "ositopolar://registration/cancel"
        )

        // 2. Llamamos al repositorio pasando el OBJETO, no el Int suelto
        return repository.createRegistrationCheckout(request)
    }
}