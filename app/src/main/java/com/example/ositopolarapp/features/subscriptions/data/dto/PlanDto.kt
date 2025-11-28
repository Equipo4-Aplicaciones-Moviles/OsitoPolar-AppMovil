package com.example.ositopolarapp.features.subscriptions.data.dto

import com.google.gson.annotations.SerializedName

data class PlanDto(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("price") val price: Double,
    @SerializedName("description") val description: String,
    @SerializedName("features") val features: List<String>,
    @SerializedName("billingCycle") val billingCycle: String, // "Monthly", "Yearly"
    @SerializedName("maxEquipment") val maxEquipment: Int
)