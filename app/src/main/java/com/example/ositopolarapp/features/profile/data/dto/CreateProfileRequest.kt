package com.example.ositopolarapp.features.profile.data.dto

/**
 * Request to create a new profile
 */
data class CreateProfileRequest(
    val firstName: String,
    val lastName: String,
    val email: String,
    val street: String,
    val number: String,
    val city: String,
    val postalCode: String,
    val country: String
)
