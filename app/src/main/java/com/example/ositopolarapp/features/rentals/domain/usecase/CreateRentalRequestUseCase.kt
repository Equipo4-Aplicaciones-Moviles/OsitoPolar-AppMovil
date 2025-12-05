package com.example.ositopolarapp.features.rentals.domain.usecase

import com.example.ositopolarapp.features.rentals.data.api.CreateRentalResponseDto
import com.example.ositopolarapp.features.rentals.domain.repository.RentalEquipmentRepository

/**
 * Use case to create a rental request and get Stripe checkout URL
 */
class CreateRentalRequestUseCase(
    private val repository: RentalEquipmentRepository
) {
    suspend operator fun invoke(
        equipmentId: Int,
        months: Int,
        successUrl: String? = null,
        cancelUrl: String? = null
    ): Result<CreateRentalResponseDto> {
        return repository.createRentalRequest(
            equipmentId = equipmentId,
            months = months,
            successUrl = successUrl,
            cancelUrl = cancelUrl
        )
    }
}
