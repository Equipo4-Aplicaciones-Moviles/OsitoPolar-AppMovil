package com.example.ositopolarapp.features.equipment.presentation.state

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ositopolarapp.features.equipment.domain.model.Equipment
import com.example.ositopolarapp.features.equipment.domain.usecase.DeleteEquipmentUseCase
import com.example.ositopolarapp.features.equipment.domain.usecase.GetEquipmentByIdUseCase
import com.example.ositopolarapp.features.equipment.domain.usecase.UpdateEquipmentOperationsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class EquipmentDetailUiState(
    val isLoading: Boolean = false,
    val equipment: Equipment? = null,
    val error: String? = null
)

class EquipmentDetailViewModel(
    private val getEquipmentByIdUseCase: GetEquipmentByIdUseCase,
    private val updateEquipmentOperationsUseCase: UpdateEquipmentOperationsUseCase,
    private val deleteEquipmentUseCase: DeleteEquipmentUseCase? = null
) : ViewModel() {

    private val _uiState = MutableStateFlow(EquipmentDetailUiState())
    val uiState: StateFlow<EquipmentDetailUiState> = _uiState.asStateFlow()

    fun loadEquipment(id: Int) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            getEquipmentByIdUseCase(id)
                .onSuccess { eq ->
                    _uiState.update { it.copy(isLoading = false, equipment = eq) }
                }
                .onFailure { error ->
                    _uiState.update { it.copy(isLoading = false, error = error.message) }
                }
        }
    }

    fun updateStatus(newStatus: String) {
        val currentEq = _uiState.value.equipment ?: return
        viewModelScope.launch {
            // Mantenemos la temperatura actual, solo cambiamos el estado
            updateEquipmentOperationsUseCase(currentEq.id, newStatus, currentEq.temperature)
                .onSuccess { updatedEq ->
                    _uiState.update { it.copy(equipment = updatedEq) }
                }
                .onFailure { error ->
                    _uiState.update { it.copy(error = error.message) }
                }
        }
    }

    // Función simple para cambiar temperatura
    fun updateTemperature(newTemp: Double) {
        val currentEq = _uiState.value.equipment ?: return
        viewModelScope.launch {
            updateEquipmentOperationsUseCase(currentEq.id, currentEq.status, newTemp)
                .onSuccess { updatedEq ->
                    _uiState.update { it.copy(equipment = updatedEq) }
                }
        }
    }

    fun deleteEquipment() {
        val currentEq = _uiState.value.equipment ?: return
        if (deleteEquipmentUseCase != null) {
            viewModelScope.launch {
                deleteEquipmentUseCase(currentEq.id)
                // Aquí la UI debería escuchar y navegar atrás
            }
        }
    }
}