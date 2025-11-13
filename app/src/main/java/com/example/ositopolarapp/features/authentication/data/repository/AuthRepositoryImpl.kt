// feature/registration/data/repository/AuthRepositoryImpl.kt
package com.example.ositopolarapp.features.authentication.data.repository
import com.example.ositopolarapp.features.authentication.data.api.AuthApiService
import com.example.ositopolarapp.features.authentication.data.dto.CreateRegistrationCheckoutRequest
import com.example.ositopolarapp.features.authentication.data.dto.CompleteRegistrationRequest
import com.example.ositopolarapp.features.authentication.data.mapper.toEntity
import com.example.ositopolarapp.features.authentication.domain.model.RegistrationCheckoutEntity
import com.example.ositopolarapp.features.authentication.domain.repository.AuthRepository

import com.example.ositopolarapp.features.authentication.data.dto.SignInRequest
import com.example.ositopolarapp.features.authentication.data.mapper.toEntity
import com.example.ositopolarapp.features.authentication.domain.model.AuthenticatedUserEntity
import java.io.IOException

// NOTA: 'apiService' deberías inyectarlo con Hilt/Koin,
// pero por ahora lo pasamos en el constructor.
class AuthRepositoryImpl(
    private val apiService: AuthApiService
) : AuthRepository {

    override suspend fun createRegistrationCheckout(
        planId: Int,
        userType: String
    ): Result<RegistrationCheckoutEntity> {
        return try {
            // Definimos las URLs de Deep Link para la app
            val successUrl = "ositopolar://registration/success"
            val cancelUrl = "ositopolar://registration/cancel"

            val request = CreateRegistrationCheckoutRequest(
                planId = planId,
                userType = userType,
                successUrl = successUrl,
                cancelUrl = cancelUrl
            )

            val response = apiService.createRegistrationCheckout(request)

            if (response.isSuccessful && response.body() != null) {
                // Éxito: Mapea el DTO a Entidad y devuelve
                Result.success(response.body()!!.toEntity())
            } else {
                // Error de la API (ej. 400, 500)
                Result.failure(Exception("Error: ${response.message()}"))
            }
        } catch (e: IOException) {
            // Error de red (sin internet)
            Result.failure(Exception("Network error: ${e.message}"))
        } catch (e: Exception) {
            // Otro error
            Result.failure(e)
        }
    }

    override suspend fun completeRegistration(
        request: CompleteRegistrationRequest
    ): Result<Unit> {
        return try {
            val response = apiService.completeRegistration(request)
            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                Result.failure(Exception("Error: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun signIn(
        username: String,
        password: String
    ): Result<AuthenticatedUserEntity> {
        return try {
            val request = SignInRequest(username = username, password = password)
            val response = apiService.signIn(request)

            if (response.isSuccessful && response.body() != null) {
                // Éxito: Mapea el DTO a Entidad y devuelve
                Result.success(response.body()!!.toEntity())
            } else {
                // Error de API (ej. 401 Credenciales incorrectas)
                Result.failure(Exception("Error: ${response.message()}"))
            }
        } catch (e: IOException) {
            // Error de red
            Result.failure(Exception("Network error: ${e.message}"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }


}