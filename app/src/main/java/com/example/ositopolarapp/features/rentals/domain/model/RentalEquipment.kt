package com.example.ositopolarapp.features.rentals.domain.model

/**
 * Rental Equipment Domain Model
 *
 * Represents equipment available for rent from providers.
 * NOTE: Backend endpoints not yet implemented - using mock data.
 */
data class RentalEquipment(
    val id: Int,
    val name: String,
    val type: String,
    val manufacturer: String,
    val monthlyPrice: Double,
    val description: String,
    val providerId: Int,
    val providerName: String,
    val status: RentalStatus,
    val imageUrl: String? = null
) {
    fun getFormattedPrice(): String = "$${monthlyPrice}/mes"
}

enum class RentalStatus {
    AVAILABLE,
    RENTED,
    MAINTENANCE
}
