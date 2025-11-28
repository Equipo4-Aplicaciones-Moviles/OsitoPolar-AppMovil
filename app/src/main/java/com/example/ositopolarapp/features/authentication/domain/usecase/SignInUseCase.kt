package com.example.ositopolarapp.features.authentication.domain.usecase

import com.example.ositopolarapp.features.authentication.data.dto.SignInRequest
import com.example.ositopolarapp.features.authentication.data.dto.SignInResponse
import com.example.ositopolarapp.features.authentication.domain.repository.AuthRepository

class SignInUseCase(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(username: String, password: String): Result<SignInResponse> {
        // 1. Creamos el objeto que pide el repo
        val request = SignInRequest(username, password)

        // 2. Llamamos a la función 'login' (antes se llamaba signIn)
        return repository.login(request)
    }
}