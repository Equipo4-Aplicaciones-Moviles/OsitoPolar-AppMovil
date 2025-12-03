package com.example.ositopolarapp.features.maintenance.data.repository

import com.example.ositopolarapp.features.maintenance.data.api.MaintenanceApiService
import com.example.ositopolarapp.features.maintenance.data.mapper.toDomain
import com.example.ositopolarapp.features.maintenance.domain.model.MaintenanceForecast
import com.example.ositopolarapp.features.maintenance.domain.repository.MaintenanceRepository

class MaintenanceRepositoryImpl(
    private val apiService: MaintenanceApiService
) : MaintenanceRepository {

    override suspend fun getMaintenanceForecast(equipmentId: Int): Result<MaintenanceForecast> {
        return try {
            val response = apiService.getMaintenanceForecast(equipmentId)

            if (response.isSuccessful) {
                response.body()?.let {
                    Result.success(it.toDomain())
                } ?: Result.failure(Exception("Respuesta vacía del servidor"))
            } else {
                Result.failure(Exception("Error al obtener pronóstico: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
