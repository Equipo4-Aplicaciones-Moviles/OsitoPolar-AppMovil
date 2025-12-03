package com.example.ositopolarapp.features.maintenance.data.dto

import com.google.gson.annotations.SerializedName

/**
 * DTO for maintenance forecast response
 */
data class MaintenanceForecastDto(
    @SerializedName("equipmentId")
    val equipmentId: Int,

    @SerializedName("equipmentName")
    val equipmentName: String,

    @SerializedName("lastMaintenanceDate")
    val lastMaintenanceDate: String?,

    @SerializedName("nextMaintenanceDue")
    val nextMaintenanceDue: String?,

    @SerializedName("daysUntilMaintenance")
    val daysUntilMaintenance: Int,

    @SerializedName("status")
    val status: String, // "ok", "due_soon", "overdue"

    @SerializedName("maintenanceIntervalDays")
    val maintenanceIntervalDays: Int,

    @SerializedName("reminderThresholdDays")
    val reminderThresholdDays: Int
)
