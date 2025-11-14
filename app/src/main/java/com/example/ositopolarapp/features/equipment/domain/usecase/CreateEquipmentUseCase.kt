package com.example.ositopolarapp.features.equipment.domain.usecase

import com.example.ositopolarapp.features.equipment.domain.model.Equipment
import com.example.ositopolarapp.features.equipment.domain.repository.EquipmentRepository

/**
 * Use case for creating new equipment.
 */
class CreateEquipmentUseCase(
    private val repository: EquipmentRepository
) {
    suspend operator fun invoke(equipment: Equipment) = repository.createEquipment(equipment)
}
