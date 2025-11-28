package com.example.ositopolarapp.features.subscriptions.data.repository

import com.example.ositopolarapp.features.subscriptions.data.api.SubscriptionApiService
import com.example.ositopolarapp.features.subscriptions.data.mapper.toDomain
import com.example.ositopolarapp.features.subscriptions.domain.model.Plan
import com.example.ositopolarapp.features.subscriptions.domain.repository.SubscriptionRepository

class SubscriptionRepositoryImpl(
    private val apiService: SubscriptionApiService
) : SubscriptionRepository {

    override suspend fun getAllPlans(userType: String?): Result<List<Plan>> {
        return try {
            // Llamamos a la API
            val response = apiService.getAllPlans(userType)

            if (response.isSuccessful && response.body() != null) {
                // Convertimos lo que llega del server a lo que usa la app
                val plans = response.body()!!.map { it.toDomain() }
                Result.success(plans)
            } else {
                Result.failure(Exception("Error al cargar planes: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Error de conexión: ${e.message}"))
        }
    }

    override suspend fun getPlanById(planId: Int): Result<Plan> {
        return try {
            val response = apiService.getPlanById(planId)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!.toDomain())
            } else {
                Result.failure(Exception("Plan no encontrado"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}