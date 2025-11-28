package com.example.ositopolarapp.core.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
// IMPORTANTE: Estos son los imports que faltaban o estaban mal
import com.example.ositopolarapp.features.authentication.domain.usecase.CompleteRegistrationUseCase
import com.example.ositopolarapp.features.authentication.domain.usecase.CreateRegistrationCheckoutUseCase
import com.example.ositopolarapp.features.authentication.domain.usecase.SignInUseCase
import com.example.ositopolarapp.features.authentication.domain.usecase.VerifyTwoFactorUseCase
import com.example.ositopolarapp.features.authentication.presentation.state.LoginViewModel
import com.example.ositopolarapp.features.authentication.presentation.state.RegistrationViewModel

class AuthViewModelFactory(
    private val appContainer: AppContainer
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        // Creador de LoginViewModel
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel(
                signInUseCase = SignInUseCase(appContainer.authRepository),
                verifyTwoFactorUseCase = VerifyTwoFactorUseCase(appContainer.authRepository)
            ) as T
        }

        // Creador de RegistrationViewModel
        if (modelClass.isAssignableFrom(RegistrationViewModel::class.java)) {
            return RegistrationViewModel(
                createRegistrationCheckoutUseCase = CreateRegistrationCheckoutUseCase(appContainer.authRepository),
                completeRegistrationUseCase = CompleteRegistrationUseCase(appContainer.authRepository),
                prefs = appContainer.sharedPreferences
            ) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}