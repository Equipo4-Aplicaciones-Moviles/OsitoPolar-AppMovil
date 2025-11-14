package com.example.ositopolarapp.features.equipment.data.dto

import com.google.gson.annotations.SerializedName

/**
 * Request DTO for creating new equipment.
 * Matches backend API POST /api/v1/equipments request body.
 */
data class CreateEquipmentRequest(
    @SerializedName("name")
    val name: String,

    @SerializedName("type")
    val type: String,

    @SerializedName("model")
    val model: String,

    @SerializedName("manufacturer")
    val manufacturer: String,

    @SerializedName("serialNumber")
    val serialNumber: String,

    @SerializedName("code")
    val code: String,

    @SerializedName("cost")
    val cost: Double,

    @SerializedName("technicalDetails")
    val technicalDetails: String? = null,

    @SerializedName("currentTemperature")
    val currentTemperature: Double,

    @SerializedName("setTemperature")
    val setTemperature: Double,

    @SerializedName("optimalTemperatureMin")
    val optimalTemperatureMin: Double,

    @SerializedName("optimalTemperatureMax")
    val optimalTemperatureMax: Double,

    @SerializedName("locationName")
    val locationName: String,

    @SerializedName("locationAddress")
    val locationAddress: String,

    @SerializedName("locationLatitude")
    val locationLatitude: Double?,

    @SerializedName("locationLongitude")
    val locationLongitude: Double?,

    @SerializedName("energyConsumptionCurrent")
    val energyConsumptionCurrent: Double,

    @SerializedName("energyConsumptionUnit")
    val energyConsumptionUnit: String,

    @SerializedName("energyConsumptionAverage")
    val energyConsumptionAverage: Double,

    @SerializedName("isPoweredOn")
    val isPoweredOn: Boolean,

    @SerializedName("status")
    val status: String,

    @SerializedName("ownershipType")
    val ownershipType: String,

    @SerializedName("ownerId")
    val ownerId: Int,

    @SerializedName("ownerType")
    val ownerType: String
)
