package com.example.ositopolarapp.features.subscriptions.data.repository

import android.util.Log
import com.example.ositopolarapp.features.subscriptions.data.api.SubscriptionApiService
import com.example.ositopolarapp.features.subscriptions.data.mapper.toEntity
import com.example.ositopolarapp.features.subscriptions.domain.model.Plan
import com.example.ositopolarapp.features.subscriptions.domain.repository.SubscriptionRepository
import retrofit2.HttpException
import java.io.IOException

/**
 * Implementation of SubscriptionRepository.
 * Handles all subscription/plan-related API calls.
 */
class SubscriptionRepositoryImpl(
    private val apiService: SubscriptionApiService
) : SubscriptionRepository {

    companion object {
        private const val TAG = "SubscriptionRepository"
    }

    override suspend fun getAllPlans(userType: String?): Result<List<Plan>> {
        return try {
            val response = apiService.getAllPlans(userType)

            if (response.isSuccessful && response.body() != null) {
                val plans = response.body()!!.map { it.toEntity() }
                Log.d(TAG, "Successfully fetched ${plans.size} plans (userType: $userType)")
                Result.success(plans)
            } else {
                val errorMsg = "Failed to fetch plans: ${response.message()}"
                Log.e(TAG, errorMsg)
                Result.failure(Exception(errorMsg))
            }
        } catch (e: IOException) {
            Log.e(TAG, "Network error while fetching plans", e)
            Result.failure(Exception("Network error. Please check your connection."))
        } catch (e: HttpException) {
            Log.e(TAG, "HTTP error while fetching plans: ${e.code()}", e)
            Result.failure(Exception("Server error: ${e.message()}"))
        } catch (e: Exception) {
            Log.e(TAG, "Unexpected error while fetching plans", e)
            Result.failure(e)
        }
    }

    override suspend fun getPlanById(planId: Int): Result<Plan> {
        return try {
            val response = apiService.getPlanById(planId)

            if (response.isSuccessful && response.body() != null) {
                val plan = response.body()!!.toEntity()
                Log.d(TAG, "Successfully fetched plan: ${plan.planName}")
                Result.success(plan)
            } else {
                val errorMsg = when (response.code()) {
                    404 -> "Plan not found"
                    else -> "Failed to fetch plan: ${response.message()}"
                }
                Log.e(TAG, errorMsg)
                Result.failure(Exception(errorMsg))
            }
        } catch (e: IOException) {
            Log.e(TAG, "Network error while fetching plan by ID", e)
            Result.failure(Exception("Network error. Please check your connection."))
        } catch (e: HttpException) {
            Log.e(TAG, "HTTP error while fetching plan by ID: ${e.code()}", e)
            Result.failure(Exception("Server error: ${e.message()}"))
        } catch (e: Exception) {
            Log.e(TAG, "Unexpected error while fetching plan by ID", e)
            Result.failure(e)
        }
    }
}
