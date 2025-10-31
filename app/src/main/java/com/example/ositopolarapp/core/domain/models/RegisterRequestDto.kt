package com.example.ositopolarapp.core.domain.models

// Coincide con el Schema del request body de sign-up
data class RegisterRequestDto(
    val username: String,
    val password: String
)

