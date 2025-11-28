package com.example.ositopolarapp.features.equipment.data.dto

import com.google.gson.annotations.SerializedName

data class UpdateOperationsRequest(
    @SerializedName("status") val status: String,
    @SerializedName("currentTemperature") val currentTemperature: Double
)