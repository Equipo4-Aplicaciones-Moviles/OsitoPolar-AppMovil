package com.example.ositopolarapp.core.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.ositopolarapp.features.servicemarketplace.presentation.state.ServiceMarketplaceViewModel

class ServiceMarketplaceViewModelFactory(
    private val appContainer: AppContainer
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ServiceMarketplaceViewModel::class.java)) {
            return ServiceMarketplaceViewModel(
                getMarketplaceRequestsUseCase = appContainer.getMarketplaceRequestsUseCase,
                acceptServiceRequestUseCase = appContainer.acceptServiceRequestUseCase
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}
