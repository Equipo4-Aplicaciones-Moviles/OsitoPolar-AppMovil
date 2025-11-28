package com.example.ositopolarapp.features.profile.data.dto

import com.google.gson.annotations.SerializedName

// SOLO debe estar esta clase. Si ves 'data class ProfileDto' aquí, es el error.
data class UpdateProfileRequest(
    @SerializedName("firstName") val firstName: String,
    @SerializedName("lastName") val lastName: String,
    @SerializedName("email") val email: String,
    @SerializedName("street") val street: String,
    @SerializedName("number") val number: String,
    @SerializedName("city") val city: String,
    @SerializedName("postalCode") val postalCode: String,
    @SerializedName("country") val country: String
)