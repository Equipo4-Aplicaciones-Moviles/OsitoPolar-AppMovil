package com.example.ositopolarapp.features.equipment.domain.usecase

import com.example.ositopolarapp.features.equipment.domain.model.Equipment
import com.example.ositopolarapp.features.equipment.domain.repository.EquipmentRepository

class UpdateEquipmentOperationsUseCase(
    private val repository: EquipmentRepository
) {
    // Ya no pedimos 'LocationUpdate'. Solo lo que el repo sabe manejar.
    suspend operator fun invoke(
        id: Int,
        status: String,
        temperature: Double
    ): Result<Equipment> {
        return repository.updateOperations(id, status, temperature)
    }
}