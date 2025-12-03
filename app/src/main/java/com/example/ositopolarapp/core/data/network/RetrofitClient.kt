package com.example.ositopolarapp.core.data.network

import com.example.ositopolarapp.features.authentication.data.api.AuthApiService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    // URL del backend desplegado en Azure
    private const val BASE_URL = "https://ositopolar-api.grayground-d49718c1.eastus.azurecontainerapps.io/"

    // Crea un interceptor para ver las llamadas de red en el Logcat (¡súper útil!)
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    // Crea el cliente OkHttp
    private val httpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .build()

    // Crea la instancia de Retrofit
    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(httpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    /**
     * Esta es la instancia que usaremos en toda la app.
     * lazy { ... } asegura que se cree solo una vez, cuando se necesite.
     */
    val authApiService: AuthApiService by lazy {
        retrofit.create(AuthApiService::class.java)
    }

    val logging = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }
}