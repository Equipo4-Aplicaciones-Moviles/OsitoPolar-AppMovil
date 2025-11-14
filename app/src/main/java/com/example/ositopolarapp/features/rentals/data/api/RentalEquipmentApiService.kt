package com.example.ositopolarapp.features.rentals.data.api

import com.example.ositopolarapp.features.rentals.data.dto.RentalEquipmentDto
import retrofit2.Response
import retrofit2.http.*

/**
 * Rental Equipment API Service (Owner view only)
 *
 * Endpoints:
 * - GET /rental-equipment - Get available rental equipment catalog
 * - GET /rental-equipment/{id} - Get rental equipment details
 */
interface RentalEquipmentApiService {

    /**
     * Get all available rental equipment
     * Endpoint: GET /api/v1/rental-equipment
     */
    @GET("rental-equipment")
    suspend fun getAllRentalEquipment(): Response<List<RentalEquipmentDto>>

    /**
     * Get rental equipment by ID
     * Endpoint: GET /api/v1/rental-equipment/{id}
     */
    @GET("rental-equipment/{id}")
    suspend fun getRentalEquipmentById(
        @Path("id") equipmentId: Int
    ): Response<RentalEquipmentDto>
}
