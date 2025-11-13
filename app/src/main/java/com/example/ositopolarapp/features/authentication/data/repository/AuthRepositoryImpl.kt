// feature/registration/data/repository/AuthRepositoryImpl.kt
package com.example.ositopolarapp.features.authentication.data.repository
import com.example.ositopolarapp.features.authentication.data.api.AuthApiService
import com.example.ositopolarapp.features.authentication.data.dto.CreateRegistrationCheckoutRequest
import com.example.ositopolarapp.features.authentication.data.dto.CompleteRegistrationRequest
import com.example.ositopolarapp.features.authentication.data.mapper.toEntity
import com.example.ositopolarapp.features.authentication.domain.model.RegistrationCheckoutEntity
import com.example.ositopolarapp.features.authentication.domain.repository.AuthRepository

import com.example.ositopolarapp.features.authentication.data.dto.Verify2FARequest

import com.example.ositopolarapp.features.authentication.data.dto.SignInRequest
import com.example.ositopolarapp.features.authentication.data.mapper.toEntity
import com.example.ositopolarapp.features.authentication.domain.model.AuthenticatedUserEntity
import java.io.IOException
import com.example.ositopolarapp.features.authentication.data.local.AuthDao
import com.example.ositopolarapp.features.authentication.data.local.AuthToken
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import retrofit2.HttpException

// NOTA: 'apiService' deberías inyectarlo con Hilt/Koin,
// pero por ahora lo pasamos en el constructor.
class AuthRepositoryImpl(
    private val apiService: AuthApiService,
    private val authDao: AuthDao
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

            val responseDto = apiService.signIn(SignInRequest(username, password)).body()

            if (responseDto == null) {
                return Result.failure(Exception("Credenciales incorrectas o respuesta vacía."))
            }

            // 🚀 LÓGICA DE PERSISTENCIA DEL TOKEN:
            responseDto.token.let { tokenString ->
                val authToken = AuthToken(
                    token = tokenString,
                    // CORRECCIÓN 2: Eliminamos la referencia a expiryTimestamp (no existe en el DTO)
                    expiryDate = System.currentTimeMillis() + (7L * 24 * 60 * 60 * 1000)
                )
                authDao.insertToken(authToken) // Guarda el token en Room
            }

            // CORRECCIÓN 3: Usamos Result.success()
            Result.success(responseDto.toEntity())
        } catch (e: IOException) {
            Result.failure(Exception("Error de red. Asegúrate de estar conectado."))
        } catch (e: Exception) {
            // Atrapa errores de la API (401 Unauthorized, etc.)
            Result.failure(Exception("Fallo en el inicio de sesión: ${e.message}"))
        }
    }


    override suspend fun verifyTwoFactor(
        username: String,
        code: String
    ): Result<AuthenticatedUserEntity> {
        return try {
            val request = Verify2FARequest(username, code)
            val response = apiService.verifyTwoFactor(request)
            val responseDto = response.body()

            if (response.isSuccessful && responseDto != null) {

                // 1. PERSISTENCIA: Si la verificación es exitosa, guardamos el token
                responseDto.token.let { tokenString ->
                    val authToken = AuthToken(
                        token = tokenString,
                        // Asumimos 7 días de validez si el backend no proporciona un timestamp de expiración
                        expiryDate = System.currentTimeMillis() + (7L * 24 * 60 * 60 * 1000)
                    )
                    authDao.insertToken(authToken) // Guarda el token en Room
                }

                // 2. Éxito: Devolvemos la entidad de usuario
                Result.success(responseDto.toEntity())

            } else if (response.code() == 401) {
                // Código 401: Típicamente, credenciales inválidas (código 2FA incorrecto)
                Result.failure(Exception("Código de verificación incorrecto o expirado."))
            }
            else {
                // Otro error de la API
                Result.failure(Exception("Error al verificar el código: ${response.message()}"))
            }
        } catch (e: IOException) {
            // Error de red
            Result.failure(Exception("Error de red. Asegúrate de estar conectado."))
        } catch (e: HttpException) {
            // Manejo de errores HTTP (ej. 400 Bad Request)
            Result.failure(Exception("Fallo en la verificación: ${e.message}"))
        } catch (e: Exception) {
            // Otros errores
            Result.failure(e)
        }
    }

    // --- MÉTODOS DE ROOM (TOKEN) ---

    // CORRECCIÓN 4: Asegúrate de que este método esté en la interfaz
    override fun getSessionToken(): Flow<String?> {
        // Mapea la entidad AuthToken a solo el String del token
        return authDao.getToken().map { it?.token }
    }

    // CORRECCIÓN 4: Asegúrate de que este método esté en la interfaz
    override suspend fun signOut() {
        authDao.deleteToken() // Elimina todos los tokens
    }


}