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

/**
 * UI state for Equipment List screen.
 */
data class EquipmentListUiState(
    val isLoading: Boolean = false,
    val equipmentList: List<Equipment> = emptyList(),
    val filteredEquipmentList: List<Equipment> = emptyList(),
    val error: String? = null,
    val searchQuery: String = "",
    val selectedStatus: String? = null,
    val deleteSuccess: Boolean = false,
    val deletingEquipmentId: Int? = null,
    val createSuccess: Boolean = false,
    val isCreating: Boolean = false
)

/**
 * ViewModel for Equipment List screen.
 * Handles fetching, filtering, creating, and deleting equipment.
 */
class EquipmentListViewModel(
    private val getAllEquipmentsUseCase: GetAllEquipmentsUseCase,
    private val createEquipmentUseCase: CreateEquipmentUseCase,
    private val deleteEquipmentUseCase: DeleteEquipmentUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(EquipmentListUiState())
    val uiState: StateFlow<EquipmentListUiState> = _uiState.asStateFlow()

    init {
        loadEquipment()
    }

    /**
     * Loads all equipment from the repository.
     */
    fun loadEquipment() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            getAllEquipmentsUseCase()
                .onSuccess { equipmentList ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            equipmentList = equipmentList,
                            filteredEquipmentList = filterEquipment(equipmentList, it.searchQuery, it.selectedStatus),
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
     * Updates the search query and filters the equipment list.
     */
    fun updateSearchQuery(query: String) {
        _uiState.update {
            it.copy(
                searchQuery = query,
                filteredEquipmentList = filterEquipment(it.equipmentList, query, it.selectedStatus)
            )
        }
    }

    /**
     * Updates the status filter and filters the equipment list.
     */
    fun updateStatusFilter(status: String?) {
        _uiState.update {
            it.copy(
                selectedStatus = status,
                filteredEquipmentList = filterEquipment(it.equipmentList, it.searchQuery, status)
            )
        }
    }

    /**
     * Filters equipment list based on search query and status.
     */
    private fun filterEquipment(
        equipmentList: List<Equipment>,
        searchQuery: String,
        selectedStatus: String?
    ): List<Equipment> {
        var filtered = equipmentList

        // Filter by search query
        if (searchQuery.isNotBlank()) {
            filtered = filtered.filter {
                it.name.contains(searchQuery, ignoreCase = true) ||
                it.manufacturer.contains(searchQuery, ignoreCase = true) ||
                it.model.contains(searchQuery, ignoreCase = true)
            }
        }

        // Filter by status
        if (selectedStatus != null) {
            filtered = filtered.filter {
                it.status.name.equals(selectedStatus, ignoreCase = true)
            }
        }

        return filtered
    }

    /**
     * Deletes an equipment by ID.
     */
    fun deleteEquipment(equipmentId: Int) {
        viewModelScope.launch {
            _uiState.update { it.copy(deletingEquipmentId = equipmentId, error = null) }

            deleteEquipmentUseCase(equipmentId)
                .onSuccess {
                    // Remove equipment from list
                    val updatedList = _uiState.value.equipmentList.filter { it.id != equipmentId }
                    _uiState.update {
                        it.copy(
                            deletingEquipmentId = null,
                            equipmentList = updatedList,
                            filteredEquipmentList = filterEquipment(updatedList, it.searchQuery, it.selectedStatus),
                            deleteSuccess = true,
                            error = null
                        )
                    }
                }
                .onFailure { error ->
                    _uiState.update {
                        it.copy(
                            deletingEquipmentId = null,
                            error = error.message ?: "Failed to delete equipment"
                        )
                    }
                }
        }
    }

    /**
     * Creates a new equipment.
     */
    fun createEquipment(equipment: Equipment) {
        viewModelScope.launch {
            _uiState.update { it.copy(isCreating = true, error = null, createSuccess = false) }

            createEquipmentUseCase(equipment)
                .onSuccess { createdEquipment ->
                    // Add new equipment to list
                    val updatedList = _uiState.value.equipmentList + createdEquipment
                    _uiState.update {
                        it.copy(
                            isCreating = false,
                            equipmentList = updatedList,
                            filteredEquipmentList = filterEquipment(updatedList, it.searchQuery, it.selectedStatus),
                            createSuccess = true,
                            error = null
                        )
                    }
                }
                .onFailure { error ->
                    _uiState.update {
                        it.copy(
                            isCreating = false,
                            createSuccess = false,
                            error = error.message ?: "Failed to create equipment"
                        )
                    }
                }
        }
    }

    /**
     * Clears the create success flag.
     */
    fun clearCreateSuccess() {
        _uiState.update { it.copy(createSuccess = false) }
    }

    /**
     * Clears the delete success flag.
     */
    fun clearDeleteSuccess() {
        _uiState.update { it.copy(deleteSuccess = false) }
    }

    /**
     * Clears the error message.
     */
    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }
}
