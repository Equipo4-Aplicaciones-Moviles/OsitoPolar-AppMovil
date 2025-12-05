package com.example.ositopolarapp.features.equipment.data.api

import com.example.ositopolarapp.features.equipment.data.dto.CreateEquipmentRequest
import com.example.ositopolarapp.features.equipment.data.dto.EquipmentDto
import com.example.ositopolarapp.features.equipment.data.dto.UpdateOperationsRequest
import retrofit2.Response
import retrofit2.http.*

/**
 * Retrofit API service for Equipment endpoints.
 * All endpoints match the backend API specification.
 */
interface EquipmentApiService {

    /**
     * Get all equipment for the current user (authenticated via JWT token).
     * Endpoint: GET /api/v1/equipments
     */
    @GET("equipments")
    suspend fun getAllEquipment(): Response<List<EquipmentDto>>

    /**
     * Get equipment by owner ID.
     * Endpoint: GET /api/v1/equipments?owner-id={ownerId}
     */
    @GET("equipments")
    suspend fun getEquipmentByOwner(
        @Query("owner-id") ownerId: Int
    ): Response<List<EquipmentDto>>

    /**
     * Get equipment by ID.
     * Endpoint: GET /api/v1/equipments/{equipmentId}
     */
    @GET("equipments/{equipmentId}")
    suspend fun getEquipmentById(
        @Path("equipmentId") equipmentId: Int
    ): Response<EquipmentDto>

    /**
     * Create new equipment.
     * Endpoint: POST /api/v1/equipments
     */
    @POST("equipments")
    suspend fun createEquipment(
        @Body request: CreateEquipmentRequest
    ): Response<EquipmentDto>

    /**
     * Update existing equipment.
     * Endpoint: PUT /api/v1/equipments/{equipmentId}
     */
    @PUT("equipments/{equipmentId}")
    suspend fun updateEquipment(
        @Path("equipmentId") equipmentId: Int,
        @Body request: CreateEquipmentRequest
    ): Response<EquipmentDto>

    /**
     * Delete equipment.
     * Endpoint: DELETE /api/v1/equipments/{equipmentId}
     */
    @DELETE("equipments/{equipmentId}")
    suspend fun deleteEquipment(
        @Path("equipmentId") equipmentId: Int
    ): Response<Unit>

    /**
     * Update equipment operations (temperature, power, location).
     * Endpoint: PATCH /api/v1/equipments/{equipmentId}/operations
     */
    @PATCH("equipments/{equipmentId}/operations")
    suspend fun updateEquipmentOperations(
        @Path("equipmentId") equipmentId: Int,
        @Body request: UpdateOperationsRequest
    ): Response<EquipmentDto>
}
