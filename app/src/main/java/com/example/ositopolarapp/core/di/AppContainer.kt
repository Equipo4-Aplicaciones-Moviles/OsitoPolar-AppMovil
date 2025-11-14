package com.example.ositopolarapp.core.di

import androidx.room.Room
import com.example.ositopolarapp.core.data.network.AuthInterceptor
import com.example.ositopolarapp.core.data.network.RetrofitClient
import com.example.ositopolarapp.features.authentication.data.api.AuthApiService
import com.example.ositopolarapp.features.authentication.data.local.AuthDatabase
import com.example.ositopolarapp.features.authentication.data.repository.AuthRepositoryImpl
import com.example.ositopolarapp.features.authentication.domain.repository.AuthRepository
import com.example.ositopolarapp.features.authentication.domain.usecase.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import android.content.Context // Importar
import android.content.SharedPreferences // Importar
import android.content.Context.MODE_PRIVATE

class AppContainer(private val context: Context) {

    // 🚀 Base de datos: se crea en un hilo IO
    private val authDatabase: AuthDatabase by lazy {
        runBlocking(Dispatchers.IO) {
            Room.databaseBuilder(
                context,
                AuthDatabase::class.java,
                "ositopolar"
            )
                .fallbackToDestructiveMigration()
                .build()
        }
    }

    private val authDao = authDatabase.authDao()

    // Interceptor (sin dependencias al inicio)
    private val authInterceptor by lazy { AuthInterceptor() }

    // Cliente HTTP
    private val httpClient: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .build()
    }

    // Retrofit
    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("http://10.0.2.2:8080/") // Cambia esto
            .addConverterFactory(GsonConverterFactory.create())
            .client(httpClient)
            .build()
    }

    val authApiService: AuthApiService by lazy {
        retrofit.create(AuthApiService::class.java)
    }

    // Repositorio
    val authRepository: AuthRepository by lazy {
        AuthRepositoryImpl(
            apiService = authApiService,
            authDao = authDao
        )
    }.also {
        authInterceptor.setAuthRepository(it.value)
    }

    val sharedPreferences: SharedPreferences by lazy {
        context.getSharedPreferences("reg_cache", Context.MODE_PRIVATE)
    }

    // Use Cases
    val signInUseCase = SignInUseCase(authRepository)
    val verifyTwoFactorUseCase = VerifyTwoFactorUseCase(authRepository)
    val checkAuthUseCase = CheckAuthUseCase(authRepository)
    val createRegistrationCheckoutUseCase = CreateRegistrationCheckoutUseCase(authRepository)
    val completeRegistrationUseCase = CompleteRegistrationUseCase(authRepository)
}
