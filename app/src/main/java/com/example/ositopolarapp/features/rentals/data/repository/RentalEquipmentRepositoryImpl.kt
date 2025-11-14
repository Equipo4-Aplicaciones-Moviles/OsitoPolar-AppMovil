package com.example.ositopolarapp.features.rentals.data.repository

import android.util.Log
import com.example.ositopolarapp.features.rentals.data.api.RentalEquipmentApiService
import com.example.ositopolarapp.features.rentals.data.dto.RentalEquipmentDto
import com.example.ositopolarapp.features.rentals.domain.repository.RentalEquipmentRepository
import retrofit2.HttpException
import java.io.IOException

class RentalEquipmentRepositoryImpl(
    private val apiService: RentalEquipmentApiService
) : RentalEquipmentRepository {

    companion object {
        private const val TAG = "RentalEquipmentRepository"
    }

    override suspend fun getAllRentalEquipment(): Result<List<RentalEquipmentDto>> {
        return try {
            val response = apiService.getAllRentalEquipment()
            if (response.isSuccessful && response.body() != null) {
                Log.d(TAG, "Successfully fetched ${response.body()!!.size} rental equipment")
                Result.success(response.body()!!)
            } else {
                val errorMsg = "Failed to fetch rental equipment: ${response.message()}"
                Log.e(TAG, errorMsg)
                Result.failure(Exception(errorMsg))
            }
        } catch (e: IOException) {
            Log.e(TAG, "Network error while fetching rental equipment", e)
            Result.failure(Exception("Network error. Please check your connection."))
        } catch (e: HttpException) {
            Log.e(TAG, "HTTP error while fetching rental equipment: ${e.code()}", e)
            Result.failure(Exception("Server error: ${e.message()}"))
        } catch (e: Exception) {
            Log.e(TAG, "Unexpected error while fetching rental equipment", e)
            Result.failure(e)
        }
    }

    override suspend fun getRentalEquipmentById(equipmentId: Int): Result<RentalEquipmentDto> {
        return try {
            val response = apiService.getRentalEquipmentById(equipmentId)
            if (response.isSuccessful && response.body() != null) {
                Log.d(TAG, "Successfully fetched rental equipment ID: $equipmentId")
                Result.success(response.body()!!)
            } else {
                val errorMsg = "Failed to fetch rental equipment: ${response.message()}"
                Log.e(TAG, errorMsg)
                Result.failure(Exception(errorMsg))
            }
        } catch (e: IOException) {
            Log.e(TAG, "Network error while fetching rental equipment details", e)
            Result.failure(Exception("Network error. Please check your connection."))
        } catch (e: HttpException) {
            Log.e(TAG, "HTTP error while fetching rental equipment details: ${e.code()}", e)
            Result.failure(Exception("Server error: ${e.message()}"))
        } catch (e: Exception) {
            Log.e(TAG, "Unexpected error while fetching rental equipment details", e)
            Result.failure(e)
        }
    }
}
