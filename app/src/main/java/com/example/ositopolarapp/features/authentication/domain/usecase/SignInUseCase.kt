package com.example.ositopolarapp.features.authentication.domain.usecase

import com.example.ositopolarapp.features.authentication.domain.repository.AuthRepository

class SignInUseCase(
    private val repository: AuthRepository
) {
    /**
     * 'operator fun invoke' permite llamar a la clase como si fuera una función
     */
    suspend operator fun invoke(username: String, password: String) =
        repository.signIn(username, password)
}