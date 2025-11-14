package com.example.ositopolarapp.features.analytics.data.dto

/**
 * Summary statistics for equipment overview
 */
data class OverviewSummaryDto(
    val totalEquipments: Int,
    val normalCount: Int,
    val warningCount: Int,
    val criticalCount: Int
)
