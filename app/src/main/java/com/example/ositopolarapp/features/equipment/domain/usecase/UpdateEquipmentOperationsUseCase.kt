package com.example.ositopolarapp.features.equipment.domain.usecase

import com.example.ositopolarapp.features.equipment.domain.repository.EquipmentRepository
import com.example.ositopolarapp.features.equipment.domain.repository.LocationUpdate

/**
 * Use case for updating equipment operations (temperature, power, location).
 */
class UpdateEquipmentOperationsUseCase(
    private val repository: EquipmentRepository
) {
    suspend operator fun invoke(
        equipmentId: Int,
        temperature: Double? = null,
        powerState: String? = null,
        location: LocationUpdate? = null
    ) = repository.updateEquipmentOperations(equipmentId, temperature, powerState, location)
}
