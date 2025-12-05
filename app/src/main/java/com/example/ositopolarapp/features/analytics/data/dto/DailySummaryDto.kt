package com.example.ositopolarapp.features.analytics.data.dto

/**
 * Daily temperature summary/average
 */
data class DailySummaryDto(
    val id: Int,
    val equipmentId: Int,
    val date: String,
    val type: String,  // "daily-average"
    val averageTemperature: Double,
    val minTemperature: Double,
    val maxTemperature: Double
)
