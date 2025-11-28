package com.example.ositopolarapp.features.equipment.domain.repository

import com.example.ositopolarapp.features.equipment.domain.model.Equipment

interface EquipmentRepository {
    // Nota: Asegúrate de que los nombres sean EXACTAMENTE estos
    suspend fun getAllEquipments(): Result<List<Equipment>>

    suspend fun getEquipmentById(id: Int): Result<Equipment>

    suspend fun createEquipment(
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
    ): Result<Equipment>

    suspend fun updateOperations(id: Int, status: String, temperature: Double): Result<Equipment>

    suspend fun deleteEquipment(id: Int): Result<Unit>
}