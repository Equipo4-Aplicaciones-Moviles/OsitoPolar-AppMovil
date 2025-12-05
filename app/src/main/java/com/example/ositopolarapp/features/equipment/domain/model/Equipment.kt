package com.example.ositopolarapp.features.equipment.domain.model

/**
 * Equipment domain entity representing industrial freezing/refrigeration equipment.
 * This entity contains all business logic related to equipment status, temperature monitoring,
 * and operational state.
 */
data class Equipment(
    val id: Int = 0,
    val name: String = "",
    val type: EquipmentType = EquipmentType.REFRIGERATOR,
    val model: String = "",
    val manufacturer: String = "",
    val serialNumber: String = "",
    val code: String = "",
    val cost: Double = 0.0,
    val technicalDetails: String = "",

    // Temperature
    val currentTemperature: Double = 0.0,
    val setTemperature: Double = 0.0,
    val optimalTemperatureMin: Double = 0.0,
    val optimalTemperatureMax: Double = 0.0,

    // Location
    val locationName: String = "",
    val locationAddress: String = "",
    val locationLatitude: Double? = null,
    val locationLongitude: Double? = null,

    // Energy
    val energyConsumptionCurrent: Double = 0.0,
    val energyConsumptionUnit: String = "kWh",
    val energyConsumptionAverage: Double = 0.0,

    // Operational
    val isPoweredOn: Boolean = true,
    val status: EquipmentStatus = EquipmentStatus.ACTIVE,

    // Ownership
    val ownerId: Int = 0,
    val ownerType: String = "User",
    val ownershipType: OwnershipType = OwnershipType.OWNED,

    // Timestamps
    val createdAt: String? = null,
    val updatedAt: String? = null,

    // Notes
    val notes: String = ""
) {
    /**
     * Determines the temperature status based on current temperature and optimal range.
     * Returns: NORMAL, WARNING, CRITICAL, or OFF
     */
    fun getTemperatureStatus(): TemperatureStatus {
        if (!isPoweredOn) return TemperatureStatus.OFF

        val temp = currentTemperature
        val min = optimalTemperatureMin
        val max = optimalTemperatureMax

        if (temp < min || temp > max) {
            val minDiff = kotlin.math.abs(temp - min)
            val maxDiff = kotlin.math.abs(temp - max)
            val threshold = kotlin.math.abs(max - min) * 0.2

            return if (minDiff > threshold || maxDiff > threshold) {
                TemperatureStatus.CRITICAL
            } else {
                TemperatureStatus.WARNING
            }
        }

        return TemperatureStatus.NORMAL
    }

    /**
     * Returns the color associated with the current temperature status.
     */
    fun getStatusColor(): Long {
        return when (getTemperatureStatus()) {
            TemperatureStatus.NORMAL -> 0xFF4CAF50 // Green
            TemperatureStatus.WARNING -> 0xFFFFC107 // Amber
            TemperatureStatus.CRITICAL -> 0xFFF44336 // Red
            TemperatureStatus.OFF -> 0xFF9E9E9E // Gray
        }
    }

    /**
     * Returns a user-friendly display name for the equipment type.
     */
    fun getTypeDisplay(): String {
        return when (type) {
            EquipmentType.FREEZER -> "Freezer"
            EquipmentType.COLD_ROOM -> "Cold Room"
            EquipmentType.REFRIGERATOR -> "Refrigerator"
        }
    }

    /**
     * Checks if the equipment has valid location coordinates.
     */
    fun hasLocation(): Boolean {
        return locationLatitude != null && locationLongitude != null
    }

    /**
     * Returns a formatted temperature range string.
     */
    fun getOptimalTemperatureRange(): String {
        return "${optimalTemperatureMin}°C - ${optimalTemperatureMax}°C"
    }

    /**
     * Checks if the current temperature is within the optimal range.
     */
    fun isTemperatureOptimal(): Boolean {
        return currentTemperature >= optimalTemperatureMin &&
               currentTemperature <= optimalTemperatureMax
    }
}

/**
 * Enum representing the type of refrigeration equipment.
 */
enum class EquipmentType {
    FREEZER,
    COLD_ROOM,
    REFRIGERATOR
}

/**
 * Enum representing the operational status of equipment.
 */
enum class EquipmentStatus {
    ACTIVE,
    INACTIVE,
    MAINTENANCE,
    RETIRED
}

/**
 * Enum representing the ownership type of equipment.
 */
enum class OwnershipType {
    OWNED,
    RENTED
}

/**
 * Enum representing the temperature status based on optimal range.
 */
enum class TemperatureStatus {
    NORMAL,
    WARNING,
    CRITICAL,
    OFF
}
