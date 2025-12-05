package com.example.ositopolarapp.features.servicemarketplace.presentation.state

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ositopolarapp.features.servicemarketplace.domain.model.MarketplaceServiceRequest
import com.example.ositopolarapp.features.servicemarketplace.domain.usecase.AcceptServiceRequestUseCase
import com.example.ositopolarapp.features.servicemarketplace.domain.usecase.GetMarketplaceRequestsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class ServiceMarketplaceUiState(
    val isLoading: Boolean = false,
    val requests: List<MarketplaceServiceRequest> = emptyList(),
    val error: String? = null,
    val acceptingRequestId: Int? = null,
    val acceptSuccess: Boolean = false,
    val acceptMessage: String? = null
)

class ServiceMarketplaceViewModel(
    private val getMarketplaceRequestsUseCase: GetMarketplaceRequestsUseCase,
    private val acceptServiceRequestUseCase: AcceptServiceRequestUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ServiceMarketplaceUiState())
    val uiState: StateFlow<ServiceMarketplaceUiState> = _uiState.asStateFlow()

    init {
        loadMarketplaceRequests()
    }

    fun loadMarketplaceRequests() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            getMarketplaceRequestsUseCase()
                .onSuccess { requests ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            requests = requests
                        )
                    }
                }
                .onFailure { error ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = error.message ?: "Error al cargar solicitudes"
                        )
                    }
                }
        }
    }

    fun acceptRequest(serviceRequestId: Int) {
        viewModelScope.launch {
            _uiState.update { it.copy(acceptingRequestId = serviceRequestId) }

            acceptServiceRequestUseCase(serviceRequestId)
                .onSuccess { result ->
                    _uiState.update {
                        it.copy(
                            acceptingRequestId = null,
                            acceptSuccess = true,
                            acceptMessage = result.message
                        )
                    }
                    // Reload requests after accepting
                    loadMarketplaceRequests()
                }
                .onFailure { error ->
                    _uiState.update {
                        it.copy(
                            acceptingRequestId = null,
                            error = error.message ?: "Error al aceptar solicitud"
                        )
                    }
                }
        }
    }

    fun clearAcceptMessage() {
        _uiState.update { it.copy(acceptSuccess = false, acceptMessage = null) }
    }

    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }
}
