package com.example.ositopolarapp.core.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.ositopolarapp.features.authentication.presentation.state.MainViewModel

class MainViewModelFactory(
    private val container: AppContainer
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(
                checkAuthUseCase = container.checkAuthUseCase,
                getCurrentUserUseCase = container.getCurrentUserUseCase,
                logoutUseCase = container.logoutUseCase,
                enable2FAUseCase = container.enable2FAUseCase,
                disable2FAUseCase = container.disable2FAUseCase
            ) as T
        }

        throw IllegalArgumentException("Unknown ViewModel: ${modelClass.name}")
    }
}
