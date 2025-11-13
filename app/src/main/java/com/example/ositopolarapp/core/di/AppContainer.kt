package com.example.ositopolarapp.core.di

import com.example.ositopolarapp.core.data.network.RetrofitClient
import com.example.ositopolarapp.features.authentication.data.repository.AuthRepositoryImpl
import com.example.ositopolarapp.features.authentication.domain.repository.AuthRepository
import com.example.ositopolarapp.features.authentication.domain.usecase.CreateRegistrationCheckoutUseCase
import com.example.ositopolarapp.features.authentication.domain.usecase.CompleteRegistrationUseCase
import com.example.ositopolarapp.features.authentication.domain.usecase.SignInUseCase

// TODO: Importa aquí tus futuros UseCases de Login
// import com.example.ositopolardefinitivo.feature.login.domain.usecase.SignInUseCase

/**
 * Contenedor de dependencias manual.
 * Construye y provee todas las instancias que la app necesita.
 */
class AppContainer {

    // --- 1. Capa DATA (API y Repos) ---
    private val authApiService = RetrofitClient.authApiService

    val authRepository: AuthRepository = AuthRepositoryImpl(authApiService)

    // --- 2. Capa DOMAIN (Use Cases) ---

    // Use Cases de Registro
    val createRegistrationCheckoutUseCase = CreateRegistrationCheckoutUseCase(authRepository)
    val completeRegistrationUseCase = CompleteRegistrationUseCase(authRepository)
    val signInUseCase = SignInUseCase(authRepository)
    // TODO: Cuando crees el SignInUseCase, añádelo aquí
    // val signInUseCase = SignInUseCase(authRepository)

}