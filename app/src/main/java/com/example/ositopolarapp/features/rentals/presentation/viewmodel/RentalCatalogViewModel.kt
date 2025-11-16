package com.example.ositopolarapp.features.rentals.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ositopolarapp.features.rentals.domain.model.RentalEquipment
import com.example.ositopolarapp.features.rentals.domain.usecase.CreateRentalRequestUseCase
import com.example.ositopolarapp.features.rentals.domain.usecase.GetRentalEquipmentUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * State for Rental Catalog Screen
 */
data class RentalCatalogUiState(
    val isLoading: Boolean = false,
    val equipment: List<RentalEquipment> = emptyList(),
    val error: String? = null,
    val checkoutUrl: String? = null,
    val selectedEquipment: RentalEquipment? = null
)

/**
 * ViewModel for Rental Catalog Screen
 */
class RentalCatalogViewModel(
    private val getRentalEquipmentUseCase: GetRentalEquipmentUseCase,
    private val createRentalRequestUseCase: CreateRentalRequestUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(RentalCatalogUiState())
    val uiState: StateFlow<RentalCatalogUiState> = _uiState.asStateFlow()

    init {
        loadRentalEquipment()
    }

    fun loadRentalEquipment() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            getRentalEquipmentUseCase()
                .onSuccess { equipmentList ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            equipment = equipmentList,
                            error = null
                        )
                    }
                }
                .onFailure { exception ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = exception.message ?: "Error al cargar equipos"
                        )
                    }
                }
        }
    }

    fun selectEquipment(equipment: RentalEquipment) {
        _uiState.update { it.copy(selectedEquipment = equipment) }
    }

    fun createRentalRequest(equipmentId: Int, months: Int) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            createRentalRequestUseCase(
                equipmentId = equipmentId,
                months = months,
                successUrl = "ositopolar://rental/success",
                cancelUrl = "ositopolar://rental/cancel"
            )
                .onSuccess { response ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            checkoutUrl = response.checkoutUrl,
                            error = null
                        )
                    }
                }
                .onFailure { exception ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = exception.message ?: "Error al crear solicitud de renta"
                        )
                    }
                }
        }
    }

    fun clearCheckoutUrl() {
        _uiState.update { it.copy(checkoutUrl = null) }
    }

    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }
}
