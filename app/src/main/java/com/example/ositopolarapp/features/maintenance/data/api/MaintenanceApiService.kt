package com.example.ositopolarapp.features.maintenance.data.api

import com.example.ositopolarapp.features.maintenance.data.dto.MaintenanceForecastDto
import retrofit2.Response
import retrofit2.http.*

/**
 * Maintenance API Service
 *
 * For getting maintenance forecasts
 */
interface MaintenanceApiService {

    /**
     * Get maintenance forecast for specific equipment
     * Endpoint: GET /api/v1/maintenance-notifications/forecast/{equipmentId}
     */
    @GET("maintenance-notifications/forecast/{equipmentId}")
    suspend fun getMaintenanceForecast(
        @Path("equipmentId") equipmentId: Int
    ): Response<MaintenanceForecastDto>
}
