package com.example.ositopolarapp.features.equipment.domain.usecase

import com.example.ositopolarapp.features.equipment.domain.model.Equipment
import com.example.ositopolarapp.features.equipment.domain.repository.EquipmentRepository

class CreateEquipmentUseCase(
    private val repository: EquipmentRepository
) {
    // El operador invoke permite llamar a la clase como una función
    suspend operator fun invoke(
        ownerId: Int,
        name: String,
        type: String,
        model: String,
        serialNumber: String,
        brand: String,
        location: String,
        address: String,
        latitude: Double,
        longitude: Double
    ): Result<Equipment> {
        return repository.createEquipment(
            ownerId, name, type, model, serialNumber, brand, location, address, latitude, longitude
        )
    }
}