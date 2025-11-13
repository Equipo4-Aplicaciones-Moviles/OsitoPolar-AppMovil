package com.example.ositopolarapp.core.di

import android.content.Context // Importar
import androidx.room.Room // Importar
import com.example.ositopolarapp.core.data.network.AuthInterceptor // Importar
import com.example.ositopolarapp.features.authentication.data.api.AuthApiService
import com.example.ositopolarapp.core.data.network.RetrofitClient
import com.example.ositopolarapp.features.authentication.data.local.AuthDatabase
import com.example.ositopolarapp.features.authentication.data.repository.AuthRepositoryImpl
import com.example.ositopolarapp.features.authentication.domain.repository.AuthRepository
import com.example.ositopolarapp.features.authentication.domain.usecase.CreateRegistrationCheckoutUseCase
import com.example.ositopolarapp.features.authentication.domain.usecase.CompleteRegistrationUseCase
import com.example.ositopolarapp.features.authentication.domain.usecase.SignInUseCase
import com.example.ositopolarapp.features.authentication.domain.usecase.VerifyTwoFactorUseCase
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

// TODO: Importa aquí tus futuros UseCases de Login
// import com.example.ositopolardefinitivo.feature.login.domain.usecase.SignInUseCase

/**
 * Contenedor de dependencias manual.
 * Construye y provee todas las instancias que la app necesita.
 */
class AppContainer(private val context: Context) {

    // --- 1. Capa DATA (API y Repos) ---

    //private val authApiService = RetrofitClient.authApiService

    private val authApiService by lazy {
        retrofit.create(AuthApiService::class.java)
    }

    // Room Database
    private val authDatabase: AuthDatabase by lazy { // <--- 2. INICIALIZA ROOM
        Room.databaseBuilder(
            context, // Usa el contexto recibido
            AuthDatabase::class.java,
            "osito_polar_db" // Nombre del archivo de la DB
        )
            .fallbackToDestructiveMigration() // Útil en desarrollo, la borra si hay un cambio de versión
            .build()
    }

    // Auth DAO
    private val authDao = authDatabase.authDao()
    val authRepository: AuthRepository by lazy {
        AuthRepositoryImpl(
            apiService = authApiService,
            authDao = authDao
        )
    }

    // --- 2. Capa DOMAIN (Use Cases) ---

    // Use Cases de Registro
    val createRegistrationCheckoutUseCase = CreateRegistrationCheckoutUseCase(authRepository)
    val completeRegistrationUseCase = CompleteRegistrationUseCase(authRepository)
    val signInUseCase = SignInUseCase(authRepository)
    // TODO: Cuando crees el SignInUseCase, añádelo aquí
    // val signInUseCase = SignInUseCase(authRepository)

    private val authInterceptor by lazy {
        AuthInterceptor(authRepository) // Pasa el repositorio para que pueda leer el token
    }

    private val httpClient by lazy {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        OkHttpClient.Builder()
            .addInterceptor(authInterceptor) // <-- ¡ADJUNTAMOS EL INTERCEPTOR DE TOKEN!
            .addInterceptor(loggingInterceptor)
            .build()
    }

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("http://10.0.2.2:8080")
            .client(httpClient) // Usamos el cliente con el Interceptor
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val verifyTwoFactorUseCase = VerifyTwoFactorUseCase(authRepository)

    // 🚀 Creamos el servicio de API












}