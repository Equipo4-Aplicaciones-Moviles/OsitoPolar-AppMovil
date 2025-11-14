package com.example.ositopolarapp.features.paymenthistory.data.repository

import android.util.Log
import com.example.ositopolarapp.features.paymenthistory.data.api.PaymentHistoryApiService
import com.example.ositopolarapp.features.paymenthistory.data.dto.PaymentDetailsDto
import com.example.ositopolarapp.features.paymenthistory.data.dto.PaymentHistoryDto
import com.example.ositopolarapp.features.paymenthistory.domain.repository.PaymentHistoryRepository
import retrofit2.HttpException
import java.io.IOException

class PaymentHistoryRepositoryImpl(
    private val apiService: PaymentHistoryApiService
) : PaymentHistoryRepository {

    companion object {
        private const val TAG = "PaymentHistoryRepository"
    }

    override suspend fun getOwnerPaymentHistory(): Result<List<PaymentHistoryDto>> {
        return try {
            val response = apiService.getOwnerPaymentHistory()
            if (response.isSuccessful && response.body() != null) {
                Log.d(TAG, "Successfully fetched ${response.body()!!.size} payment records")
                Result.success(response.body()!!)
            } else {
                val errorMsg = "Failed to fetch payment history: ${response.message()}"
                Log.e(TAG, errorMsg)
                Result.failure(Exception(errorMsg))
            }
        } catch (e: IOException) {
            Log.e(TAG, "Network error while fetching payment history", e)
            Result.failure(Exception("Network error. Please check your connection."))
        } catch (e: HttpException) {
            Log.e(TAG, "HTTP error while fetching payment history: ${e.code()}", e)
            Result.failure(Exception("Server error: ${e.message()}"))
        } catch (e: Exception) {
            Log.e(TAG, "Unexpected error while fetching payment history", e)
            Result.failure(e)
        }
    }

    override suspend fun getPaymentDetails(paymentId: Int): Result<PaymentDetailsDto> {
        return try {
            val response = apiService.getPaymentDetails(paymentId)
            if (response.isSuccessful && response.body() != null) {
                Log.d(TAG, "Successfully fetched payment details for ID: $paymentId")
                Result.success(response.body()!!)
            } else {
                val errorMsg = "Failed to fetch payment details: ${response.message()}"
                Log.e(TAG, errorMsg)
                Result.failure(Exception(errorMsg))
            }
        } catch (e: IOException) {
            Log.e(TAG, "Network error while fetching payment details", e)
            Result.failure(Exception("Network error. Please check your connection."))
        } catch (e: HttpException) {
            Log.e(TAG, "HTTP error while fetching payment details: ${e.code()}", e)
            Result.failure(Exception("Server error: ${e.message()}"))
        } catch (e: Exception) {
            Log.e(TAG, "Unexpected error while fetching payment details", e)
            Result.failure(e)
        }
    }
}
