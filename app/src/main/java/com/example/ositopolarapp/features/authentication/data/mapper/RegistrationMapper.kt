package com.example.ositopolarapp.features.authentication.data.mapper
import com.example.ositopolarapp.features.authentication.data.dto.RegistrationCheckoutResponse
import com.example.ositopolarapp.features.authentication.domain.model.RegistrationCheckoutEntity

// Convierte de DTO (data) -> Entity (domain)
fun RegistrationCheckoutResponse.toEntity(): RegistrationCheckoutEntity {
    return RegistrationCheckoutEntity(
        checkoutUrl = this.checkoutUrl,
        sessionId = this.sessionId
    )
}