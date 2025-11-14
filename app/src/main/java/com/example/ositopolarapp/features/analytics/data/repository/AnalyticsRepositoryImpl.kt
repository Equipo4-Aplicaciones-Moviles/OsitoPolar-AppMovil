package com.example.ositopolarapp.features.analytics.data.repository

import android.util.Log
import com.example.ositopolarapp.features.analytics.data.api.AnalyticsApiService
import com.example.ositopolarapp.features.analytics.data.dto.*
import com.example.ositopolarapp.features.analytics.domain.repository.AnalyticsRepository
import retrofit2.HttpException
import java.io.IOException

class AnalyticsRepositoryImpl(
    private val apiService: AnalyticsApiService
) : AnalyticsRepository {

    companion object {
        private const val TAG = "AnalyticsRepository"
    }

    override suspend fun getEquipmentReadings(
        equipmentId: Int,
        type: String,
        hours: Int,
        limit: Int
    ): Result<ReadingsResponse> {
        return try {
            val response = apiService.getEquipmentReadings(equipmentId, type, hours, limit)
            if (response.isSuccessful && response.body() != null) {
                Log.d(TAG, "Successfully fetched readings for equipment $equipmentId")
                Result.success(response.body()!!)
            } else {
                val errorMsg = "Failed to fetch readings: ${response.message()}"
                Log.e(TAG, errorMsg)
                Result.failure(Exception(errorMsg))
            }
        } catch (e: IOException) {
            Log.e(TAG, "Network error while fetching readings", e)
            Result.failure(Exception("Network error. Please check your connection."))
        } catch (e: HttpException) {
            Log.e(TAG, "HTTP error while fetching readings: ${e.code()}", e)
            Result.failure(Exception("Server error: ${e.message()}"))
        } catch (e: Exception) {
            Log.e(TAG, "Unexpected error while fetching readings", e)
            Result.failure(e)
        }
    }

    override suspend fun getEquipmentSummaries(
        equipmentId: Int,
        type: String,
        days: Int
    ): Result<SummariesResponse> {
        return try {
            val response = apiService.getEquipmentSummaries(equipmentId, type, days)
            if (response.isSuccessful && response.body() != null) {
                Log.d(TAG, "Successfully fetched summaries for equipment $equipmentId")
                Result.success(response.body()!!)
            } else {
                val errorMsg = "Failed to fetch summaries: ${response.message()}"
                Log.e(TAG, errorMsg)
                Result.failure(Exception(errorMsg))
            }
        } catch (e: IOException) {
            Log.e(TAG, "Network error while fetching summaries", e)
            Result.failure(Exception("Network error. Please check your connection."))
        } catch (e: HttpException) {
            Log.e(TAG, "HTTP error while fetching summaries: ${e.code()}", e)
            Result.failure(Exception("Server error: ${e.message()}"))
        } catch (e: Exception) {
            Log.e(TAG, "Unexpected error while fetching summaries", e)
            Result.failure(e)
        }
    }

    override suspend fun getEquipmentOverview(
        equipmentIds: List<Int>,
        type: String
    ): Result<OverviewResponse> {
        return try {
            val idsString = equipmentIds.joinToString(",")
            val response = apiService.getEquipmentOverview(idsString, type)
            if (response.isSuccessful && response.body() != null) {
                Log.d(TAG, "Successfully fetched overview for ${equipmentIds.size} equipment")
                Result.success(response.body()!!)
            } else {
                val errorMsg = "Failed to fetch overview: ${response.message()}"
                Log.e(TAG, errorMsg)
                Result.failure(Exception(errorMsg))
            }
        } catch (e: IOException) {
            Log.e(TAG, "Network error while fetching overview", e)
            Result.failure(Exception("Network error. Please check your connection."))
        } catch (e: HttpException) {
            Log.e(TAG, "HTTP error while fetching overview: ${e.code()}", e)
            Result.failure(Exception("Server error: ${e.message()}"))
        } catch (e: Exception) {
            Log.e(TAG, "Unexpected error while fetching overview", e)
            Result.failure(e)
        }
    }
}
