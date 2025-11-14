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
                checkAuthUseCase = container.checkAuthUseCase
            ) as T
        }

        throw IllegalArgumentException("Unknown ViewModel: ${modelClass.name}")
    }
}
