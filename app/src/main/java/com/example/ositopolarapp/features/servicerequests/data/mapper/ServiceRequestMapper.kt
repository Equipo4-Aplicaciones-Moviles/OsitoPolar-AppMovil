package com.example.ositopolarapp.features.servicerequests.data.mapper

import com.example.ositopolarapp.features.servicerequests.data.dto.ServiceRequestDto
import com.example.ositopolarapp.features.servicerequests.domain.model.*

/**
 * Mapper to convert between ServiceRequest DTO and Domain Model
 */
object ServiceRequestMapper {

    fun toDomain(dto: ServiceRequestDto): ServiceRequest {
        return ServiceRequest(
            id = dto.id,
            orderNumber = dto.orderNumber,
            title = dto.title,
            description = dto.description,
            issueDetails = dto.issueDetails,
            clientId = dto.clientId,
            companyId = dto.companyId,
            equipmentId = dto.equipmentId,
            technicianId = dto.technicianId,
            reportedByUserId = dto.reportedByUserId,
            serviceType = parseServiceType(dto.serviceType),
            priority = parsePriority(dto.priority),
            urgency = parseUrgency(dto.urgency),
            isEmergency = dto.isEmergency,
            status = parseStatus(dto.status),
            scheduledDate = dto.scheduledDate,
            timeSlot = dto.timeSlot,
            serviceAddress = dto.serviceAddress,
            resolutionDetails = dto.resolutionDetails,
            technicianNotes = dto.technicianNotes,
            cost = dto.cost,
            resolvedAt = dto.resolvedAt,
            rating = dto.rating,
            feedbackSubmissionDate = dto.feedbackSubmissionDate,
            createdAt = dto.createdAt,
            updatedAt = dto.updatedAt
        )
    }

    fun toDomainList(dtoList: List<ServiceRequestDto>): List<ServiceRequest> {
        return dtoList.map { toDomain(it) }
    }

    private fun parseServiceType(value: String): ServiceType {
        return try {
            ServiceType.valueOf(value.uppercase())
        } catch (e: IllegalArgumentException) {
            ServiceType.REPAIR // Default fallback
        }
    }

    private fun parsePriority(value: String): Priority {
        return try {
            Priority.valueOf(value.uppercase())
        } catch (e: IllegalArgumentException) {
            Priority.MEDIUM // Default fallback
        }
    }

    private fun parseUrgency(value: String): Urgency {
        return try {
            Urgency.valueOf(value.uppercase())
        } catch (e: IllegalArgumentException) {
            Urgency.NORMAL // Default fallback
        }
    }

    private fun parseStatus(value: String): ServiceRequestStatus {
        return try {
            // Handle both underscore and camelCase variants
            val normalized = value.uppercase().replace(" ", "_")
            ServiceRequestStatus.valueOf(normalized)
        } catch (e: IllegalArgumentException) {
            ServiceRequestStatus.PENDING // Default fallback
        }
    }
}
