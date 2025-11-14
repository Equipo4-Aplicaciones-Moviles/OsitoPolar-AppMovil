package com.example.ositopolarapp.features.paymenthistory.domain.repository

import com.example.ositopolarapp.features.paymenthistory.data.dto.PaymentDetailsDto
import com.example.ositopolarapp.features.paymenthistory.data.dto.PaymentHistoryDto

interface PaymentHistoryRepository {
    suspend fun getOwnerPaymentHistory(): Result<List<PaymentHistoryDto>>
    suspend fun getPaymentDetails(paymentId: Int): Result<PaymentDetailsDto>
}
