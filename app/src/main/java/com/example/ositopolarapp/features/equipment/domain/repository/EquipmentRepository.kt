package com.example.ositopolarapp.features.equipment.domain.repository

import com.example.ositopolarapp.features.equipment.domain.model.Equipment
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for Equipment operations.
 * Defines contracts for equipment management operations.
 */
interface EquipmentRepository {

    /**
     * Retrieves all equipment owned by the current user.
     * @return Result containing list of equipment or error
     */
    suspend fun getAllEquipment(): Result<List<Equipment>>

    /**
     * Retrieves equipment by owner ID.
     * @param ownerId The ID of the equipment owner
     * @return Result containing list of equipment or error
     */
    suspend fun getEquipmentByOwner(ownerId: Int): Result<List<Equipment>>

    /**
     * Retrieves a single equipment by its ID.
     * @param equipmentId The equipment ID
     * @return Result containing the equipment or error
     */
    suspend fun getEquipmentById(equipmentId: Int): Result<Equipment>

    /**
     * Creates new equipment.
     * @param equipment The equipment to create
     * @return Result containing the created equipment or error
     */
    suspend fun createEquipment(equipment: Equipment): Result<Equipment>

    /**
     * Updates existing equipment.
     * @param equipmentId The ID of the equipment to update
     * @param equipment The updated equipment data
     * @return Result containing the updated equipment or error
     */
    suspend fun updateEquipment(equipmentId: Int, equipment: Equipment): Result<Equipment>

    /**
     * Deletes equipment by ID.
     * @param equipmentId The ID of the equipment to delete
     * @return Result indicating success or failure
     */
    suspend fun deleteEquipment(equipmentId: Int): Result<Unit>

    /**
     * Updates equipment operational parameters (temperature, power, location).
     * @param equipmentId The equipment ID
     * @param temperature Optional new temperature setting
     * @param powerState Optional new power state ("ON" or "OFF")
     * @param location Optional new location data
     * @return Result containing the updated equipment or error
     */
    suspend fun updateEquipmentOperations(
        equipmentId: Int,
        temperature: Double? = null,
        powerState: String? = null,
        location: LocationUpdate? = null
    ): Result<Equipment>
}

/**
 * Data class for location updates.
 */
data class LocationUpdate(
    val address: String,
    val latitude: Double,
    val longitude: Double
)
