package com.example.ositopolarapp.features.rentals.data.dto

import com.google.gson.annotations.SerializedName

/**
 * Rental equipment data transfer object (matches backend response)
 */
data class RentalEquipmentDto(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("type") val type: String,
    @SerializedName("model") val model: String,
    @SerializedName("manufacturer") val manufacturer: String,
    @SerializedName("serialNumber") val serialNumber: String? = null,
    @SerializedName("monthlyFee") val monthlyFee: Double,
    @SerializedName("availableFrom") val availableFrom: String? = null,
    @SerializedName("availableUntil") val availableUntil: String? = null,
    @SerializedName("providerId") val providerId: Int,
    @SerializedName("providerName") val providerName: String? = null,
    @SerializedName("location") val location: LocationDto? = null,
    @SerializedName("technicalDetails") val technicalDetails: String? = null,
    @SerializedName("notes") val notes: String? = null,
    @SerializedName("description") val description: String? = null,
    @SerializedName("isAvailable") val isAvailable: Boolean = true,
    @SerializedName("currentTemperature") val currentTemperature: Double? = null
)

/**
 * Location information
 */
data class LocationDto(
    @SerializedName("name") val name: String? = null,
    @SerializedName("address") val address: String? = null,
    @SerializedName("latitude") val latitude: Double? = null,
    @SerializedName("longitude") val longitude: Double? = null
)
