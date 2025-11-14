package com.example.ositopolarapp.features.rentals.data.dto

/**
 * Rental equipment data transfer object
 */
data class RentalEquipmentDto(
    val id: Int,
    val name: String,
    val type: String,  // "Refrigerator", "Freezer", "ColdRoom"
    val model: String,
    val manufacturer: String,
    val description: String,
    val monthlyPrice: Double,
    val providerId: Int,
    val providerName: String,
    val status: String,  // "AVAILABLE", "RENTED"
    val imageUrl: String? = null,
    val specifications: Map<String, String>? = null
)
