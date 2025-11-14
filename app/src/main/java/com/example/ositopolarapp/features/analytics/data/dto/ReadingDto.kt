package com.example.ositopolarapp.features.analytics.data.dto

/**
 * Equipment reading (temperature or energy)
 */
data class ReadingDto(
    val id: Int,
    val equipmentId: Int,
    val type: String,  // "temperature" or "energy"
    val value: Double,
    val unit: String,  // "celsius" or "kWh"
    val timestamp: String,
    val status: String,  // "normal", "warning", "critical"
    val notes: String? = null
)
