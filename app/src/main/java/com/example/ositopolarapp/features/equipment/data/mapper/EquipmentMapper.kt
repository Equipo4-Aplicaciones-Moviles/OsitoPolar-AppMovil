package com.example.ositopolarapp.features.equipment.data.mapper

import com.example.ositopolarapp.features.equipment.data.dto.EquipmentDto
import com.example.ositopolarapp.features.equipment.domain.model.Equipment

fun EquipmentDto.toDomain(): Equipment {
    return Equipment(
        id = this.id,
        name = this.name,
        // Si viene nulo, ponemos un valor por defecto
        type = this.type ?: "Unknown",
        model = this.model,
        serialNumber = this.serialNumber,
        // Mapeamos los nombres del DTO a los nombres del Dominio
        brand = this.manufacturer,
        location = this.locationName ?: "Sin ubicación",
        address = this.locationAddress ?: "",
        latitude = this.locationLatitude ?: 0.0,
        longitude = this.locationLongitude ?: 0.0,
        status = this.status ?: "Active",
        temperature = this.currentTemperature ?: 0.0,
        ownerId = this.ownerId ?: 0,
        imageUrl = this.imageUrl,
        energyConsumption = this.energyConsumptionCurrent ?: 0.0,
        isPoweredOn = this.isPoweredOn ?: false
    )
}