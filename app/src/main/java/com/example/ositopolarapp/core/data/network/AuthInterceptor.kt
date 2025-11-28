package com.example.ositopolarapp.core.data.network

import okhttp3.Interceptor
import okhttp3.Response

// Eliminamos el import incorrecto porque PreferencesManager está en este mismo paquete.

class AuthInterceptor(
    private val preferencesManager: PreferencesManager
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val token = preferencesManager.getToken()

        val requestBuilder = chain.request().newBuilder()

        if (!token.isNullOrEmpty()) {
            requestBuilder.addHeader("Authorization", "Bearer $token")
        }

        return chain.proceed(requestBuilder.build())
    }
}