package com.example.ositopolarapp.features.rentals.domain.repository

import com.example.ositopolarapp.features.rentals.data.api.CreateRentalResponseDto
import com.example.ositopolarapp.features.rentals.data.dto.RentalEquipmentDto

interface RentalEquipmentRepository {
    suspend fun getAllRentalEquipment(): Result<List<RentalEquipmentDto>>
    suspend fun getRentalEquipmentById(equipmentId: Int): Result<RentalEquipmentDto>
    suspend fun createRentalRequest(
        equipmentId: Int,
        months: Int,
        successUrl: String?,
        cancelUrl: String?
    ): Result<CreateRentalResponseDto>
}
