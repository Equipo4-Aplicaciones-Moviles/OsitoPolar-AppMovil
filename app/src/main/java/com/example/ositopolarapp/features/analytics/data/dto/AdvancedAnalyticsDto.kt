package com.example.ositopolarapp.features.analytics.data.dto

import com.google.gson.annotations.SerializedName

/**
 * Health Score Response
 * GET /api/v1/analytics/equipments/{id}/health
 */
data class HealthScoreResponse(
    @SerializedName("equipmentId") val equipmentId: Int = 0,
    @SerializedName("equipmentName") val equipmentName: String? = null,
    @SerializedName("healthScore") val healthScore: Double = 0.0,
    @SerializedName("status") val status: String? = null,
    @SerializedName("temperatureStability") val temperatureStability: Double = 0.0,
    @SerializedName("averageDeviation") val averageDeviation: Double = 0.0,
    @SerializedName("hoursAnalyzed") val hoursAnalyzed: Int = 0,
    @SerializedName("readingsCount") val readingsCount: Int = 0,
    @SerializedName("timestamp") val timestamp: String? = null
)

/**
 * Anomalies Response
 * GET /api/v1/analytics/equipments/{id}/anomalies
 */
data class AnomaliesResponse(
    @SerializedName("equipmentId") val equipmentId: Int = 0,
    @SerializedName("equipmentName") val equipmentName: String? = null,
    @SerializedName("anomaliesDetected") val anomaliesDetected: List<AnomalyDto>? = null,
    @SerializedName("totalAnomalies") val totalAnomalies: Int = 0,
    @SerializedName("hoursAnalyzed") val hoursAnalyzed: Int = 0,
    @SerializedName("timestamp") val timestamp: String? = null
)

data class AnomalyDto(
    @SerializedName("type") val type: String? = null,
    @SerializedName("severity") val severity: String? = null,
    @SerializedName("description") val description: String? = null,
    @SerializedName("detectedAt") val detectedAt: String? = null,
    @SerializedName("duration") val duration: String? = null,
    @SerializedName("temperatureChange") val temperatureChange: Double? = null
)

/**
 * Cost Analysis Response
 * GET /api/v1/analytics/equipments/{id}/costs
 */
data class CostAnalysisResponse(
    @SerializedName("equipmentId") val equipmentId: Int = 0,
    @SerializedName("equipmentName") val equipmentName: String? = null,
    @SerializedName("currentPeriod") val currentPeriod: CostPeriodDto? = null,
    @SerializedName("previousPeriod") val previousPeriod: CostPeriodDto? = null,
    @SerializedName("comparison") val comparison: CostComparisonDto? = null,
    @SerializedName("costPerKwh") val costPerKwh: Double = 0.0,
    @SerializedName("timestamp") val timestamp: String? = null
)

data class CostPeriodDto(
    @SerializedName("startDate") val startDate: String? = null,
    @SerializedName("endDate") val endDate: String? = null,
    @SerializedName("totalEnergyKwh") val totalEnergyKwh: Double = 0.0,
    @SerializedName("totalCost") val totalCost: Double = 0.0,
    @SerializedName("averageDailyCost") val averageDailyCost: Double = 0.0,
    @SerializedName("days") val days: Int = 0
)

data class CostComparisonDto(
    @SerializedName("energyChangePercent") val energyChangePercent: Double = 0.0,
    @SerializedName("costChangePercent") val costChangePercent: Double = 0.0,
    @SerializedName("trend") val trend: String? = null,
    @SerializedName("estimatedMonthlyCost") val estimatedMonthlyCost: Double = 0.0
)

/**
 * Maintenance Forecast Response
 * GET /api/v1/analytics/equipments/{id}/maintenance-forecast
 */
data class MaintenanceForecastResponse(
    @SerializedName("equipmentId") val equipmentId: Int = 0,
    @SerializedName("equipmentName") val equipmentName: String? = null,
    @SerializedName("forecast") val forecast: MaintenanceForecastDto? = null,
    @SerializedName("recommendations") val recommendations: List<String>? = null,
    @SerializedName("timestamp") val timestamp: String? = null
)

data class MaintenanceForecastDto(
    @SerializedName("nextMaintenanceDate") val nextMaintenanceDate: String? = null,
    @SerializedName("daysUntilMaintenance") val daysUntilMaintenance: Int? = null,
    @SerializedName("priority") val priority: String? = null,
    @SerializedName("confidence") val confidence: String? = null,
    @SerializedName("basedOn") val basedOn: String? = null,
    @SerializedName("estimatedCost") val estimatedCost: Double? = null
)
