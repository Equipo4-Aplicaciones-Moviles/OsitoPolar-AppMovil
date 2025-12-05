package com.example.ositopolarapp.features.maintenance.domain.usecase

import com.example.ositopolarapp.features.maintenance.domain.model.MaintenanceForecast
import com.example.ositopolarapp.features.maintenance.domain.repository.MaintenanceRepository

class GetMaintenanceForecastUseCase(
    private val repository: MaintenanceRepository
) {
    suspend operator fun invoke(equipmentId: Int): Result<MaintenanceForecast> {
        return repository.getMaintenanceForecast(equipmentId)
    }
}
