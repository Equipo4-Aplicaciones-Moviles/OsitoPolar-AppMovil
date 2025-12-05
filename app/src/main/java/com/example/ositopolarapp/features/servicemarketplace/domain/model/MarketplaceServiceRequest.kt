package com.example.ositopolarapp.features.servicemarketplace.domain.model

/**
 * Domain entity for marketplace service requests
 */
data class MarketplaceServiceRequest(
    val id: Int,
    val orderNumber: String,
    val title: String,
    val description: String,
    val issueDetails: String,
    val requestTime: String,
    val status: ServiceRequestStatus,
    val priority: Priority,
    val urgency: Urgency,
    val isEmergency: Boolean,
    val serviceType: ServiceType,
    val scheduledDate: String?,
    val timeSlot: String?,
    val serviceAddress: String,
    val equipmentId: Int?
) {
    fun getPriorityColor(): Long = when (priority) {
        Priority.HIGH -> 0xFFE53935 // Red
        Priority.MEDIUM -> 0xFFFFA726 // Orange
        Priority.LOW -> 0xFF66BB6A // Green
    }

    fun getUrgencyLabel(): String = when (urgency) {
        Urgency.IMMEDIATE -> "Inmediato"
        Urgency.WITHIN_24_HOURS -> "Dentro de 24h"
        Urgency.WITHIN_WEEK -> "Esta semana"
        Urgency.FLEXIBLE -> "Flexible"
    }

    fun getServiceTypeLabel(): String = when (serviceType) {
        ServiceType.MAINTENANCE -> "Mantenimiento"
        ServiceType.REPAIR -> "Reparación"
        ServiceType.INSTALLATION -> "Instalación"
        ServiceType.INSPECTION -> "Inspección"
        ServiceType.EMERGENCY -> "Emergencia"
    }
}

enum class ServiceRequestStatus {
    PENDING,
    ASSIGNED,
    IN_PROGRESS,
    COMPLETED,
    CANCELLED
}

enum class Priority {
    LOW,
    MEDIUM,
    HIGH
}

enum class Urgency {
    IMMEDIATE,
    WITHIN_24_HOURS,
    WITHIN_WEEK,
    FLEXIBLE
}

enum class ServiceType {
    MAINTENANCE,
    REPAIR,
    INSTALLATION,
    INSPECTION,
    EMERGENCY
}
