package com.example.ositopolarapp.features.rentals.domain.model

/**
 * Rental Equipment Domain Model
 *
 * Represents equipment available for rent from providers.
 */
data class RentalEquipment(
    val id: Int,
    val name: String,
    val type: String,
    val model: String,
    val manufacturer: String,
    val monthlyFee: Double,
    val description: String?,
    val providerId: Int,
    val providerName: String?,
    val isAvailable: Boolean,
    val location: Location? = null,
    val technicalDetails: String? = null,
    val currentTemperature: Double? = null
) {
    fun getFormattedPrice(): String = "$$monthlyFee/mes"
    fun getStatus(): RentalStatus = if (isAvailable) RentalStatus.AVAILABLE else RentalStatus.RENTED
}

/**
 * Location information
 */
data class Location(
    val name: String?,
    val address: String?,
    val latitude: Double?,
    val longitude: Double?
)

enum class RentalStatus {
    AVAILABLE,
    RENTED,
    MAINTENANCE
}
