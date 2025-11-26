package com.example.ositopolarapp.core.di

import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import com.example.ositopolarapp.core.config.ApiConfig
import com.example.ositopolarapp.core.data.PreferencesManager
import com.example.ositopolarapp.core.data.network.AuthInterceptor
// Auth Imports
import com.example.ositopolarapp.features.authentication.data.api.AuthApiService
import com.example.ositopolarapp.features.authentication.data.local.AuthDatabase
import com.example.ositopolarapp.features.authentication.data.repository.AuthRepositoryImpl
import com.example.ositopolarapp.features.authentication.domain.repository.AuthRepository
import com.example.ositopolarapp.features.authentication.domain.usecase.*
// Equipment Imports
import com.example.ositopolarapp.features.equipment.data.api.EquipmentApiService
import com.example.ositopolarapp.features.equipment.data.repository.EquipmentRepositoryImpl
import com.example.ositopolarapp.features.equipment.domain.repository.EquipmentRepository
import com.example.ositopolarapp.features.equipment.domain.usecase.*
// Subscriptions Imports
import com.example.ositopolarapp.features.subscriptions.data.api.SubscriptionApiService
import com.example.ositopolarapp.features.subscriptions.data.repository.SubscriptionRepositoryImpl
import com.example.ositopolarapp.features.subscriptions.domain.repository.SubscriptionRepository
import com.example.ositopolarapp.features.subscriptions.domain.usecase.*
// Analytics, Profile, Rentals, ServiceRequests
import com.example.ositopolarapp.features.analytics.domain.repository.AnalyticsRepository
import com.example.ositopolarapp.features.rentals.domain.repository.RentalEquipmentRepository
import com.example.ositopolarapp.features.rentals.domain.usecase.*
import com.example.ositopolarapp.features.profile.data.api.ProfileApiService
import com.example.ositopolarapp.features.servicerequests.domain.repository.ServiceRequestRepository
import com.example.ositopolarapp.features.servicerequests.domain.usecase.*

import com.google.gson.GsonBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * 1. INTERFAZ PÚBLICA (Aquí agregué las variables que faltaban)
 */
interface AppContainer {
    // Core
    val preferencesManager: PreferencesManager
    val sharedPreferences: SharedPreferences

    // Repositorios
    val authRepository: AuthRepository
    val equipmentRepository: EquipmentRepository
    val subscriptionRepository: SubscriptionRepository
    val analyticsRepository: AnalyticsRepository
    val serviceRequestRepository: ServiceRequestRepository
    val rentalEquipmentRepository: RentalEquipmentRepository

    // APIs
    val profileApiService: ProfileApiService

    // --- AUTH USE CASES ---
    val signInUseCase: SignInUseCase
    val verifyTwoFactorUseCase: VerifyTwoFactorUseCase
    val checkAuthUseCase: CheckAuthUseCase
    val getCurrentUserUseCase: GetCurrentUserUseCase
    val logoutUseCase: LogoutUseCase
    val enable2FAUseCase: Enable2FAUseCase
    val disable2FAUseCase: Disable2FAUseCase
    val completeRegistrationUseCase: CompleteRegistrationUseCase
    val createRegistrationCheckoutUseCase: CreateRegistrationCheckoutUseCase

    // --- EQUIPMENT USE CASES ---
    val getAllEquipmentsUseCase: GetAllEquipmentsUseCase
    val getEquipmentByIdUseCase: GetEquipmentByIdUseCase
    val createEquipmentUseCase: CreateEquipmentUseCase
    val updateEquipmentOperationsUseCase: UpdateEquipmentOperationsUseCase
    val deleteEquipmentUseCase: DeleteEquipmentUseCase

    // --- SUBSCRIPTION USE CASES ---
    val getAllPlansUseCase: GetAllPlansUseCase
    val getPlanByIdUseCase: GetPlanByIdUseCase

    // --- RENTAL USE CASES ---
    val getRentalEquipmentUseCase: GetRentalEquipmentUseCase // <-- ESTA FALTABA (Error 1)
    val getRentalEquipmentByIdUseCase: GetRentalEquipmentByIdUseCase
    val createRentalRequestUseCase: CreateRentalRequestUseCase

    // --- SERVICE REQUEST USE CASES ---
    val getAllServiceRequestsUseCase: GetAllServiceRequestsUseCase // <-- ESTA FALTABA (Error 2)
    val createServiceRequestUseCase: CreateServiceRequestUseCase   // <-- ESTA FALTABA (Error 2)
    val addFeedbackUseCase: AddFeedbackUseCase                     // <-- ESTA FALTABA (Error 2)
}

/**
 * 2. IMPLEMENTACIÓN REAL
 */
class DefaultAppContainer(private val context: Context) : AppContainer {

    // ... (Configuración Core igual que antes) ...
    override val preferencesManager: PreferencesManager by lazy { PreferencesManager(context) }
    override val sharedPreferences: SharedPreferences by lazy { context.getSharedPreferences("ositopolar_prefs", Context.MODE_PRIVATE) }

    private val authDatabase: AuthDatabase by lazy {
        runBlocking(Dispatchers.IO) { Room.databaseBuilder(context, AuthDatabase::class.java, "ositopolar_db").fallbackToDestructiveMigration().build() }
    }
    private val authDao by lazy { authDatabase.authDao() }
    private val authInterceptor by lazy { AuthInterceptor() }
    private val httpClient: OkHttpClient by lazy {
        OkHttpClient.Builder().addInterceptor(authInterceptor).connectTimeout(ApiConfig.CONNECT_TIMEOUT, TimeUnit.SECONDS).readTimeout(ApiConfig.READ_TIMEOUT, TimeUnit.SECONDS).writeTimeout(ApiConfig.WRITE_TIMEOUT, TimeUnit.SECONDS).build()
    }
    private val gson by lazy { GsonBuilder().setLenient().serializeNulls().create() }
    private val retrofit: Retrofit by lazy {
        Retrofit.Builder().baseUrl(ApiConfig.BASE_URL).addConverterFactory(GsonConverterFactory.create(gson)).client(httpClient).build()
    }

    // --- AUTH ---
    val authApiService: AuthApiService by lazy { retrofit.create(AuthApiService::class.java) }
    override val authRepository: AuthRepository by lazy { AuthRepositoryImpl(apiService = authApiService, authDao = authDao) }

    override val signInUseCase by lazy { SignInUseCase(authRepository) }
    override val verifyTwoFactorUseCase by lazy { VerifyTwoFactorUseCase(authRepository) }
    override val checkAuthUseCase by lazy { CheckAuthUseCase(authRepository) }
    override val getCurrentUserUseCase by lazy { GetCurrentUserUseCase(authRepository) }
    override val logoutUseCase by lazy { LogoutUseCase(authRepository) }
    override val enable2FAUseCase by lazy { Enable2FAUseCase(authRepository) }
    override val disable2FAUseCase by lazy { Disable2FAUseCase(authRepository) }
    override val createRegistrationCheckoutUseCase by lazy { CreateRegistrationCheckoutUseCase(authRepository) }
    override val completeRegistrationUseCase by lazy { CompleteRegistrationUseCase(authRepository) }

    // --- EQUIPMENT ---
    val equipmentApiService: EquipmentApiService by lazy { retrofit.create(EquipmentApiService::class.java) }
    override val equipmentRepository: EquipmentRepository by lazy { EquipmentRepositoryImpl(apiService = equipmentApiService) }

    override val getAllEquipmentsUseCase by lazy { GetAllEquipmentsUseCase(equipmentRepository) }
    override val getEquipmentByIdUseCase by lazy { GetEquipmentByIdUseCase(equipmentRepository) }
    override val createEquipmentUseCase by lazy { CreateEquipmentUseCase(equipmentRepository) }
    override val updateEquipmentOperationsUseCase by lazy { UpdateEquipmentOperationsUseCase(equipmentRepository) }
    override val deleteEquipmentUseCase by lazy { DeleteEquipmentUseCase(equipmentRepository) }

    // --- SUBSCRIPTIONS ---
    val subscriptionApiService: SubscriptionApiService by lazy { retrofit.create(SubscriptionApiService::class.java) }
    override val subscriptionRepository: SubscriptionRepository by lazy { SubscriptionRepositoryImpl(apiService = subscriptionApiService) }

    override val getAllPlansUseCase by lazy { GetAllPlansUseCase(subscriptionRepository) }
    override val getPlanByIdUseCase by lazy { GetPlanByIdUseCase(subscriptionRepository) }

    // --- SERVICE REQUESTS ---
    val serviceRequestApiService: com.example.ositopolarapp.features.servicerequests.data.api.ServiceRequestApiService by lazy { retrofit.create(com.example.ositopolarapp.features.servicerequests.data.api.ServiceRequestApiService::class.java) }
    override val serviceRequestRepository: com.example.ositopolarapp.features.servicerequests.domain.repository.ServiceRequestRepository by lazy { com.example.ositopolarapp.features.servicerequests.data.repository.ServiceRequestRepositoryImpl(apiService = serviceRequestApiService) }

    override val getAllServiceRequestsUseCase by lazy { GetAllServiceRequestsUseCase(serviceRequestRepository) }
    override val createServiceRequestUseCase by lazy { CreateServiceRequestUseCase(serviceRequestRepository) }
    override val addFeedbackUseCase by lazy { AddFeedbackUseCase(serviceRequestRepository) }

    // --- PROFILE ---
    override val profileApiService: com.example.ositopolarapp.features.profile.data.api.ProfileApiService by lazy { retrofit.create(com.example.ositopolarapp.features.profile.data.api.ProfileApiService::class.java) }

    // --- ANALYTICS ---
    val analyticsApiService: com.example.ositopolarapp.features.analytics.data.api.AnalyticsApiService by lazy { retrofit.create(com.example.ositopolarapp.features.analytics.data.api.AnalyticsApiService::class.java) }
    override val analyticsRepository: AnalyticsRepository by lazy { com.example.ositopolarapp.features.analytics.data.repository.AnalyticsRepositoryImpl(apiService = analyticsApiService) }

    // --- RENTALS ---
    val rentalEquipmentApiService: com.example.ositopolarapp.features.rentals.data.api.RentalEquipmentApiService by lazy { retrofit.create(com.example.ositopolarapp.features.rentals.data.api.RentalEquipmentApiService::class.java) }
    override val rentalEquipmentRepository: RentalEquipmentRepository by lazy { com.example.ositopolarapp.features.rentals.data.repository.RentalEquipmentRepositoryImpl(apiService = rentalEquipmentApiService) }

    override val getRentalEquipmentUseCase by lazy { GetRentalEquipmentUseCase(repository = rentalEquipmentRepository) }
    override val getRentalEquipmentByIdUseCase by lazy { GetRentalEquipmentByIdUseCase(repository = rentalEquipmentRepository) }
    override val createRentalRequestUseCase by lazy { CreateRentalRequestUseCase(repository = rentalEquipmentRepository) }
}