package com.example.ositopolarapp.core.data.network

import com.example.ositopolarapp.features.authentication.data.api.AuthApiService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    // TODO: Mueve esta URL a tu build.gradle (BuildConfig) o .env
    private const val BASE_URL = "http://10.0.2.2:8080/" // 10.0.2.2 es el "localhost" para el emulador

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