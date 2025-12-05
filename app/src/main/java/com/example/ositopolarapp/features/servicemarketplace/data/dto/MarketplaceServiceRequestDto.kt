package com.example.ositopolarapp.features.servicemarketplace.data.dto

import com.google.gson.annotations.SerializedName

/**
 * DTO for service requests available in the marketplace
 */
data class MarketplaceServiceRequestDto(
    @SerializedName("id")
    val id: Int,

    @SerializedName("orderNumber")
    val orderNumber: String?,

    @SerializedName("title")
    val title: String,

    @SerializedName("description")
    val description: String?,

    @SerializedName("issueDetails")
    val issueDetails: String?,

    @SerializedName("requestTime")
    val requestTime: String?,

    @SerializedName("status")
    val status: String,

    @SerializedName("priority")
    val priority: String?,

    @SerializedName("urgency")
    val urgency: String?,

    @SerializedName("isEmergency")
    val isEmergency: Boolean,

    @SerializedName("serviceType")
    val serviceType: String?,

    @SerializedName("scheduledDate")
    val scheduledDate: String?,

    @SerializedName("timeSlot")
    val timeSlot: String?,

    @SerializedName("serviceAddress")
    val serviceAddress: String?,

    @SerializedName("equipmentId")
    val equipmentId: Int?,

    @SerializedName("equipment")
    val equipment: EquipmentBasicDto?
)

data class EquipmentBasicDto(
    @SerializedName("id")
    val id: Int
)

/**
 * Response when a provider accepts a service request
 */
data class AcceptServiceRequestResponse(
    @SerializedName("success")
    val success: Boolean,

    @SerializedName("message")
    val message: String,

    @SerializedName("serviceRequest")
    val serviceRequest: MarketplaceServiceRequestDto?
)
