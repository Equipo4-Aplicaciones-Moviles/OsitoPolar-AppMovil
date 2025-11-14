package com.example.ositopolarapp.features.servicerequests.data.dto

import com.google.gson.annotations.SerializedName

/**
 * Service Request DTO - Maps to backend API response
 */
data class ServiceRequestDto(
    @SerializedName("id")
    val id: Int,

    @SerializedName("orderNumber")
    val orderNumber: String,

    @SerializedName("title")
    val title: String,

    @SerializedName("description")
    val description: String,

    @SerializedName("issueDetails")
    val issueDetails: String?,

    @SerializedName("clientId")
    val clientId: Int?,

    @SerializedName("companyId")
    val companyId: Int?,

    @SerializedName("equipmentId")
    val equipmentId: Int,

    @SerializedName("assignedTechnicianId")
    val technicianId: Int?,

    @SerializedName("reportedByUserId")
    val reportedByUserId: Int,

    @SerializedName("serviceType")
    val serviceType: String,

    @SerializedName("priority")
    val priority: String,

    @SerializedName("urgency")
    val urgency: String,

    @SerializedName("isEmergency")
    val isEmergency: Boolean,

    @SerializedName("status")
    val status: String,

    @SerializedName("scheduledDate")
    val scheduledDate: String?,

    @SerializedName("timeSlot")
    val timeSlot: String?,

    @SerializedName("serviceAddress")
    val serviceAddress: String,

    @SerializedName("resolutionDetails")
    val resolutionDetails: String?,

    @SerializedName("technicianNotes")
    val technicianNotes: String?,

    @SerializedName("cost")
    val cost: Double?,

    @SerializedName("resolvedAt")
    val resolvedAt: String?,

    @SerializedName("customerFeedbackRating")
    val rating: Int?,

    @SerializedName("feedbackSubmissionDate")
    val feedbackSubmissionDate: String?,

    @SerializedName("createdAt")
    val createdAt: String,

    @SerializedName("updatedAt")
    val updatedAt: String?
)

/**
 * Request DTO for creating a new service request
 */
data class CreateServiceRequestRequest(
    @SerializedName("orderNumber")
    val orderNumber: String,

    @SerializedName("title")
    val title: String,

    @SerializedName("description")
    val description: String,

    @SerializedName("issueDetails")
    val issueDetails: String?,

    @SerializedName("clientId")
    val clientId: Int?,

    @SerializedName("companyId")
    val companyId: Int?,

    @SerializedName("equipmentId")
    val equipmentId: Int,

    @SerializedName("reportedByUserId")
    val reportedByUserId: Int,

    @SerializedName("serviceType")
    val serviceType: String,

    @SerializedName("priority")
    val priority: String,

    @SerializedName("urgency")
    val urgency: String,

    @SerializedName("isEmergency")
    val isEmergency: Boolean,

    @SerializedName("scheduledDate")
    val scheduledDate: String?,

    @SerializedName("timeSlot")
    val timeSlot: String?,

    @SerializedName("serviceAddress")
    val serviceAddress: String
)

/**
 * Request DTO for adding feedback/rating
 */
data class AddFeedbackRequest(
    @SerializedName("rating")
    val rating: Int
)
