package com.example.ositopolarapp.features.servicepayments.data.repository

import com.example.ositopolarapp.features.servicepayments.data.api.ServicePaymentApiService
import com.example.ositopolarapp.features.servicepayments.data.dto.CreateServicePaymentRequest
import com.example.ositopolarapp.features.servicepayments.data.mapper.toDomain
import com.example.ositopolarapp.features.servicepayments.domain.model.ServicePaymentCheckout
import com.example.ositopolarapp.features.servicepayments.domain.repository.ServicePaymentRepository

class ServicePaymentRepositoryImpl(
    private val apiService: ServicePaymentApiService
) : ServicePaymentRepository {

    override suspend fun createServicePaymentCheckout(
        workOrderId: Int,
        successUrl: String?,
        cancelUrl: String?
    ): Result<ServicePaymentCheckout> {
        return try {
            val request = CreateServicePaymentRequest(
                workOrderId = workOrderId,
                successUrl = successUrl,
                cancelUrl = cancelUrl
            )
            val response = apiService.createServicePaymentCheckout(request)

            if (response.isSuccessful) {
                response.body()?.let {
                    Result.success(it.toDomain())
                } ?: Result.failure(Exception("Respuesta vacía del servidor"))
            } else {
                Result.failure(Exception("Error al crear checkout: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
