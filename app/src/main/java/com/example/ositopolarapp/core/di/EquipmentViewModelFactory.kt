package com.example.ositopolarapp.core.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.ositopolarapp.features.equipment.domain.usecase.CreateEquipmentUseCase
import com.example.ositopolarapp.features.equipment.domain.usecase.DeleteEquipmentUseCase
import com.example.ositopolarapp.features.equipment.domain.usecase.GetAllEquipmentsUseCase
import com.example.ositopolarapp.features.equipment.domain.usecase.GetEquipmentByIdUseCase
import com.example.ositopolarapp.features.equipment.domain.usecase.UpdateEquipmentOperationsUseCase
import com.example.ositopolarapp.features.equipment.presentation.state.EquipmentDetailViewModel
import com.example.ositopolarapp.features.equipment.presentation.state.EquipmentListViewModel

class EquipmentViewModelFactory(
    private val appContainer: AppContainer
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        // 1. CORRECCIÓN: Agregamos Create y Delete aquí (porque el error dice que faltan)
        if (modelClass.isAssignableFrom(EquipmentListViewModel::class.java)) {
            return EquipmentListViewModel(
                getAllEquipmentsUseCase = GetAllEquipmentsUseCase(appContainer.equipmentRepository),
                createEquipmentUseCase = CreateEquipmentUseCase(appContainer.equipmentRepository),
                deleteEquipmentUseCase = DeleteEquipmentUseCase(appContainer.equipmentRepository)
            ) as T
        }

        // 2. CORRECCIÓN: Quitamos 'delete' de aquí si te daba error "No parameter found"
        if (modelClass.isAssignableFrom(EquipmentDetailViewModel::class.java)) {
            return EquipmentDetailViewModel(
                getEquipmentByIdUseCase = GetEquipmentByIdUseCase(appContainer.equipmentRepository),
                updateEquipmentOperationsUseCase = UpdateEquipmentOperationsUseCase(appContainer.equipmentRepository)
                // Si el compilador te pide 'delete' aquí también, descomenta la siguiente línea:
                // , deleteEquipmentUseCase = DeleteEquipmentUseCase(appContainer.equipmentRepository)
            ) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}