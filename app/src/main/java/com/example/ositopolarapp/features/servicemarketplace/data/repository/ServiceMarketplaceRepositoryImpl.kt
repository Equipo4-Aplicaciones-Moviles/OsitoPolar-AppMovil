package com.example.ositopolarapp.features.servicemarketplace.data.repository

import com.example.ositopolarapp.features.servicemarketplace.data.api.ServiceMarketplaceApiService
import com.example.ositopolarapp.features.servicemarketplace.data.mapper.toDomain
import com.example.ositopolarapp.features.servicemarketplace.domain.model.MarketplaceServiceRequest
import com.example.ositopolarapp.features.servicemarketplace.domain.repository.AcceptResult
import com.example.ositopolarapp.features.servicemarketplace.domain.repository.ServiceMarketplaceRepository

class ServiceMarketplaceRepositoryImpl(
    private val apiService: ServiceMarketplaceApiService
) : ServiceMarketplaceRepository {

    override suspend fun getMarketplaceRequests(): Result<List<MarketplaceServiceRequest>> {
        return try {
            val response = apiService.getMarketplaceServiceRequests()
            if (response.isSuccessful) {
                val requests = response.body()?.map { it.toDomain() } ?: emptyList()
                Result.success(requests)
            } else {
                Result.failure(Exception("Error al cargar solicitudes: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun acceptServiceRequest(serviceRequestId: Int): Result<AcceptResult> {
        return try {
            val response = apiService.acceptServiceRequest(serviceRequestId)
            if (response.isSuccessful) {
                val body = response.body()
                Result.success(
                    AcceptResult(
                        success = body?.success ?: false,
                        message = body?.message ?: "Solicitud aceptada"
                    )
                )
            } else {
                Result.failure(Exception("Error al aceptar solicitud: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
