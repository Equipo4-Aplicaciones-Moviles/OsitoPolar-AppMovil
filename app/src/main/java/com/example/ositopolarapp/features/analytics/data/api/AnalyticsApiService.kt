package com.example.ositopolarapp.features.analytics.data.api

import com.example.ositopolarapp.features.analytics.data.dto.*
import retrofit2.Response
import retrofit2.http.*

/**
 * Analytics API Service for equipment monitoring and insights
 *
 * Endpoints:
 * - GET /analytics/equipments/{id}/readings - Get equipment readings
 * - GET /analytics/equipments/{id}/summaries - Get daily summaries
 * - GET /analytics/equipments/overview - Get multi-equipment overview
 */
interface AnalyticsApiService {

    /**
     * Get equipment readings (temperature, energy, or all)
     * Endpoint: GET /api/v1/analytics/equipments/{equipmentId}/readings
     * @param equipmentId Equipment ID
     * @param type Reading type: "all", "temperature", or "energy" (default: "all")
     * @param hours Hours to look back (default: 24)
     * @param limit Maximum number of readings (default: 100)
     */
    @GET("analytics/equipments/{equipmentId}/readings")
    suspend fun getEquipmentReadings(
        @Path("equipmentId") equipmentId: Int,
        @Query("type") type: String = "all",
        @Query("hours") hours: Int = 24,
        @Query("limit") limit: Int = 100
    ): Response<ReadingsResponse>

    /**
     * Get equipment daily summaries (averages)
     * Endpoint: GET /api/v1/analytics/equipments/{equipmentId}/summaries
     * @param equipmentId Equipment ID
     * @param type Summary type: "daily-averages" (default)
     * @param days Number of days to look back (default: 7)
     */
    @GET("analytics/equipments/{equipmentId}/summaries")
    suspend fun getEquipmentSummaries(
        @Path("equipmentId") equipmentId: Int,
        @Query("type") type: String = "daily-averages",
        @Query("days") days: Int = 7
    ): Response<SummariesResponse>

    /**
     * Get multi-equipment overview for dashboard
     * Endpoint: GET /api/v1/analytics/equipments/overview
     * @param ids Comma-separated equipment IDs (e.g., "101,102,103")
     * @param type Overview type: "current" (default)
     */
    @GET("analytics/equipments/overview")
    suspend fun getEquipmentOverview(
        @Query("ids") ids: String,
        @Query("type") type: String = "current"
    ): Response<OverviewResponse>
}
