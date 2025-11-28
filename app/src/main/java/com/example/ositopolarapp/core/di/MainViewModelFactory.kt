package com.example.ositopolarapp.core.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.ositopolarapp.features.authentication.domain.usecase.CheckAuthUseCase
import com.example.ositopolarapp.features.authentication.domain.usecase.Disable2FAUseCase
import com.example.ositopolarapp.features.authentication.domain.usecase.Enable2FAUseCase
import com.example.ositopolarapp.features.authentication.domain.usecase.GetCurrentUserUseCase
import com.example.ositopolarapp.features.authentication.domain.usecase.LogoutUseCase
import com.example.ositopolarapp.features.authentication.presentation.state.MainViewModel

class MainViewModelFactory(
    private val appContainer: AppContainer
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(
                checkAuthUseCase = CheckAuthUseCase(appContainer.authRepository),
                logoutUseCase = LogoutUseCase(appContainer.authRepository),
                getCurrentUserUseCase = GetCurrentUserUseCase(appContainer.authRepository),
                enable2FAUseCase = Enable2FAUseCase(appContainer.authRepository),
                disable2FAUseCase = Disable2FAUseCase(appContainer.authRepository)
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}