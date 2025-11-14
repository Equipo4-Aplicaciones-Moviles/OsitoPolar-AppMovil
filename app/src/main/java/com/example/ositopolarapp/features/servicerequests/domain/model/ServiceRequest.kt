package com.example.ositopolarapp.features.servicerequests.domain.model

import java.time.OffsetDateTime

/**
 * Service Request Domain Model
 *
 * Represents a service request for equipment maintenance, installation, or repair.
 * Matches backend ServiceRequest aggregate.
 */
data class ServiceRequest(
    val id: Int,
    val orderNumber: String,
    val title: String,
    val description: String,
    val issueDetails: String?,
    val clientId: Int?,
    val companyId: Int?,
    val equipmentId: Int,
    val technicianId: Int?,
    val reportedByUserId: Int,
    val serviceType: ServiceType,
    val priority: Priority,
    val urgency: Urgency,
    val isEmergency: Boolean,
    val status: ServiceRequestStatus,
    val scheduledDate: String?,
    val timeSlot: String?,
    val serviceAddress: String,
    val resolutionDetails: String?,
    val technicianNotes: String?,
    val cost: Double?,
    val resolvedAt: String?,
    val rating: Int?,
    val feedbackSubmissionDate: String?,
    val createdAt: String,
    val updatedAt: String?
) {
    fun hasTechnician(): Boolean = technicianId != null

    fun getSummary(): String = "[#$orderNumber] $title ($status)"

    fun canBeRated(): Boolean = status == ServiceRequestStatus.RESOLVED && rating == null

    fun getStatusColor(): String {
        return when (status) {
            ServiceRequestStatus.PENDING -> "#FFA000"
            ServiceRequestStatus.ACCEPTED -> "#1976D2"
            ServiceRequestStatus.IN_PROGRESS -> "#2196F3"
            ServiceRequestStatus.RESOLVED -> "#4CAF50"
            ServiceRequestStatus.REJECTED -> "#F44336"
            ServiceRequestStatus.CANCELLED -> "#9E9E9E"
        }
    }
}

enum class ServiceType {
    DIAGNOSTIC,
    PREVENTIVE,
    REPAIR,
    INSTALLATION,
    REMOVAL
}

enum class Priority {
    LOW,
    MEDIUM,
    HIGH,
    CRITICAL
}

enum class Urgency {
    LOW,
    NORMAL,
    HIGH,
    CRITICAL
}

enum class ServiceRequestStatus {
    PENDING,
    ACCEPTED,
    IN_PROGRESS,
    RESOLVED,
    REJECTED,
    CANCELLED
}
