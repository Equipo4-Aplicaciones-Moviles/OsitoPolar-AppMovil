package com.example.ositopolarapp.features.maintenance.data.mapper

import com.example.ositopolarapp.features.maintenance.data.dto.MaintenanceForecastDto
import com.example.ositopolarapp.features.maintenance.domain.model.MaintenanceForecast
import com.example.ositopolarapp.features.maintenance.domain.model.MaintenanceStatus

fun MaintenanceForecastDto.toDomain(): MaintenanceForecast {
    return MaintenanceForecast(
        equipmentId = equipmentId,
        equipmentName = equipmentName,
        lastMaintenanceDate = lastMaintenanceDate ?: "Sin datos",
        nextMaintenanceDue = nextMaintenanceDue ?: "Sin datos",
        daysUntilMaintenance = daysUntilMaintenance,
        status = parseStatus(status),
        maintenanceIntervalDays = maintenanceIntervalDays,
        reminderThresholdDays = reminderThresholdDays
    )
}

private fun parseStatus(status: String): MaintenanceStatus {
    return when (status.lowercase()) {
        "ok" -> MaintenanceStatus.OK
        "due_soon" -> MaintenanceStatus.DUE_SOON
        "overdue" -> MaintenanceStatus.OVERDUE
        else -> MaintenanceStatus.OK
    }
}
