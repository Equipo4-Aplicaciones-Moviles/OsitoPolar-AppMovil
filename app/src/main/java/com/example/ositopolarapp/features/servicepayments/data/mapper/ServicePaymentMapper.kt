package com.example.ositopolarapp.features.servicepayments.data.mapper

import com.example.ositopolarapp.features.servicepayments.data.dto.ServicePaymentCheckoutResponse
import com.example.ositopolarapp.features.servicepayments.domain.model.ServicePaymentCheckout

fun ServicePaymentCheckoutResponse.toDomain(): ServicePaymentCheckout {
    return ServicePaymentCheckout(
        checkoutUrl = checkoutUrl,
        sessionId = sessionId,
        totalAmount = totalAmount,
        platformFee = platformFee,
        providerAmount = providerAmount,
        platformFeePercentage = platformFeePercentage,
        workOrderId = workOrder?.id ?: 0,
        workOrderNumber = workOrder?.workOrderNumber ?: "N/A",
        workOrderTitle = workOrder?.title ?: "Servicio",
        providerName = workOrder?.providerName ?: "Proveedor"
    )
}
