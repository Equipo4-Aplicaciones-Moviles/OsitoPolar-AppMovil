package com.example.ositopolarapp.features.equipment.domain.usecase

import com.example.ositopolarapp.features.equipment.domain.repository.EquipmentRepository

/**
 * Use case for deleting equipment.
 */
class DeleteEquipmentUseCase(
    private val repository: EquipmentRepository
) {
    suspend operator fun invoke(equipmentId: Int) = repository.deleteEquipment(equipmentId)
}
