package com.example.ositopolarapp.features.analytics.data.dto

/**
 * Response containing daily summaries
 */
data class SummariesResponse(
    val data: List<DailySummaryDto>,
    val total: Int,
    val equipmentId: Int,
    val type: String,  // "daily-averages"
    val days: Int
)
