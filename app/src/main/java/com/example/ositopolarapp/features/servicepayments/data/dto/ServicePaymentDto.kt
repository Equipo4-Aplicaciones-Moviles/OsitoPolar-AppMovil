package com.example.ositopolarapp.features.servicepayments.data.dto

import com.google.gson.annotations.SerializedName

/**
 * Request to create a service payment checkout
 */
data class CreateServicePaymentRequest(
    @SerializedName("workOrderId")
    val workOrderId: Int,

    @SerializedName("successUrl")
    val successUrl: String? = null,

    @SerializedName("cancelUrl")
    val cancelUrl: String? = null
)

/**
 * Response from service payment checkout creation
 */
data class ServicePaymentCheckoutResponse(
    @SerializedName("checkoutUrl")
    val checkoutUrl: String,

    @SerializedName("sessionId")
    val sessionId: String,

    @SerializedName("totalAmount")
    val totalAmount: Double,

    @SerializedName("platformFee")
    val platformFee: Double,

    @SerializedName("providerAmount")
    val providerAmount: Double,

    @SerializedName("platformFeePercentage")
    val platformFeePercentage: Double,

    @SerializedName("workOrder")
    val workOrder: WorkOrderInfoDto?
)

data class WorkOrderInfoDto(
    @SerializedName("id")
    val id: Int,

    @SerializedName("workOrderNumber")
    val workOrderNumber: String?,

    @SerializedName("title")
    val title: String?,

    @SerializedName("providerName")
    val providerName: String?
)
