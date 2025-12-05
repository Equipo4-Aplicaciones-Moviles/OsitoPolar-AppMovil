package com.example.ositopolarapp.features.equipment.data.dto

import com.google.gson.annotations.SerializedName

/**
 * DTO for Equipment API responses.
 * Field names match backend API exactly (using SerializedName for mapping).
 */
data class EquipmentDto(
    @SerializedName("id")
    val id: Int = 0,

    @SerializedName("name")
    val name: String = "",

    @SerializedName("type")
    val type: String = "",

    @SerializedName("model")
    val model: String = "",

    @SerializedName("manufacturer")
    val manufacturer: String = "",

    @SerializedName("serialNumber")
    val serialNumber: String = "",

    @SerializedName("code")
    val code: String = "",

    @SerializedName("cost")
    val cost: Double = 0.0,

    @SerializedName("technicalDetails")
    val technicalDetails: String? = null,

    // Temperature fields
    @SerializedName("currentTemperature")
    val currentTemperature: Double = 0.0,

    @SerializedName("setTemperature")
    val setTemperature: Double = 0.0,

    @SerializedName("optimalTemperatureMin")
    val optimalTemperatureMin: Double = 0.0,

    @SerializedName("optimalTemperatureMax")
    val optimalTemperatureMax: Double = 0.0,

    // Location fields
    @SerializedName("locationName")
    val locationName: String = "",

    @SerializedName("locationAddress")
    val locationAddress: String = "",

    @SerializedName("locationLatitude")
    val locationLatitude: Double? = null,

    @SerializedName("locationLongitude")
    val locationLongitude: Double? = null,

    // Energy fields
    @SerializedName("energyConsumptionCurrent")
    val energyConsumptionCurrent: Double = 0.0,

    @SerializedName("energyConsumptionUnit")
    val energyConsumptionUnit: String = "kWh",

    @SerializedName("energyConsumptionAverage")
    val energyConsumptionAverage: Double = 0.0,

    // Operational fields
    @SerializedName("isPoweredOn")
    val isPoweredOn: Boolean = true,

    @SerializedName("status")
    val status: String = "Active",

    // Ownership fields
    @SerializedName("ownerId")
    val ownerId: Int = 0,

    @SerializedName("ownerType")
    val ownerType: String = "User",

    @SerializedName("ownershipType")
    val ownershipType: String = "Owned",

    // Timestamps
    @SerializedName("createdAt")
    val createdAt: String? = null,

    @SerializedName("updatedAt")
    val updatedAt: String? = null,

    // Notes
    @SerializedName("notes")
    val notes: String? = null
)
