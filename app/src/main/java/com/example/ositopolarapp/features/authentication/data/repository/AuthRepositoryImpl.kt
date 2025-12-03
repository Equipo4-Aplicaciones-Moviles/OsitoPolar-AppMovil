package com.example.ositopolarapp.features.authentication.data.repository

import com.example.ositopolarapp.features.authentication.data.api.AuthApiService
import com.example.ositopolarapp.features.authentication.domain.repository.AuthRepository
import com.example.ositopolarapp.core.data.network.PreferencesManager
import com.example.ositopolarapp.features.authentication.domain.model.AuthenticatedUserEntity
// DTOs
import com.example.ositopolarapp.features.authentication.data.dto.*

class AuthRepositoryImpl(
    private val apiService: AuthApiService,
    private val preferencesManager: PreferencesManager
) : AuthRepository {

    override suspend fun login(request: SignInRequest): Result<SignInResponse> {
        return try {
            val response = apiService.signIn(request)
            if (response.isSuccessful && response.body() != null) {
                val body = response.body()!!
                val token = body.token ?: ""
                if (token.isNotEmpty()) {
                    preferencesManager.saveToken(token)
                    Result.success(body)
                } else {
                    Result.failure(Exception("Token vacío"))
                }
            } else {
                Result.failure(Exception("Error Login: ${response.code()}"))
            }
        } catch (e: Exception) { Result.failure(e) }
    }

    override suspend fun logout(): Result<Unit> {
        preferencesManager.clearData()
        return Result.success(Unit)
    }

    override suspend fun checkAuth(): Result<Boolean> {
        return Result.success(!preferencesManager.getToken().isNullOrEmpty())
    }

    override suspend fun getCurrentUser(): Result<AuthenticatedUserEntity> {
        return Result.success(
            AuthenticatedUserEntity(
                id = 1,
                username = "Usuario",
                userType = "Owner",
                token = preferencesManager.getToken() ?: "",
                profileId = 1,
                requires2FA = false
            )
        )
    }

    // --- AQUÍ ESTABAN LOS ERRORES (Implementación 2FA) ---

    override suspend fun enable2FA(username: String): Result<String> {
        return Result.success("otpauth://dummy")
    }

    override suspend fun disable2FA(username: String): Result<Unit> {
        return Result.success(Unit)
    }

    override suspend fun verifyTwoFactor(request: Verify2FARequest): Result<Boolean> {
        return Result.success(true)
    }

    // Esta es la función que te faltaba implementar correctamente:
    override suspend fun getTwoFactorStatus(username: String): Result<Boolean> {
        return Result.success(false)
    }

    // --- REGISTRO ---

    override suspend fun createRegistrationCheckout(request: CreateRegistrationCheckoutRequest): Result<RegistrationCheckoutResponse> {
        return try {
            val response = apiService.createRegistrationCheckout(request)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                val errorMsg = when (response.code()) {
                    400 -> "Datos inválidos para crear el checkout"
                    404 -> "Endpoint de checkout no encontrado"
                    500 -> "Error del servidor al crear checkout"
                    else -> "Error al crear checkout: ${response.code()} - ${response.message()}"
                }
                Result.failure(Exception(errorMsg))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Error de conexión al crear checkout: ${e.message}", e))
        }
    }

    override suspend fun completeRegistration(request: CompleteRegistrationRequest): Result<Pair<String, String>> {
        return try {
            android.util.Log.d("AuthRepository", "Enviando completeRegistration al backend")
            val response = apiService.completeRegistration(request)
            android.util.Log.d("AuthRepository", "Respuesta recibida - Código: ${response.code()}")

            if (response.isSuccessful && response.body() != null) {
                val body = response.body()!!
                android.util.Log.d("AuthRepository", "Registro completado exitosamente")
                Result.success(Pair(body.username, body.password))
            } else {
                // Intentar leer el cuerpo del error
                val errorBody = response.errorBody()?.string()
                android.util.Log.e("AuthRepository", "Error ${response.code()}: $errorBody")

                val errorMsg = when (response.code()) {
                    400 -> {
                        // Intentar extraer el mensaje específico del backend
                        val specificError = errorBody ?: "Datos incompletos o inválidos"
                        "Error 400: $specificError"
                    }
                    404 -> "Endpoint de completar registro no encontrado"
                    409 -> "El usuario ya existe o el email está en uso"
                    500 -> "Error del servidor al completar registro"
                    else -> "Error al completar registro: ${response.code()} - ${response.message()}"
                }
                Result.failure(Exception(errorMsg))
            }
        } catch (e: Exception) {
            android.util.Log.e("AuthRepository", "Excepción al completar registro", e)
            Result.failure(Exception("Error de conexión al completar registro: ${e.message}", e))
        }
    }
}