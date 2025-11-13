package com.example.ositopolarapp.core.data.network

import com.example.ositopolarapp.features.authentication.domain.repository.AuthRepository // Importar
import kotlinx.coroutines.flow.firstOrNull // Importar
import kotlinx.coroutines.runBlocking // Importar
import okhttp3.Interceptor
import okhttp3.Response

/**
 * Interceptor de OkHttp que adjunta el token JWT (Bearer Token)
 * a todas las solicitudes autenticadas.
 */
class AuthInterceptor(
    private val authRepository: AuthRepository // Recibe la dependencia
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {

        // 1. Bloquea el hilo para obtener el token del repositorio asíncrono (Room)
        val token = runBlocking {
            authRepository.getSessionToken().firstOrNull() // Obtiene el valor más reciente del Flow
        }

        val request = chain.request()

        // 2. Si el token existe, añade el encabezado
        if (token.isNullOrEmpty()) {
            return chain.proceed(request) // Sin token, continúa
        }

        val newRequest = request.newBuilder()
            .header("Authorization", "Bearer $token") // <-- ¡Añade el token aquí!
            .build()

        // 3. Procede con la nueva solicitud
        return chain.proceed(newRequest)
    }
}