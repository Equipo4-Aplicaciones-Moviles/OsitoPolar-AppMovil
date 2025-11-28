package com.example.ositopolarapp.core.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.ositopolarapp.features.servicerequests.domain.usecase.AddFeedbackUseCase
import com.example.ositopolarapp.features.servicerequests.domain.usecase.CreateServiceRequestUseCase
import com.example.ositopolarapp.features.servicerequests.domain.usecase.GetAllServiceRequestsUseCase
import com.example.ositopolarapp.features.servicerequests.presentation.state.ServiceRequestViewModel

class ServiceRequestViewModelFactory(
    private val appContainer: AppContainer
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ServiceRequestViewModel::class.java)) {
            return ServiceRequestViewModel(
                createServiceRequestUseCase = CreateServiceRequestUseCase(appContainer.serviceRequestRepository),
                getAllServiceRequestsUseCase = GetAllServiceRequestsUseCase(appContainer.serviceRequestRepository),
                addFeedbackUseCase = AddFeedbackUseCase(appContainer.serviceRequestRepository)
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}