package com.example.ositopolarapp.features.servicemarketplace.data.mapper

import com.example.ositopolarapp.features.servicemarketplace.data.dto.MarketplaceServiceRequestDto
import com.example.ositopolarapp.features.servicemarketplace.domain.model.*

fun MarketplaceServiceRequestDto.toDomain(): MarketplaceServiceRequest {
    return MarketplaceServiceRequest(
        id = id,
        orderNumber = orderNumber ?: "N/A",
        title = title,
        description = description ?: "",
        issueDetails = issueDetails ?: "",
        requestTime = requestTime ?: "",
        status = parseStatus(status),
        priority = parsePriority(priority),
        urgency = parseUrgency(urgency),
        isEmergency = isEmergency,
        serviceType = parseServiceType(serviceType),
        scheduledDate = scheduledDate,
        timeSlot = timeSlot,
        serviceAddress = serviceAddress ?: "Sin dirección",
        equipmentId = equipmentId
    )
}

private fun parseStatus(status: String?): ServiceRequestStatus {
    return when (status?.uppercase()) {
        "PENDING" -> ServiceRequestStatus.PENDING
        "ASSIGNED" -> ServiceRequestStatus.ASSIGNED
        "IN_PROGRESS", "INPROGRESS" -> ServiceRequestStatus.IN_PROGRESS
        "COMPLETED" -> ServiceRequestStatus.COMPLETED
        "CANCELLED" -> ServiceRequestStatus.CANCELLED
        else -> ServiceRequestStatus.PENDING
    }
}

private fun parsePriority(priority: String?): Priority {
    return when (priority?.uppercase()) {
        "HIGH" -> Priority.HIGH
        "MEDIUM" -> Priority.MEDIUM
        "LOW" -> Priority.LOW
        else -> Priority.MEDIUM
    }
}

private fun parseUrgency(urgency: String?): Urgency {
    return when (urgency?.uppercase()) {
        "IMMEDIATE" -> Urgency.IMMEDIATE
        "WITHIN_24_HOURS", "WITHIN24HOURS" -> Urgency.WITHIN_24_HOURS
        "WITHIN_WEEK", "WITHINWEEK" -> Urgency.WITHIN_WEEK
        "FLEXIBLE" -> Urgency.FLEXIBLE
        else -> Urgency.FLEXIBLE
    }
}

private fun parseServiceType(serviceType: String?): ServiceType {
    return when (serviceType?.uppercase()) {
        "MAINTENANCE" -> ServiceType.MAINTENANCE
        "REPAIR" -> ServiceType.REPAIR
        "INSTALLATION" -> ServiceType.INSTALLATION
        "INSPECTION" -> ServiceType.INSPECTION
        "EMERGENCY" -> ServiceType.EMERGENCY
        else -> ServiceType.MAINTENANCE
    }
}
