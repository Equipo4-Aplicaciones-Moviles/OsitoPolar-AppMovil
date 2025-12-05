package com.example.ositopolarapp.features.equipment.domain.usecase

import com.example.ositopolarapp.features.equipment.domain.repository.EquipmentRepository

/**
 * Use case for retrieving a single equipment by its ID.
 */
class GetEquipmentByIdUseCase(
    private val repository: EquipmentRepository
) {
    suspend operator fun invoke(equipmentId: Int) = repository.getEquipmentById(equipmentId)
}
