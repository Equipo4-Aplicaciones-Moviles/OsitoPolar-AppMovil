package com.example.ositopolarapp.features.subscriptions.data.dto

import com.google.gson.annotations.SerializedName

/**
 * DTO for Plan/Subscription API responses.
 * Matches backend API field names exactly.
 */
data class PlanDto(
    @SerializedName("id")
    val id: Int = 0,

    @SerializedName("planName")
    val planName: String = "",

    @SerializedName("price")
    val price: Double = 0.0,

    @SerializedName("billingCycle")
    val billingCycle: String = "Monthly",

    @SerializedName("maxEquipment")
    val maxEquipment: Int? = null,

    @SerializedName("maxClients")
    val maxClients: Int? = null,

    @SerializedName("features")
    val features: List<String> = emptyList()
)
