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

    // Advanced Analytics
    suspend fun getEquipmentHealth(
        equipmentId: Int,
        hours: Int = 24
    ): Result<HealthScoreResponse>

    suspend fun getEquipmentAnomalies(
        equipmentId: Int,
        hours: Int = 24
    ): Result<AnomaliesResponse>

    suspend fun getEnergyCosts(
        equipmentId: Int,
        days: Int = 30,
        costPerKwh: Double = 0.12
    ): Result<CostAnalysisResponse>

    suspend fun getMaintenanceForecast(
        equipmentId: Int
    ): Result<MaintenanceForecastResponse>
}
