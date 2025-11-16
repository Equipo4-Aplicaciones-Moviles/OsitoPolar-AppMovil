package com.example.ositopolarapp.features.authentication.data.mapper

import com.example.ositopolarapp.features.authentication.data.dto.SignInResponse
import com.example.ositopolarapp.features.authentication.data.local.AuthToken
import com.example.ositopolarapp.features.authentication.domain.model.AuthenticatedUserEntity

/**
 * Convierte el DTO de Red (SignInResponse) a la Entidad de Dominio.
 */
fun SignInResponse.toEntity(): AuthenticatedUserEntity {
    return AuthenticatedUserEntity(
        id = this.id ?: 0,
        username = this.username,
        token = this.token ?: "",
        userType = this.userType ?: "User",
        profileId = this.profileId ?: 0,
        requires2FA = this.requires2FA,
        requiresTwoFactorSetup = this.requiresTwoFactorSetup,
        qrCodeDataUrl = this.qrCodeDataUrl,
        manualEntryKey = this.manualEntryKey
    )
}

/**
 * Convierte AuthenticatedUserEntity a AuthToken (para guardar en Room)
 */
fun AuthenticatedUserEntity.toAuthToken(): AuthToken {
    return AuthToken(
        id = 1, // Always 1 for singleton pattern
        token = this.token,
        userId = this.id,
        username = this.username,
        userType = this.userType,
        profileId = this.profileId,
        requires2FA = this.requires2FA,
        requiresTwoFactorSetup = this.requiresTwoFactorSetup
    )
}

/**
 * Convierte AuthToken (Room entity) a AuthenticatedUserEntity (domain entity)
 */
fun AuthToken.toEntity(): AuthenticatedUserEntity {
    return AuthenticatedUserEntity(
        id = this.userId,
        username = this.username,
        token = this.token,
        userType = this.userType,
        profileId = this.profileId,
        requires2FA = this.requires2FA,
        requiresTwoFactorSetup = this.requiresTwoFactorSetup,
        qrCodeDataUrl = null,
        manualEntryKey = null
    )
}