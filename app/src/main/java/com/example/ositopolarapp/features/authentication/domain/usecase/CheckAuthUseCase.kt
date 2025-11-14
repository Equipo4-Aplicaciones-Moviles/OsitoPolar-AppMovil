package com.example.ositopolarapp.features.authentication.domain.usecase

import com.example.ositopolarapp.features.authentication.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow

/**
 * Caso de Uso para verificar si existe un token de sesión guardado.
 * Esto determina la pantalla de inicio (Login vs. Dashboard).
 */
class CheckAuthUseCase(
    private val repository: AuthRepository
) {
    // Devuelve un Flow que emitirá el token (String) o null.
    operator fun invoke(): Flow<String?> = repository.getSessionToken()
}