package com.example.ositopolarapp.features.rentals.data.mapper

import com.example.ositopolarapp.features.rentals.data.dto.RentalEquipmentDto
import com.example.ositopolarapp.features.rentals.domain.model.RentalEquipment
import com.example.ositopolarapp.features.rentals.domain.model.Location

/**
 * Maps RentalEquipmentDto to RentalEquipment domain model
 */
fun RentalEquipmentDto.toDomain(): RentalEquipment {
    return RentalEquipment(
        id = this.id,
        name = this.name,
        type = this.type,
        model = this.model,
        manufacturer = this.manufacturer,
        monthlyFee = this.monthlyFee,
        description = this.description ?: this.technicalDetails,
        providerId = this.providerId,
        providerName = this.providerName,
        isAvailable = this.isAvailable,
        location = this.location?.let {
            Location(
                name = it.name,
                address = it.address,
                latitude = it.latitude,
                longitude = it.longitude
            )
        },
        technicalDetails = this.technicalDetails,
        currentTemperature = this.currentTemperature
    )
}
