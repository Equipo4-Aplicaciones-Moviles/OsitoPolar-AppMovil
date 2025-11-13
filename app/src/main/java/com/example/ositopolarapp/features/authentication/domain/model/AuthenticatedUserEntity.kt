package com.example.ositopolarapp.features.authentication.domain.model

/**
 * La entidad de Dominio que representa a un usuario logueado.
 */
data class AuthenticatedUserEntity(
    val id: Int,
    val username: String,
    val token: String,
    val userType: String,
    val profileId: Int,
    val requires2FA: Boolean
)