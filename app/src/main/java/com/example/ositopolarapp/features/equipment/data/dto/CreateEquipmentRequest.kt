package com.example.ositopolarapp.features.equipment.data.dto

import com.google.gson.annotations.SerializedName

data class CreateEquipmentRequest(
    @SerializedName("ownerId") val ownerId: Int,
    @SerializedName("name") val name: String,
    @SerializedName("type") val type: String,
    @SerializedName("model") val model: String,
    @SerializedName("serialNumber") val serialNumber: String,
    @SerializedName("manufacturer") val manufacturer: String,
    @SerializedName("locationName") val locationName: String,
    @SerializedName("locationAddress") val address: String,
    @SerializedName("locationLatitude") val latitude: Double,
    @SerializedName("locationLongitude") val longitude: Double,

    // Valores por defecto
    @SerializedName("code") val code: String = "DEF-001",
    @SerializedName("cost") val cost: Double = 0.0,
    @SerializedName("currentTemperature") val currentTemperature: Double = 0.0,
    @SerializedName("setTemperature") val setTemperature: Double = 0.0,
    @SerializedName("optimalTemperatureMin") val optimalTemperatureMin: Double = -10.0,
    @SerializedName("optimalTemperatureMax") val optimalTemperatureMax: Double = 10.0,
    @SerializedName("energyConsumptionCurrent") val energyConsumptionCurrent: Double = 0.0,
    @SerializedName("energyConsumptionUnit") val energyConsumptionUnit: String = "kWh",
    @SerializedName("energyConsumptionAverage") val energyConsumptionAverage: Double = 0.0,
    @SerializedName("isPoweredOn") val isPoweredOn: Boolean = true,
    @SerializedName("status") val status: String = "Active",
    @SerializedName("ownershipType") val ownershipType: String = "Owned",
    @SerializedName("ownerType") val ownerType: String = "Company"
)