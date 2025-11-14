package com.example.ositopolarapp.core.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.ositopolarapp.features.equipment.presentation.state.EquipmentDetailViewModel
import com.example.ositopolarapp.features.equipment.presentation.state.EquipmentListViewModel

/**
 * ViewModelFactory for Equipment module.
 * Handles creation of EquipmentListViewModel and EquipmentDetailViewModel.
 */
class EquipmentViewModelFactory(
    private val container: AppContainer
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(EquipmentListViewModel::class.java) -> {
                EquipmentListViewModel(
                    getAllEquipmentsUseCase = container.getAllEquipmentsUseCase,
                    createEquipmentUseCase = container.createEquipmentUseCase,
                    deleteEquipmentUseCase = container.deleteEquipmentUseCase
                ) as T
            }

            modelClass.isAssignableFrom(EquipmentDetailViewModel::class.java) -> {
                EquipmentDetailViewModel(
                    getEquipmentByIdUseCase = container.getEquipmentByIdUseCase,
                    updateEquipmentOperationsUseCase = container.updateEquipmentOperationsUseCase
                ) as T
            }

            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }
}
