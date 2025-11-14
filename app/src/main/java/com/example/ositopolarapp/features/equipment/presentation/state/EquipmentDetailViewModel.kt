package com.example.ositopolarapp.features.equipment.presentation.state

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ositopolarapp.features.equipment.domain.model.Equipment
import com.example.ositopolarapp.features.equipment.domain.repository.LocationUpdate
import com.example.ositopolarapp.features.equipment.domain.usecase.GetEquipmentByIdUseCase
import com.example.ositopolarapp.features.equipment.domain.usecase.UpdateEquipmentOperationsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * UI state for Equipment Detail screen.
 */
data class EquipmentDetailUiState(
    val isLoading: Boolean = false,
    val equipment: Equipment? = null,
    val error: String? = null,
    val isUpdatingOperations: Boolean = false,
    val operationsUpdateSuccess: Boolean = false,
    val operationsUpdateError: String? = null
)

/**
 * ViewModel for Equipment Detail screen.
 * Handles fetching equipment details and updating operations (temperature, power, location).
 */
class EquipmentDetailViewModel(
    private val getEquipmentByIdUseCase: GetEquipmentByIdUseCase,
    private val updateEquipmentOperationsUseCase: UpdateEquipmentOperationsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(EquipmentDetailUiState())
    val uiState: StateFlow<EquipmentDetailUiState> = _uiState.asStateFlow()

    /**
     * Loads equipment details by ID.
     */
    fun loadEquipment(equipmentId: Int) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            getEquipmentByIdUseCase(equipmentId)
                .onSuccess { equipment ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            equipment = equipment,
                            error = null
                        )
                    }
                }
                .onFailure { error ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = error.message ?: "Failed to load equipment"
                        )
                    }
                }
        }
    }

    /**
     * Updates the equipment temperature.
     */
    fun updateTemperature(equipmentId: Int, temperature: Double) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isUpdatingOperations = true,
                    operationsUpdateError = null,
                    operationsUpdateSuccess = false
                )
            }

            updateEquipmentOperationsUseCase(
                equipmentId = equipmentId,
                temperature = temperature
            )
                .onSuccess { updatedEquipment ->
                    _uiState.update {
                        it.copy(
                            isUpdatingOperations = false,
                            equipment = updatedEquipment,
                            operationsUpdateSuccess = true,
                            operationsUpdateError = null
                        )
                    }
                }
                .onFailure { error ->
                    _uiState.update {
                        it.copy(
                            isUpdatingOperations = false,
                            operationsUpdateError = error.message ?: "Failed to update temperature"
                        )
                    }
                }
        }
    }

    /**
     * Toggles the equipment power state.
     */
    fun togglePower(equipmentId: Int, isPoweredOn: Boolean) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isUpdatingOperations = true,
                    operationsUpdateError = null,
                    operationsUpdateSuccess = false
                )
            }

            val powerState = if (isPoweredOn) "ON" else "OFF"

            updateEquipmentOperationsUseCase(
                equipmentId = equipmentId,
                powerState = powerState
            )
                .onSuccess { updatedEquipment ->
                    _uiState.update {
                        it.copy(
                            isUpdatingOperations = false,
                            equipment = updatedEquipment,
                            operationsUpdateSuccess = true,
                            operationsUpdateError = null
                        )
                    }
                }
                .onFailure { error ->
                    _uiState.update {
                        it.copy(
                            isUpdatingOperations = false,
                            operationsUpdateError = error.message ?: "Failed to toggle power"
                        )
                    }
                }
        }
    }

    /**
     * Updates the equipment location.
     */
    fun updateLocation(equipmentId: Int, address: String, latitude: Double, longitude: Double) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isUpdatingOperations = true,
                    operationsUpdateError = null,
                    operationsUpdateSuccess = false
                )
            }

            val location = LocationUpdate(
                address = address,
                latitude = latitude,
                longitude = longitude
            )

            updateEquipmentOperationsUseCase(
                equipmentId = equipmentId,
                location = location
            )
                .onSuccess { updatedEquipment ->
                    _uiState.update {
                        it.copy(
                            isUpdatingOperations = false,
                            equipment = updatedEquipment,
                            operationsUpdateSuccess = true,
                            operationsUpdateError = null
                        )
                    }
                }
                .onFailure { error ->
                    _uiState.update {
                        it.copy(
                            isUpdatingOperations = false,
                            operationsUpdateError = error.message ?: "Failed to update location"
                        )
                    }
                }
        }
    }

    /**
     * Clears the operations update success flag.
     */
    fun clearOperationsUpdateSuccess() {
        _uiState.update { it.copy(operationsUpdateSuccess = false) }
    }

    /**
     * Clears the operations update error.
     */
    fun clearOperationsUpdateError() {
        _uiState.update { it.copy(operationsUpdateError = null) }
    }

    /**
     * Clears the error message.
     */
    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }

    /**
     * Reloads the equipment data.
     */
    fun reload() {
        _uiState.value.equipment?.let {
            loadEquipment(it.id)
        }
    }
}
