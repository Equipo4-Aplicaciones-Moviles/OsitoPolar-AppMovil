package com.example.ositopolarapp.core.di

import android.content.Context
import android.content.SharedPreferences
import com.example.ositopolarapp.core.data.network.AuthInterceptor
import com.example.ositopolarapp.core.data.network.PreferencesManager
import com.example.ositopolarapp.features.analytics.data.api.AnalyticsApiService
import com.example.ositopolarapp.features.analytics.data.repository.AnalyticsRepositoryImpl
import com.example.ositopolarapp.features.analytics.domain.repository.AnalyticsRepository
import com.example.ositopolarapp.features.authentication.data.api.AuthApiService
import com.example.ositopolarapp.features.authentication.data.repository.AuthRepositoryImpl
import com.example.ositopolarapp.features.authentication.domain.repository.AuthRepository
import com.example.ositopolarapp.features.equipment.data.api.EquipmentApiService
import com.example.ositopolarapp.features.equipment.data.repository.EquipmentRepositoryImpl
import com.example.ositopolarapp.features.equipment.domain.repository.EquipmentRepository
import com.example.ositopolarapp.features.rentals.data.api.RentalEquipmentApiService
import com.example.ositopolarapp.features.rentals.data.repository.RentalEquipmentRepositoryImpl
import com.example.ositopolarapp.features.rentals.domain.repository.RentalEquipmentRepository
import com.example.ositopolarapp.features.servicerequests.data.api.ServiceRequestApiService
import com.example.ositopolarapp.features.servicerequests.data.repository.ServiceRequestRepositoryImpl
import com.example.ositopolarapp.features.servicerequests.domain.repository.ServiceRequestRepository
import com.example.ositopolarapp.features.subscriptions.data.api.SubscriptionApiService
import com.example.ositopolarapp.features.profile.data.api.ProfileApiService
import com.example.ositopolarapp.features.subscriptions.data.repository.SubscriptionRepositoryImpl
import com.example.ositopolarapp.features.subscriptions.domain.repository.SubscriptionRepository
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Contenedor de Inyección de Dependencias.
 * Define qué objetos están disponibles para toda la app.
 */
interface AppContainer {
    // Core & Network
    val sharedPreferences: SharedPreferences
    val preferencesManager: PreferencesManager

    // Repositorios
    val authRepository: AuthRepository
    val equipmentRepository: EquipmentRepository
    val subscriptionRepository: SubscriptionRepository
    val analyticsRepository: AnalyticsRepository
    val rentalEquipmentRepository: RentalEquipmentRepository
    val serviceRequestRepository: ServiceRequestRepository

    // Servicios API (A veces útiles inyectarlos directo)
    val profileApiService: ProfileApiService
}

class DefaultAppContainer(private val context: Context) : AppContainer {

    // 1. CONFIGURACIÓN DE RED Y ALMACENAMIENTO

    override val sharedPreferences: SharedPreferences by lazy {
        context.getSharedPreferences("os_polar_prefs", Context.MODE_PRIVATE)
    }

    override val preferencesManager: PreferencesManager by lazy {
        PreferencesManager(context)
    }

    // El interceptor ahora usa PreferencesManager (Solución al error de bucle)
    private val authInterceptor: AuthInterceptor by lazy {
        AuthInterceptor(preferencesManager)
    }

    private val okHttpClient: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    // Configuración de Gson: no serializar nulls y campos @Transient
    private val gson: com.google.gson.Gson by lazy {
        com.google.gson.GsonBuilder()
            .excludeFieldsWithModifiers(java.lang.reflect.Modifier.TRANSIENT)
            .create()
    }

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            // Backend desplegado en Azure
            .baseUrl("https://ositopolar-api.grayground-d49718c1.eastus.azurecontainerapps.io/api/v1/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    // 2. CREACIÓN DE SERVICIOS API (Endpoints)

    private val authApiService: AuthApiService by lazy {
        retrofit.create(AuthApiService::class.java)
    }

    private val equipmentApiService: EquipmentApiService by lazy {
        retrofit.create(EquipmentApiService::class.java)
    }

    private val subscriptionApiService: SubscriptionApiService by lazy {
        retrofit.create(SubscriptionApiService::class.java)
    }

    override val profileApiService: ProfileApiService by lazy {
        retrofit.create(ProfileApiService::class.java)
    }

    private val analyticsApiService: AnalyticsApiService by lazy {
        retrofit.create(AnalyticsApiService::class.java)
    }

    private val rentalEquipmentApiService: RentalEquipmentApiService by lazy {
        retrofit.create(RentalEquipmentApiService::class.java)
    }

    private val serviceRequestApiService: ServiceRequestApiService by lazy {
        retrofit.create(ServiceRequestApiService::class.java)
    }

    // 3. IMPLEMENTACIÓN DE REPOSITORIOS

    override val authRepository: AuthRepository by lazy {
        // CORRECCIÓN: Ahora pasamos 'preferencesManager' como segundo parámetro
        AuthRepositoryImpl(authApiService, preferencesManager)
    }

    override val equipmentRepository: EquipmentRepository by lazy {
        EquipmentRepositoryImpl(equipmentApiService)
    }

    override val subscriptionRepository: SubscriptionRepository by lazy {
        SubscriptionRepositoryImpl(subscriptionApiService)
    }

    override val analyticsRepository: AnalyticsRepository by lazy {
        AnalyticsRepositoryImpl(analyticsApiService)
    }

    override val rentalEquipmentRepository: RentalEquipmentRepository by lazy {
        RentalEquipmentRepositoryImpl(rentalEquipmentApiService)
    }

    override val serviceRequestRepository: ServiceRequestRepository by lazy {
        ServiceRequestRepositoryImpl(serviceRequestApiService)
    }
}