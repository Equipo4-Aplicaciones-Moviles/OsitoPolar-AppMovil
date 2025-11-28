package com.example.ositopolarapp.core.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.ositopolarapp.features.subscriptions.domain.usecase.GetAllPlansUseCase
import com.example.ositopolarapp.features.subscriptions.presentation.state.PlansViewModel

class PlansViewModelFactory(
    private val appContainer: AppContainer
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PlansViewModel::class.java)) {
            return PlansViewModel(
                getAllPlansUseCase = GetAllPlansUseCase(appContainer.subscriptionRepository)
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}