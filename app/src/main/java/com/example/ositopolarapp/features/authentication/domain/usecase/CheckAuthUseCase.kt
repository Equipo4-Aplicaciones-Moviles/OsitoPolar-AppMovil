package com.example.ositopolarapp.features.authentication.domain.usecase

import com.example.ositopolarapp.features.authentication.domain.repository.AuthRepository

class CheckAuthUseCase(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(): Result<Boolean> {
        // CORRECCIÓN: Usamos 'checkAuth' que es la función que definimos en el repositorio
        return repository.checkAuth()
    }
}