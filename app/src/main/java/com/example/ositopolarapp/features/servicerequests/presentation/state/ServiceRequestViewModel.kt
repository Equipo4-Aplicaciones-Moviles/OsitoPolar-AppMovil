package com.example.ositopolarapp.features.servicerequests.presentation.state

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ositopolarapp.features.servicerequests.domain.model.ServiceRequest
import com.example.ositopolarapp.features.servicerequests.domain.usecase.AddFeedbackUseCase
import com.example.ositopolarapp.features.servicerequests.domain.usecase.CreateServiceRequestUseCase
import com.example.ositopolarapp.features.servicerequests.domain.usecase.GetAllServiceRequestsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * Service Request ViewModel
 *
 * Manages UI state for service request screens.
 */
class ServiceRequestViewModel(
    private val getAllServiceRequestsUseCase: GetAllServiceRequestsUseCase,
    private val createServiceRequestUseCase: CreateServiceRequestUseCase,
    private val addFeedbackUseCase: AddFeedbackUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ServiceRequestUiState())
    val uiState: StateFlow<ServiceRequestUiState> = _uiState.asStateFlow()

    init {
        loadServiceRequests()
    }

    fun loadServiceRequests() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            getAllServiceRequestsUseCase()
                .onSuccess { serviceRequests ->
                    _uiState.update {
                        it.copy(
                            serviceRequests = serviceRequests,
                            isLoading = false,
                            error = null
                        )
                    }
                }
                .onFailure { exception ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = exception.message ?: "Error al cargar solicitudes"
                        )
                    }
                }
        }
    }

    fun createServiceRequest(
        title: String,
        description: String,
        issueDetails: String,
        equipmentId: Int,
        reportedByUserId: Int,
        serviceType: String,
        priority: String,
        urgency: String,
        isEmergency: Boolean,
        scheduledDate: String?,
        timeSlot: String?,
        serviceAddress: String
    ) {
        viewModelScope.launch {
            _uiState.update { it.copy(isCreating = true, error = null) }

            // Generate order number (simple timestamp-based)
            val orderNumber = "SR-${System.currentTimeMillis()}"

            createServiceRequestUseCase(
                orderNumber = orderNumber,
                title = title,
                description = description,
                issueDetails = issueDetails,
                equipmentId = equipmentId,
                reportedByUserId = reportedByUserId,
                serviceType = serviceType,
                priority = priority,
                urgency = urgency,
                isEmergency = isEmergency,
                scheduledDate = scheduledDate,
                timeSlot = timeSlot,
                serviceAddress = serviceAddress
            )
                .onSuccess { newRequest ->
                    _uiState.update {
                        it.copy(
                            serviceRequests = it.serviceRequests + newRequest,
                            isCreating = false,
                            createSuccess = true,
                            error = null
                        )
                    }
                }
                .onFailure { exception ->
                    _uiState.update {
                        it.copy(
                            isCreating = false,
                            error = exception.message ?: "Error al crear solicitud"
                        )
                    }
                }
        }
    }

    fun addFeedback(serviceRequestId: Int, rating: Int) {
        viewModelScope.launch {
            _uiState.update { it.copy(isUpdating = true, error = null) }

            addFeedbackUseCase(serviceRequestId, rating)
                .onSuccess { updatedRequest ->
                    _uiState.update { state ->
                        val updatedList = state.serviceRequests.map { request ->
                            if (request.id == serviceRequestId) updatedRequest else request
                        }
                        state.copy(
                            serviceRequests = updatedList,
                            isUpdating = false,
                            error = null
                        )
                    }
                }
                .onFailure { exception ->
                    _uiState.update {
                        it.copy(
                            isUpdating = false,
                            error = exception.message ?: "Error al agregar calificación"
                        )
                    }
                }
        }
    }

    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }

    fun clearCreateSuccess() {
        _uiState.update { it.copy(createSuccess = false) }
    }
}

/**
 * UI State for Service Requests
 */
data class ServiceRequestUiState(
    val serviceRequests: List<ServiceRequest> = emptyList(),
    val isLoading: Boolean = false,
    val isCreating: Boolean = false,
    val isUpdating: Boolean = false,
    val createSuccess: Boolean = false,
    val error: String? = null
)
