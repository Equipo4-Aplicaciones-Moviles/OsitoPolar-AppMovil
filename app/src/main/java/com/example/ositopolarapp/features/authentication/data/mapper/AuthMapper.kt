package com.example.ositopolarapp.features.authentication.data.mapper

import com.example.ositopolarapp.features.authentication.data.dto.SignInResponse
import com.example.ositopolarapp.features.authentication.domain.model.AuthenticatedUserEntity

/**
 * Convierte el DTO de Red (SignInResponse) a la Entidad de Dominio.
 */
fun SignInResponse.toEntity(): AuthenticatedUserEntity {
    return AuthenticatedUserEntity(
        id = this.id,
        username = this.username,
        token = this.token,
        userType = this.userType,
        profileId = this.profileId,
        requires2FA = this.requires2FA,
        requiresTwoFactorSetup = this.requiresTwoFactorSetup,
        qrCodeDataUrl = this.qrCodeDataUrl,
        manualEntryKey = this.manualEntryKey
    )
}