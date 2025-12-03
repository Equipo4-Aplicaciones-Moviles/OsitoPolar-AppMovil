package com.example.ositopolarapp.features.equipment.data.repository

import android.util.Log
import com.example.ositopolarapp.features.equipment.data.api.EquipmentApiService
import com.example.ositopolarapp.features.equipment.data.dto.UpdateOperationsRequest
import com.example.ositopolarapp.features.equipment.data.mapper.toCreateRequest
import com.example.ositopolarapp.features.equipment.data.mapper.toDto
import com.example.ositopolarapp.features.equipment.data.mapper.toEntity
import com.example.ositopolarapp.features.equipment.domain.model.Equipment
import com.example.ositopolarapp.features.equipment.domain.repository.EquipmentRepository
import com.example.ositopolarapp.features.equipment.domain.repository.LocationUpdate
import retrofit2.HttpException
import java.io.IOException

/**
 * Implementation of EquipmentRepository.
 * Handles all equipment-related API calls and error handling.
 */
class EquipmentRepositoryImpl(
    private val apiService: EquipmentApiService
) : EquipmentRepository {

    companion object {
        private const val TAG = "EquipmentRepository"
    }

    override suspend fun getAllEquipment(): Result<List<Equipment>> {
        return try {
            val response = apiService.getAllEquipment()

            if (response.isSuccessful && response.body() != null) {
                val equipmentList = response.body()!!.map { it.toEntity() }
                Log.d(TAG, "Successfully fetched ${equipmentList.size} equipment items")
                Result.success(equipmentList)
            } else {
                val errorMsg = "Failed to fetch equipment: ${response.message()}"
                Log.e(TAG, errorMsg)
                Result.failure(Exception(errorMsg))
            }
        } catch (e: IOException) {
            Log.e(TAG, "Network error while fetching equipment", e)
            Result.failure(Exception("Network error. Please check your connection."))
        } catch (e: HttpException) {
            Log.e(TAG, "HTTP error while fetching equipment: ${e.code()}", e)
            Result.failure(Exception("Server error: ${e.message()}"))
        } catch (e: Exception) {
            Log.e(TAG, "Unexpected error while fetching equipment", e)
            Result.failure(e)
        }
    }

    override suspend fun getEquipmentByOwner(ownerId: Int): Result<List<Equipment>> {
        return try {
            val response = apiService.getEquipmentByOwner(ownerId)

            if (response.isSuccessful && response.body() != null) {
                val equipmentList = response.body()!!.map { it.toEntity() }
                Log.d(TAG, "Successfully fetched ${equipmentList.size} equipment items for owner $ownerId")
                Result.success(equipmentList)
            } else {
                val errorMsg = "Failed to fetch equipment for owner: ${response.message()}"
                Log.e(TAG, errorMsg)
                Result.failure(Exception(errorMsg))
            }
        } catch (e: IOException) {
            Log.e(TAG, "Network error while fetching equipment by owner", e)
            Result.failure(Exception("Network error. Please check your connection."))
        } catch (e: HttpException) {
            Log.e(TAG, "HTTP error while fetching equipment by owner: ${e.code()}", e)
            Result.failure(Exception("Server error: ${e.message()}"))
        } catch (e: Exception) {
            Log.e(TAG, "Unexpected error while fetching equipment by owner", e)
            Result.failure(e)
        }
    }

    override suspend fun getEquipmentById(equipmentId: Int): Result<Equipment> {
        return try {
            val response = apiService.getEquipmentById(equipmentId)

            if (response.isSuccessful && response.body() != null) {
                val equipment = response.body()!!.toEntity()
                Log.d(TAG, "Successfully fetched equipment: ${equipment.name}")
                Result.success(equipment)
            } else {
                val errorMsg = when (response.code()) {
                    403 -> "You do not have permission to access this equipment"
                    404 -> "Equipment not found"
                    else -> "Failed to fetch equipment: ${response.message()}"
                }
                Log.e(TAG, errorMsg)
                Result.failure(Exception(errorMsg))
            }
        } catch (e: IOException) {
            Log.e(TAG, "Network error while fetching equipment by ID", e)
            Result.failure(Exception("Network error. Please check your connection."))
        } catch (e: HttpException) {
            Log.e(TAG, "HTTP error while fetching equipment by ID: ${e.code()}", e)
            Result.failure(Exception("Server error: ${e.message()}"))
        } catch (e: Exception) {
            Log.e(TAG, "Unexpected error while fetching equipment by ID", e)
            Result.failure(e)
        }
    }

    override suspend fun createEquipment(equipment: Equipment): Result<Equipment> {
        return try {
            val request = equipment.toCreateRequest()
            Log.d(TAG, "Creating equipment with request: $request")
            val response = apiService.createEquipment(request)

            if (response.isSuccessful && response.body() != null) {
                val createdEquipment = response.body()!!.toEntity()
                Log.d(TAG, "Successfully created equipment: ${createdEquipment.name}")
                Result.success(createdEquipment)
            } else {
                // Get detailed error from response body
                val errorBody = response.errorBody()?.string()
                val errorMsg = "Failed to create equipment: ${response.code()} - ${response.message()} - Body: $errorBody"
                Log.e(TAG, errorMsg)
                Result.failure(Exception(errorBody ?: response.message()))
            }
        } catch (e: IOException) {
            Log.e(TAG, "Network error while creating equipment", e)
            Result.failure(Exception("Network error. Please check your connection."))
        } catch (e: HttpException) {
            Log.e(TAG, "HTTP error while creating equipment: ${e.code()}", e)
            Result.failure(Exception("Server error: ${e.message()}"))
        } catch (e: Exception) {
            Log.e(TAG, "Unexpected error while creating equipment", e)
            Result.failure(e)
        }
    }

    override suspend fun updateEquipment(equipmentId: Int, equipment: Equipment): Result<Equipment> {
        return try {
            val request = equipment.toCreateRequest()
            val response = apiService.updateEquipment(equipmentId, request)

            if (response.isSuccessful && response.body() != null) {
                val updatedEquipment = response.body()!!.toEntity()
                Log.d(TAG, "Successfully updated equipment: ${updatedEquipment.name}")
                Result.success(updatedEquipment)
            } else {
                val errorMsg = "Failed to update equipment: ${response.message()}"
                Log.e(TAG, errorMsg)
                Result.failure(Exception(errorMsg))
            }
        } catch (e: IOException) {
            Log.e(TAG, "Network error while updating equipment", e)
            Result.failure(Exception("Network error. Please check your connection."))
        } catch (e: HttpException) {
            Log.e(TAG, "HTTP error while updating equipment: ${e.code()}", e)
            Result.failure(Exception("Server error: ${e.message()}"))
        } catch (e: Exception) {
            Log.e(TAG, "Unexpected error while updating equipment", e)
            Result.failure(e)
        }
    }

    override suspend fun deleteEquipment(equipmentId: Int): Result<Unit> {
        return try {
            val response = apiService.deleteEquipment(equipmentId)

            if (response.isSuccessful) {
                Log.d(TAG, "Successfully deleted equipment ID: $equipmentId")
                Result.success(Unit)
            } else {
                val errorMsg = "Failed to delete equipment: ${response.message()}"
                Log.e(TAG, errorMsg)
                Result.failure(Exception(errorMsg))
            }
        } catch (e: IOException) {
            Log.e(TAG, "Network error while deleting equipment", e)
            Result.failure(Exception("Network error. Please check your connection."))
        } catch (e: HttpException) {
            Log.e(TAG, "HTTP error while deleting equipment: ${e.code()}", e)
            Result.failure(Exception("Server error: ${e.message()}"))
        } catch (e: Exception) {
            Log.e(TAG, "Unexpected error while deleting equipment", e)
            Result.failure(e)
        }
    }

    override suspend fun updateEquipmentOperations(
        equipmentId: Int,
        temperature: Double?,
        powerState: String?,
        location: LocationUpdate?
    ): Result<Equipment> {
        return try {
            val request = UpdateOperationsRequest(
                temperature = temperature,
                powerState = powerState,
                location = location?.toDto()
            )

            val response = apiService.updateEquipmentOperations(equipmentId, request)

            if (response.isSuccessful && response.body() != null) {
                val updatedEquipment = response.body()!!.toEntity()
                Log.d(TAG, "Successfully updated equipment operations: ${updatedEquipment.name}")
                Result.success(updatedEquipment)
            } else {
                val errorMsg = "Failed to update equipment operations: ${response.message()}"
                Log.e(TAG, errorMsg)
                Result.failure(Exception(errorMsg))
            }
        } catch (e: IOException) {
            Log.e(TAG, "Network error while updating equipment operations", e)
            Result.failure(Exception("Network error. Please check your connection."))
        } catch (e: HttpException) {
            Log.e(TAG, "HTTP error while updating equipment operations: ${e.code()}", e)
            Result.failure(Exception("Server error: ${e.message()}"))
        } catch (e: Exception) {
            Log.e(TAG, "Unexpected error while updating equipment operations", e)
            Result.failure(e)
        }
    }
}
