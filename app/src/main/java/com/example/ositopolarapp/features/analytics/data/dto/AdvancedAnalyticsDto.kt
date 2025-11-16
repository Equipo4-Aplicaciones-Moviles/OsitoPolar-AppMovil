package com.example.ositopolarapp.features.analytics.data.dto

import com.google.gson.annotations.SerializedName

/**
 * Health Score Response
 * GET /api/v1/analytics/equipments/{id}/health
 */
data class HealthScoreResponse(
    @SerializedName("equipmentId") val equipmentId: Int,
    @SerializedName("equipmentName") val equipmentName: String,
    @SerializedName("healthScore") val healthScore: Double, // 0-100
    @SerializedName("status") val status: String, // "Excellent", "Good", "Fair", "Poor", "Critical"
    @SerializedName("temperatureStability") val temperatureStability: Double,
    @SerializedName("averageDeviation") val averageDeviation: Double,
    @SerializedName("hoursAnalyzed") val hoursAnalyzed: Int,
    @SerializedName("readingsCount") val readingsCount: Int,
    @SerializedName("timestamp") val timestamp: String
)

/**
 * Anomalies Response
 * GET /api/v1/analytics/equipments/{id}/anomalies
 */
data class AnomaliesResponse(
    @SerializedName("equipmentId") val equipmentId: Int,
    @SerializedName("equipmentName") val equipmentName: String,
    @SerializedName("anomaliesDetected") val anomaliesDetected: List<AnomalyDto>,
    @SerializedName("totalAnomalies") val totalAnomalies: Int,
    @SerializedName("hoursAnalyzed") val hoursAnalyzed: Int,
    @SerializedName("timestamp") val timestamp: String
)

data class AnomalyDto(
    @SerializedName("type") val type: String, // "DoorOpen", "CompressorFailure", "PowerOutage"
    @SerializedName("severity") val severity: String, // "Low", "Medium", "High", "Critical"
    @SerializedName("description") val description: String,
    @SerializedName("detectedAt") val detectedAt: String,
    @SerializedName("duration") val duration: String?, // e.g., "15 minutes"
    @SerializedName("temperatureChange") val temperatureChange: Double?
)

/**
 * Cost Analysis Response
 * GET /api/v1/analytics/equipments/{id}/costs
 */
data class CostAnalysisResponse(
    @SerializedName("equipmentId") val equipmentId: Int,
    @SerializedName("equipmentName") val equipmentName: String,
    @SerializedName("currentPeriod") val currentPeriod: CostPeriodDto,
    @SerializedName("previousPeriod") val previousPeriod: CostPeriodDto,
    @SerializedName("comparison") val comparison: CostComparisonDto,
    @SerializedName("costPerKwh") val costPerKwh: Double,
    @SerializedName("timestamp") val timestamp: String
)

data class CostPeriodDto(
    @SerializedName("startDate") val startDate: String,
    @SerializedName("endDate") val endDate: String,
    @SerializedName("totalEnergyKwh") val totalEnergyKwh: Double,
    @SerializedName("totalCost") val totalCost: Double,
    @SerializedName("averageDailyCost") val averageDailyCost: Double,
    @SerializedName("days") val days: Int
)

data class CostComparisonDto(
    @SerializedName("energyChangePercent") val energyChangePercent: Double,
    @SerializedName("costChangePercent") val costChangePercent: Double,
    @SerializedName("trend") val trend: String, // "Increasing", "Decreasing", "Stable"
    @SerializedName("estimatedMonthlyCost") val estimatedMonthlyCost: Double
)

/**
 * Maintenance Forecast Response
 * GET /api/v1/analytics/equipments/{id}/maintenance-forecast
 */
data class MaintenanceForecastResponse(
    @SerializedName("equipmentId") val equipmentId: Int,
    @SerializedName("equipmentName") val equipmentName: String,
    @SerializedName("forecast") val forecast: MaintenanceForecastDto,
    @SerializedName("recommendations") val recommendations: List<String>,
    @SerializedName("timestamp") val timestamp: String
)

data class MaintenanceForecastDto(
    @SerializedName("nextMaintenanceDate") val nextMaintenanceDate: String?,
    @SerializedName("daysUntilMaintenance") val daysUntilMaintenance: Int?,
    @SerializedName("priority") val priority: String, // "Low", "Medium", "High", "Critical"
    @SerializedName("confidence") val confidence: String, // "Low", "Medium", "High"
    @SerializedName("basedOn") val basedOn: String, // e.g., "Usage hours and temperature stability"
    @SerializedName("estimatedCost") val estimatedCost: Double?
)
