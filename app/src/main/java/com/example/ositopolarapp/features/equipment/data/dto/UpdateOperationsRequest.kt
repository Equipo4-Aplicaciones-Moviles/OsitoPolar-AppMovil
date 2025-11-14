package com.example.ositopolarapp.features.equipment.data.dto

import com.google.gson.annotations.SerializedName

/**
 * Request DTO for updating equipment operations.
 * Matches backend API PATCH /api/v1/equipments/{id}/operations request body.
 */
data class UpdateOperationsRequest(
    @SerializedName("temperature")
    val temperature: Double? = null,

    @SerializedName("powerState")
    val powerState: String? = null, // "ON" or "OFF"

    @SerializedName("location")
    val location: LocationUpdateDto? = null
)

/**
 * DTO for location update within operations request.
 */
data class LocationUpdateDto(
    @SerializedName("address")
    val address: String,

    @SerializedName("latitude")
    val latitude: Double,

    @SerializedName("longitude")
    val longitude: Double
)
