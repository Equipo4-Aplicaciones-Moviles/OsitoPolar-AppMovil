package com.example.ositopolarapp.features.equipment.data.dto

import com.google.gson.annotations.SerializedName

data class EquipmentDto(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("type") val type: String?,
    @SerializedName("model") val model: String,
    @SerializedName("serialNumber") val serialNumber: String,
    @SerializedName("manufacturer") val manufacturer: String,
    @SerializedName("locationName") val locationName: String?,
    @SerializedName("locationAddress") val locationAddress: String?,
    @SerializedName("locationLatitude") val locationLatitude: Double?,
    @SerializedName("locationLongitude") val locationLongitude: Double?,
    @SerializedName("status") val status: String?,
    @SerializedName("currentTemperature") val currentTemperature: Double?,
    @SerializedName("ownerId") val ownerId: Int?,
    @SerializedName("imageUrl") val imageUrl: String?,
    @SerializedName("energyConsumptionCurrent") val energyConsumptionCurrent: Double?,
    @SerializedName("isPoweredOn") val isPoweredOn: Boolean?
)