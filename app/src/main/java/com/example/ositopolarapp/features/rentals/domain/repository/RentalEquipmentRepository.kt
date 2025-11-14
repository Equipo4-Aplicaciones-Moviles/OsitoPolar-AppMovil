package com.example.ositopolarapp.features.rentals.domain.repository

import com.example.ositopolarapp.features.rentals.data.dto.RentalEquipmentDto

interface RentalEquipmentRepository {
    suspend fun getAllRentalEquipment(): Result<List<RentalEquipmentDto>>
    suspend fun getRentalEquipmentById(equipmentId: Int): Result<RentalEquipmentDto>
}
