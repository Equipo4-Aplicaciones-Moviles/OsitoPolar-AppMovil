package com.example.ositopolarapp.features.equipment.domain.usecase

import com.example.ositopolarapp.features.equipment.domain.repository.EquipmentRepository

/**
 * Use case for retrieving all equipment owned by the current user.
 */
class GetAllEquipmentsUseCase(
    private val repository: EquipmentRepository
) {
    suspend operator fun invoke() = repository.getAllEquipment()
}
