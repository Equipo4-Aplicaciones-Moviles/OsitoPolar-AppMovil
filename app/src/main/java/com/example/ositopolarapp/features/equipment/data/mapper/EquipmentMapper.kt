package com.example.ositopolarapp.features.equipment.data.mapper

import com.example.ositopolarapp.features.equipment.data.dto.CreateEquipmentRequest
import com.example.ositopolarapp.features.equipment.data.dto.EquipmentDto
import com.example.ositopolarapp.features.equipment.data.dto.LocationUpdateDto
import com.example.ositopolarapp.features.equipment.domain.model.Equipment
import com.example.ositopolarapp.features.equipment.domain.model.EquipmentStatus
import com.example.ositopolarapp.features.equipment.domain.model.EquipmentType
import com.example.ositopolarapp.features.equipment.domain.model.OwnershipType
import com.example.ositopolarapp.features.equipment.domain.repository.LocationUpdate

/**
 * Extension function to convert EquipmentDto to Equipment domain entity.
 */
fun EquipmentDto.toEntity(): Equipment {
    return Equipment(
        id = id,
        name = name,
        type = when (type.uppercase()) {
            "FREEZER" -> EquipmentType.FREEZER
            "COLDROOM", "COLD_ROOM" -> EquipmentType.COLD_ROOM
            "REFRIGERATOR" -> EquipmentType.REFRIGERATOR
            else -> EquipmentType.REFRIGERATOR
        },
        model = model,
        manufacturer = manufacturer,
        serialNumber = serialNumber,
        code = code,
        cost = cost,
        technicalDetails = technicalDetails ?: "",
        currentTemperature = currentTemperature,
        setTemperature = setTemperature,
        optimalTemperatureMin = optimalTemperatureMin,
        optimalTemperatureMax = optimalTemperatureMax,
        locationName = locationName,
        locationAddress = locationAddress,
        locationLatitude = locationLatitude,
        locationLongitude = locationLongitude,
        energyConsumptionCurrent = energyConsumptionCurrent,
        energyConsumptionUnit = energyConsumptionUnit,
        energyConsumptionAverage = energyConsumptionAverage,
        isPoweredOn = isPoweredOn,
        status = when (status.uppercase()) {
            "ACTIVE" -> EquipmentStatus.ACTIVE
            "INACTIVE" -> EquipmentStatus.INACTIVE
            "MAINTENANCE" -> EquipmentStatus.MAINTENANCE
            "RETIRED" -> EquipmentStatus.RETIRED
            else -> EquipmentStatus.ACTIVE
        },
        ownerId = ownerId,
        ownerType = ownerType,
        ownershipType = when (ownershipType.uppercase()) {
            "OWNED" -> OwnershipType.OWNED
            "RENTED" -> OwnershipType.RENTED
            else -> OwnershipType.OWNED
        },
        createdAt = createdAt,
        updatedAt = updatedAt,
        notes = notes ?: ""
    )
}

/**
 * Extension function to convert Equipment domain entity to CreateEquipmentRequest DTO.
 * Note: OwnerId is set automatically from JWT token by the backend - we don't send it.
 */
fun Equipment.toCreateRequest(): CreateEquipmentRequest {
    return CreateEquipmentRequest(
        name = name,
        type = when (type) {
            EquipmentType.FREEZER -> "Freezer"
            EquipmentType.COLD_ROOM -> "ColdRoom"
            EquipmentType.REFRIGERATOR -> "Refrigerator"
        },
        model = model,
        manufacturer = manufacturer,
        serialNumber = serialNumber,
        code = code,
        cost = cost,
        technicalDetails = technicalDetails.ifBlank { "" },
        currentTemperature = currentTemperature,
        setTemperature = setTemperature,
        optimalTemperatureMin = optimalTemperatureMin,
        optimalTemperatureMax = optimalTemperatureMax,
        locationName = locationName,
        locationAddress = locationAddress,
        locationLatitude = locationLatitude ?: 0.0,
        locationLongitude = locationLongitude ?: 0.0,
        energyConsumptionCurrent = energyConsumptionCurrent,
        energyConsumptionUnit = energyConsumptionUnit,
        energyConsumptionAverage = energyConsumptionAverage,
        ownershipType = when (ownershipType) {
            OwnershipType.OWNED -> "Owned"
            OwnershipType.RENTED -> "Rented"
        },
        notes = notes
    )
}

/**
 * Extension function to convert LocationUpdate to LocationUpdateDto.
 */
fun LocationUpdate.toDto(): LocationUpdateDto {
    return LocationUpdateDto(
        address = address,
        latitude = latitude,
        longitude = longitude
    )
}
