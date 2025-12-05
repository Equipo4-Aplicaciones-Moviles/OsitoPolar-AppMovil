package com.example.ositopolarapp.core.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.ositopolarapp.features.servicerequests.presentation.state.ServiceRequestViewModel

/**
 * ViewModelFactory for ServiceRequestViewModel
 */
class ServiceRequestViewModelFactory(
    private val appContainer: AppContainer
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ServiceRequestViewModel::class.java)) {
            return ServiceRequestViewModel(
                getAllServiceRequestsUseCase = appContainer.getAllServiceRequestsUseCase,
                createServiceRequestUseCase = appContainer.createServiceRequestUseCase,
                addFeedbackUseCase = appContainer.addFeedbackUseCase
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}
