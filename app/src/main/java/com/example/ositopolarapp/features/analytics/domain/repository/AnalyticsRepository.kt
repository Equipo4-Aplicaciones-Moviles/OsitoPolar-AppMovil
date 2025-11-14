package com.example.ositopolarapp.features.analytics.domain.repository

import com.example.ositopolarapp.features.analytics.data.dto.*

interface AnalyticsRepository {
    suspend fun getEquipmentReadings(
        equipmentId: Int,
        type: String = "all",
        hours: Int = 24,
        limit: Int = 100
    ): Result<ReadingsResponse>

    suspend fun getEquipmentSummaries(
        equipmentId: Int,
        type: String = "daily-averages",
        days: Int = 7
    ): Result<SummariesResponse>

    suspend fun getEquipmentOverview(
        equipmentIds: List<Int>,
        type: String = "current"
    ): Result<OverviewResponse>
}
