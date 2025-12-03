package com.example.ositopolarapp.features.maintenance.domain.repository

import com.example.ositopolarapp.features.maintenance.domain.model.MaintenanceForecast

interface MaintenanceRepository {

    /**
     * Get maintenance forecast for an equipment
     */
    suspend fun getMaintenanceForecast(equipmentId: Int): Result<MaintenanceForecast>
}
