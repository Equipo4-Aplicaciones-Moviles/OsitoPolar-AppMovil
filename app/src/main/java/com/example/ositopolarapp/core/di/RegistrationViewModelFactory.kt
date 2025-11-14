package com.example.ositopolarapp.core.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.ositopolarapp.features.authentication.presentation.state.RegistrationViewModel

class RegistrationViewModelFactory(
    private val container: AppContainer
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        if (modelClass.isAssignableFrom(RegistrationViewModel::class.java)) {
            return RegistrationViewModel(
                createRegistrationCheckoutUseCase = container.createRegistrationCheckoutUseCase,
                completeRegistrationUseCase = container.completeRegistrationUseCase,
                prefs = container.sharedPreferences
            ) as T
        }

        throw IllegalArgumentException("Unknown ViewModel: ${modelClass.name}")
    }
}
