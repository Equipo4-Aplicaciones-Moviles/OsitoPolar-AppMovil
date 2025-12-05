package com.example.ositopolarapp.features.analytics.data.dto

/**
 * Response containing equipment readings
 */
data class ReadingsResponse(
    val data: List<ReadingDto>,
    val total: Int,
    val equipmentId: Int,
    val type: String,  // "all", "temperature", "energy"
    val period: String  // "24h", "7d", etc.
)
