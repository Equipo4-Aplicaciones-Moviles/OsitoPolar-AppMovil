package com.example.ositopolarapp.features.profile.data.dto

import com.google.gson.annotations.SerializedName

data class ProfileDto(
    @SerializedName("id") val id: Int,
    @SerializedName("username") val username: String,
    @SerializedName("email") val email: String,
    @SerializedName("firstName") val firstName: String?,
    @SerializedName("lastName") val lastName: String?,
    @SerializedName("userType") val userType: String,
    @SerializedName("planId") val planId: Int?,
    @SerializedName("street") val street: String?,
    @SerializedName("number") val number: String?,
    @SerializedName("city") val city: String?,
    @SerializedName("postalCode") val postalCode: String?,
    @SerializedName("country") val country: String?
)