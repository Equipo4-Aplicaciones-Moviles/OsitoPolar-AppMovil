package com.example.ositopolarapp.core.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.ositopolarapp.features.rentals.presentation.viewmodel.RentalCatalogViewModel

/**
 * ViewModelFactory for Rental module.
 * Handles creation of RentalCatalogViewModel with all required dependencies
 * including PaymentApiService and SharedPreferences for rental completion.
 */
class RentalViewModelFactory(
    private val container: AppContainer
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(RentalCatalogViewModel::class.java) -> {
                RentalCatalogViewModel(
                    getRentalEquipmentUseCase = container.getRentalEquipmentUseCase,
                    createRentalRequestUseCase = container.createRentalRequestUseCase,
                    paymentApiService = container.paymentApiService,
                    sharedPreferences = container.sharedPreferences
                ) as T
            }

            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }
}
