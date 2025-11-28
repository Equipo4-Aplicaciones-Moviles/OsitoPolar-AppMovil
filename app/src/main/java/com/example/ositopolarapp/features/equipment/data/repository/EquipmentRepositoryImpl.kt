package com.example.ositopolarapp.features.equipment.data.repository

import com.example.ositopolarapp.features.equipment.data.api.EquipmentApiService
import com.example.ositopolarapp.features.equipment.data.dto.CreateEquipmentRequest
import com.example.ositopolarapp.features.equipment.data.dto.UpdateOperationsRequest
import com.example.ositopolarapp.features.equipment.data.mapper.toDomain
import com.example.ositopolarapp.features.equipment.domain.model.Equipment
import com.example.ositopolarapp.features.equipment.domain.repository.EquipmentRepository

class EquipmentRepositoryImpl(
    private val apiService: EquipmentApiService
) : EquipmentRepository {

    override suspend fun getAllEquipments(): Result<List<Equipment>> {
        return try {
            val response = apiService.getAllEquipments()
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!.map { it.toDomain() })
            } else {
                Result.failure(Exception("Error al cargar equipos: ${response.code()}"))
            }
        } catch (e: Exception) { Result.failure(e) }
    }

    override suspend fun getEquipmentById(id: Int): Result<Equipment> {
        return try {
            val response = apiService.getEquipmentById(id)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!.toDomain())
            } else {
                Result.failure(Exception("Equipo no encontrado"))
            }
        } catch (e: Exception) { Result.failure(e) }
    }

    override suspend fun createEquipment(
        ownerId: Int, name: String, type: String, model: String,
        serialNumber: String, brand: String, location: String,
        address: String, latitude: Double, longitude: Double
    ): Result<Equipment> {
        return try {
            val request = CreateEquipmentRequest(
                ownerId = ownerId,
                name = name,
                type = type,
                model = model,
                serialNumber = serialNumber,
                manufacturer = brand,
                locationName = location,
                address = address,
                latitude = latitude,
                longitude = longitude
            )

            val response = apiService.createEquipment(request)

            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!.toDomain())
            } else {
                val msg = if (response.code() == 401) "Sesión inválida" else "Error: ${response.code()}"
                Result.failure(Exception(msg))
            }
        } catch (e: Exception) { Result.failure(e) }
    }

    override suspend fun updateOperations(id: Int, status: String, temperature: Double): Result<Equipment> {
        return try {
            // AHORA ESTO FUNCIONARÁ PORQUE EL DTO YA TIENE LOS CAMPOS CORRECTOS
            val request = UpdateOperationsRequest(
                status = status,
                currentTemperature = temperature
            )

            val response = apiService.updateOperations(id, request)

            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!.toDomain())
            } else {
                Result.failure(Exception("Error al actualizar"))
            }
        } catch (e: Exception) { Result.failure(e) }
    }

    override suspend fun deleteEquipment(id: Int): Result<Unit> {
        return try {
            val response = apiService.deleteEquipment(id)
            if (response.isSuccessful) Result.success(Unit)
            else Result.failure(Exception("Error al eliminar"))
        } catch (e: Exception) { Result.failure(e) }
    }
}