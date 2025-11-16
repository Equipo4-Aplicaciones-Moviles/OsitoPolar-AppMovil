package com.example.ositopolarapp.features.rentals.domain.usecase

import com.example.ositopolarapp.features.rentals.data.mapper.toDomain
import com.example.ositopolarapp.features.rentals.domain.model.RentalEquipment
import com.example.ositopolarapp.features.rentals.domain.repository.RentalEquipmentRepository

/**
 * Use case to get all available rental equipment from providers
 */
class GetRentalEquipmentUseCase(
    private val repository: RentalEquipmentRepository
) {
    suspend operator fun invoke(): Result<List<RentalEquipment>> {
        return repository.getAllRentalEquipment()
            .mapCatching { dtoList ->
                dtoList.map { it.toDomain() }
            }
    }
}
