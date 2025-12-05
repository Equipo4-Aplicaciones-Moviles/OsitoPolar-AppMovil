package com.example.ositopolarapp.features.maintenance.domain.model

/**
 * Domain model for maintenance forecast
 */
data class MaintenanceForecast(
    val equipmentId: Int,
    val equipmentName: String,
    val lastMaintenanceDate: String,
    val nextMaintenanceDue: String,
    val daysUntilMaintenance: Int,
    val status: MaintenanceStatus,
    val maintenanceIntervalDays: Int,
    val reminderThresholdDays: Int
) {
    fun getStatusLabel(): String = when (status) {
        MaintenanceStatus.OK -> "En orden"
        MaintenanceStatus.DUE_SOON -> "Próximo"
        MaintenanceStatus.OVERDUE -> "Vencido"
    }

    fun getStatusColor(): Long = when (status) {
        MaintenanceStatus.OK -> 0xFF66BB6A // Green
        MaintenanceStatus.DUE_SOON -> 0xFFFFA726 // Orange
        MaintenanceStatus.OVERDUE -> 0xFFE53935 // Red
    }

    fun getDaysLabel(): String {
        return when {
            daysUntilMaintenance < 0 -> "Vencido hace ${-daysUntilMaintenance} días"
            daysUntilMaintenance == 0 -> "Vence hoy"
            daysUntilMaintenance == 1 -> "Vence mañana"
            else -> "En $daysUntilMaintenance días"
        }
    }
}

enum class MaintenanceStatus {
    OK,
    DUE_SOON,
    OVERDUE
}
