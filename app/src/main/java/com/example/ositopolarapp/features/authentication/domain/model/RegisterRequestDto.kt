package com.example.ositopolarapp.features.authentication.domain.model

// Coincide con el Schema del request body de sign-up
data class RegisterRequestDto(
    val username: String,
    val password: String
)

