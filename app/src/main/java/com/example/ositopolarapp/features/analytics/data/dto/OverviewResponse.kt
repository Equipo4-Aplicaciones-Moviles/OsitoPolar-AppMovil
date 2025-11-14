package com.example.ositopolarapp.features.analytics.data.dto

/**
 * Response for multi-equipment overview
 */
data class OverviewResponse(
    val equipments: List<EquipmentOverviewDto>,
    val summary: OverviewSummaryDto
)
