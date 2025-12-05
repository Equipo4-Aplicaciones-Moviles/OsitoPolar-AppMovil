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
 * - POST /rental-equipment/request - Create rental request and get Stripe checkout URL
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

    /**
     * Create rental request and get Stripe checkout URL
     * Endpoint: POST /api/v1/rental-equipment/request
     */
    @POST("rental-equipment/request")
    suspend fun createRentalRequest(
        @Body request: CreateRentalRequestDto
    ): Response<CreateRentalResponseDto>
}

/**
 * Request to create a rental
 */
data class CreateRentalRequestDto(
    val equipmentId: Int,
    val months: Int,
    val successUrl: String? = null,
    val cancelUrl: String? = null
)

/**
 * Response from creating a rental request
 */
data class CreateRentalResponseDto(
    val checkoutUrl: String,
    val sessionId: String,
    val totalAmount: Double,
    val months: Int,
    val monthlyFee: Double
)
