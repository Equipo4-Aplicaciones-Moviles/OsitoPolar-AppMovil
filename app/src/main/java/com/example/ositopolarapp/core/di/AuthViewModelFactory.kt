package com.example.ositopolarapp.core.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.ositopolarapp.features.authentication.presentation.state.RegistrationViewModel
// TODO: Importa tu LoginViewModel cuando lo crees
import com.example.ositopolarapp.features.authentication.presentation.state.LoginViewModel

/**
 * Esta Factory sabe cómo construir todos los ViewModels
 * relacionados con la autenticación (Login, Register).
 */
class AuthViewModelFactory(
    private val container: AppContainer
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        // --- Cuando se pida un RegistrationViewModel ---
        if (modelClass.isAssignableFrom(RegistrationViewModel::class.java)) {
            return RegistrationViewModel(
                createRegistrationCheckoutUseCase = container.createRegistrationCheckoutUseCase,
                completeRegistrationUseCase = container.completeRegistrationUseCase
            ) as T
        }

        // TODO: Cuando crees el LoginViewModel, añade la lógica aquí

        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel(
                signInUseCase = container.signInUseCase
            ) as T
        }


        // Si se pide un ViewModel desconocido, lanza un error
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}