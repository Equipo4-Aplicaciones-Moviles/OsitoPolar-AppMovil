package com.example.ositopolarapp.features.analytics.data.dto

/**
 * Overview data for a single equipment
 */
data class EquipmentOverviewDto(
    val equipmentId: Int,
    val lastTemperature: Double,
    val lastEnergyReading: Double,
    val status: String,  // "normal", "warning", "critical"
    val lastReadingTime: String
)
