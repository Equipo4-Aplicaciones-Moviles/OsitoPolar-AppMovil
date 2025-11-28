package com.example.ositopolarapp.features.equipment.presentation.state

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ositopolarapp.features.equipment.domain.model.Equipment
import com.example.ositopolarapp.features.equipment.domain.usecase.CreateEquipmentUseCase
import com.example.ositopolarapp.features.equipment.domain.usecase.DeleteEquipmentUseCase
import com.example.ositopolarapp.features.equipment.domain.usecase.GetAllEquipmentsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class EquipmentListUiState(
    val isLoading: Boolean = false,
    val equipmentList: List<Equipment> = emptyList(),
    val error: String? = null
)

class EquipmentListViewModel(
    private val getAllEquipmentsUseCase: GetAllEquipmentsUseCase,
    private val createEquipmentUseCase: CreateEquipmentUseCase,
    private val deleteEquipmentUseCase: DeleteEquipmentUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(EquipmentListUiState())
    val uiState: StateFlow<EquipmentListUiState> = _uiState.asStateFlow()

    init {
        loadEquipments()
    }

    fun loadEquipments() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            getAllEquipmentsUseCase()
                .onSuccess { list ->
                    _uiState.update { it.copy(isLoading = false, equipmentList = list) }
                }
                .onFailure { error ->
                    _uiState.update { it.copy(isLoading = false, error = error.message) }
                }
        }
    }

    // --- ESTA ES LA FUNCIÓN QUE FALTABA ---
    fun addEquipment(
        ownerId: Int,
        name: String,
        type: String,
        model: String,
        serialNumber: String,
        brand: String,
        location: String,
        address: String,
        latitude: Double,
        longitude: Double
    ) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            createEquipmentUseCase(
                ownerId, name, type, model, serialNumber, brand, location, address, latitude, longitude
            ).onSuccess {
                // Si se crea con éxito, recargamos la lista
                loadEquipments()
            }.onFailure { error ->
                _uiState.update { it.copy(isLoading = false, error = error.message) }
            }
        }
    }

    fun deleteEquipment(id: Int) {
        viewModelScope.launch {
            deleteEquipmentUseCase(id).onSuccess { loadEquipments() }
        }
    }

    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }
}