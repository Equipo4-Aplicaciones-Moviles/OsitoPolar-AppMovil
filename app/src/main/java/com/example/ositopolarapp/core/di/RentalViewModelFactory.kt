package com.example.ositopolarapp.core.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.ositopolarapp.features.rentals.domain.usecase.CreateRentalRequestUseCase // Asegúrate de importar este UseCase
import com.example.ositopolarapp.features.rentals.domain.usecase.GetRentalEquipmentUseCase
import com.example.ositopolarapp.features.rentals.presentation.viewmodel.RentalCatalogViewModel

class RentalViewModelFactory(
    private val appContainer: AppContainer
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RentalCatalogViewModel::class.java)) {
            return RentalCatalogViewModel(
                getRentalEquipmentUseCase = GetRentalEquipmentUseCase(appContainer.rentalEquipmentRepository),
                // AGREGADO: El parámetro que faltaba
                createRentalRequestUseCase = CreateRentalRequestUseCase(appContainer.rentalEquipmentRepository)
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}