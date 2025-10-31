package com.example.ositopolarapp.core.domain.models

// Coincide con el Schema de la respuesta 200 de sign-in
data class LoginResponseDto(
    val id: Int,
    val username: String,
    val token: String
)