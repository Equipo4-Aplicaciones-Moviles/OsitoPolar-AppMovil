package com.example.ositopolarapp.features.rentals.domain.usecase

import com.example.ositopolarapp.features.rentals.domain.model.RentalEquipment
import com.example.ositopolarapp.features.rentals.domain.repository.RentalEquipmentRepository
import com.example.ositopolarapp.features.rentals.data.mapper.toDomain

class GetRentalEquipmentByIdUseCase(
    private val repository: RentalEquipmentRepository
) {
    suspend operator fun invoke(equipmentId: Int): Result<RentalEquipment> {
        return repository.getRentalEquipmentById(equipmentId)
            .mapCatching { dto ->
                dto.toDomain()
            }
    }
}
