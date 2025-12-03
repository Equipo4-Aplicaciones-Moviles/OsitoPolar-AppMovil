package com.example.ositopolarapp.core.di

import androidx.room.Room
import com.example.ositopolarapp.core.data.network.AuthInterceptor
import com.example.ositopolarapp.core.data.network.RetrofitClient
import com.example.ositopolarapp.features.authentication.data.api.AuthApiService
import com.example.ositopolarapp.features.authentication.data.local.AuthDatabase
import com.example.ositopolarapp.features.authentication.data.repository.AuthRepositoryImpl
import com.example.ositopolarapp.features.authentication.domain.repository.AuthRepository
import com.example.ositopolarapp.features.authentication.domain.usecase.*
import com.example.ositopolarapp.features.equipment.data.api.EquipmentApiService
import com.example.ositopolarapp.features.equipment.data.repository.EquipmentRepositoryImpl
import com.example.ositopolarapp.features.equipment.domain.repository.EquipmentRepository
import com.example.ositopolarapp.features.equipment.domain.usecase.*
import com.example.ositopolarapp.features.subscriptions.data.api.SubscriptionApiService
import com.example.ositopolarapp.features.subscriptions.data.repository.SubscriptionRepositoryImpl
import com.example.ositopolarapp.features.subscriptions.domain.repository.SubscriptionRepository
import com.example.ositopolarapp.features.subscriptions.domain.usecase.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import android.content.Context // Importar
import android.content.SharedPreferences // Importar
import android.content.Context.MODE_PRIVATE
import com.example.ositopolarapp.core.data.PreferencesManager
import com.example.ositopolarapp.core.config.ApiConfig
import java.util.concurrent.TimeUnit
import com.google.gson.GsonBuilder
import java.nio.charset.StandardCharsets

class AppContainer(private val context: Context) {

    // Preferences Manager
    val preferencesManager: PreferencesManager by lazy {
        PreferencesManager(context)
    }

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
            .connectTimeout(ApiConfig.CONNECT_TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(ApiConfig.READ_TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(ApiConfig.WRITE_TIMEOUT, TimeUnit.SECONDS)
            .build()
    }

    // Gson configurado con UTF-8
    private val gson by lazy {
        GsonBuilder()
            .setLenient()
            .serializeNulls()
            .create()
    }

    // Retrofit
    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(ApiConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
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

    // Auth Use Cases
    val signInUseCase = SignInUseCase(authRepository)
    val verifyTwoFactorUseCase = VerifyTwoFactorUseCase(authRepository)
    val checkAuthUseCase = CheckAuthUseCase(authRepository)
    val getCurrentUserUseCase = GetCurrentUserUseCase(authRepository)
    val logoutUseCase = LogoutUseCase(authRepository)
    val enable2FAUseCase = Enable2FAUseCase(authRepository)
    val disable2FAUseCase = Disable2FAUseCase(authRepository)
    val createRegistrationCheckoutUseCase = CreateRegistrationCheckoutUseCase(authRepository)
    val completeRegistrationUseCase = CompleteRegistrationUseCase(authRepository)

    // ============ EQUIPMENT MODULE ============

    // API Service
    val equipmentApiService: EquipmentApiService by lazy {
        retrofit.create(EquipmentApiService::class.java)
    }

    // Repository
    val equipmentRepository: EquipmentRepository by lazy {
        EquipmentRepositoryImpl(apiService = equipmentApiService)
    }

    // Use Cases
    val getAllEquipmentsUseCase by lazy { GetAllEquipmentsUseCase(equipmentRepository) }
    val getEquipmentByIdUseCase by lazy { GetEquipmentByIdUseCase(equipmentRepository) }
    val createEquipmentUseCase by lazy { CreateEquipmentUseCase(equipmentRepository) }
    val updateEquipmentOperationsUseCase by lazy { UpdateEquipmentOperationsUseCase(equipmentRepository) }
    val deleteEquipmentUseCase by lazy { DeleteEquipmentUseCase(equipmentRepository) }

    // ============ SUBSCRIPTIONS MODULE ============

    // API Service
    val subscriptionApiService: SubscriptionApiService by lazy {
        retrofit.create(SubscriptionApiService::class.java)
    }

    // Repository
    val subscriptionRepository: SubscriptionRepository by lazy {
        SubscriptionRepositoryImpl(apiService = subscriptionApiService)
    }

    // Use Cases
    val getAllPlansUseCase by lazy { GetAllPlansUseCase(subscriptionRepository) }
    val getPlanByIdUseCase by lazy { GetPlanByIdUseCase(subscriptionRepository) }

    // ============ SERVICE REQUESTS MODULE ============

    // API Service
    val serviceRequestApiService: com.example.ositopolarapp.features.servicerequests.data.api.ServiceRequestApiService by lazy {
        retrofit.create(com.example.ositopolarapp.features.servicerequests.data.api.ServiceRequestApiService::class.java)
    }

    // Repository
    val serviceRequestRepository: com.example.ositopolarapp.features.servicerequests.domain.repository.ServiceRequestRepository by lazy {
        com.example.ositopolarapp.features.servicerequests.data.repository.ServiceRequestRepositoryImpl(
            apiService = serviceRequestApiService
        )
    }

    // Use Cases
    val getAllServiceRequestsUseCase by lazy {
        com.example.ositopolarapp.features.servicerequests.domain.usecase.GetAllServiceRequestsUseCase(serviceRequestRepository)
    }
    val createServiceRequestUseCase by lazy {
        com.example.ositopolarapp.features.servicerequests.domain.usecase.CreateServiceRequestUseCase(serviceRequestRepository)
    }
    val addFeedbackUseCase by lazy {
        com.example.ositopolarapp.features.servicerequests.domain.usecase.AddFeedbackUseCase(serviceRequestRepository)
    }

    // ============ PAYMENTS MODULE ============

    // API Service
    val paymentApiService: com.example.ositopolarapp.features.payments.data.api.PaymentApiService by lazy {
        retrofit.create(com.example.ositopolarapp.features.payments.data.api.PaymentApiService::class.java)
    }

    // ============ PROFILE MODULE ============

    // API Service
    val profileApiService: com.example.ositopolarapp.features.profile.data.api.ProfileApiService by lazy {
        retrofit.create(com.example.ositopolarapp.features.profile.data.api.ProfileApiService::class.java)
    }

    // ============ ANALYTICS MODULE ============

    // API Service
    val analyticsApiService: com.example.ositopolarapp.features.analytics.data.api.AnalyticsApiService by lazy {
        retrofit.create(com.example.ositopolarapp.features.analytics.data.api.AnalyticsApiService::class.java)
    }

    // Repository
    val analyticsRepository: com.example.ositopolarapp.features.analytics.domain.repository.AnalyticsRepository by lazy {
        com.example.ositopolarapp.features.analytics.data.repository.AnalyticsRepositoryImpl(
            apiService = analyticsApiService
        )
    }

    // ============ NOTIFICATIONS MODULE ============

    // API Service
    val notificationApiService: com.example.ositopolarapp.features.notifications.data.api.NotificationApiService by lazy {
        retrofit.create(com.example.ositopolarapp.features.notifications.data.api.NotificationApiService::class.java)
    }

    // Repository
    val notificationRepository: com.example.ositopolarapp.features.notifications.domain.repository.NotificationRepository by lazy {
        com.example.ositopolarapp.features.notifications.data.repository.NotificationRepositoryImpl(
            apiService = notificationApiService
        )
    }

    // ============ PAYMENT HISTORY MODULE ============

    // API Service
    val paymentHistoryApiService: com.example.ositopolarapp.features.paymenthistory.data.api.PaymentHistoryApiService by lazy {
        retrofit.create(com.example.ositopolarapp.features.paymenthistory.data.api.PaymentHistoryApiService::class.java)
    }

    // Repository
    val paymentHistoryRepository: com.example.ositopolarapp.features.paymenthistory.domain.repository.PaymentHistoryRepository by lazy {
        com.example.ositopolarapp.features.paymenthistory.data.repository.PaymentHistoryRepositoryImpl(
            apiService = paymentHistoryApiService
        )
    }

    // ============ RENTAL EQUIPMENT MODULE ============

    // API Service
    val rentalEquipmentApiService: com.example.ositopolarapp.features.rentals.data.api.RentalEquipmentApiService by lazy {
        retrofit.create(com.example.ositopolarapp.features.rentals.data.api.RentalEquipmentApiService::class.java)
    }

    // Repository
    val rentalEquipmentRepository: com.example.ositopolarapp.features.rentals.domain.repository.RentalEquipmentRepository by lazy {
        com.example.ositopolarapp.features.rentals.data.repository.RentalEquipmentRepositoryImpl(
            apiService = rentalEquipmentApiService
        )
    }

    // Use Cases
    val getRentalEquipmentUseCase: com.example.ositopolarapp.features.rentals.domain.usecase.GetRentalEquipmentUseCase by lazy {
        com.example.ositopolarapp.features.rentals.domain.usecase.GetRentalEquipmentUseCase(
            repository = rentalEquipmentRepository
        )
    }

    val getRentalEquipmentByIdUseCase: com.example.ositopolarapp.features.rentals.domain.usecase.GetRentalEquipmentByIdUseCase by lazy {
        com.example.ositopolarapp.features.rentals.domain.usecase.GetRentalEquipmentByIdUseCase(
            repository = rentalEquipmentRepository
        )
    }

    val createRentalRequestUseCase: com.example.ositopolarapp.features.rentals.domain.usecase.CreateRentalRequestUseCase by lazy {
        com.example.ositopolarapp.features.rentals.domain.usecase.CreateRentalRequestUseCase(
            repository = rentalEquipmentRepository
        )
    }

    // ============ SERVICE MARKETPLACE MODULE ============

    // API Service
    val serviceMarketplaceApiService: com.example.ositopolarapp.features.servicemarketplace.data.api.ServiceMarketplaceApiService by lazy {
        retrofit.create(com.example.ositopolarapp.features.servicemarketplace.data.api.ServiceMarketplaceApiService::class.java)
    }

    // Repository
    val serviceMarketplaceRepository: com.example.ositopolarapp.features.servicemarketplace.domain.repository.ServiceMarketplaceRepository by lazy {
        com.example.ositopolarapp.features.servicemarketplace.data.repository.ServiceMarketplaceRepositoryImpl(
            apiService = serviceMarketplaceApiService
        )
    }

    // Use Cases
    val getMarketplaceRequestsUseCase: com.example.ositopolarapp.features.servicemarketplace.domain.usecase.GetMarketplaceRequestsUseCase by lazy {
        com.example.ositopolarapp.features.servicemarketplace.domain.usecase.GetMarketplaceRequestsUseCase(
            repository = serviceMarketplaceRepository
        )
    }

    val acceptServiceRequestUseCase: com.example.ositopolarapp.features.servicemarketplace.domain.usecase.AcceptServiceRequestUseCase by lazy {
        com.example.ositopolarapp.features.servicemarketplace.domain.usecase.AcceptServiceRequestUseCase(
            repository = serviceMarketplaceRepository
        )
    }

    // ============ SERVICE PAYMENTS MODULE ============

    // API Service
    val servicePaymentApiService: com.example.ositopolarapp.features.servicepayments.data.api.ServicePaymentApiService by lazy {
        retrofit.create(com.example.ositopolarapp.features.servicepayments.data.api.ServicePaymentApiService::class.java)
    }

    // Repository
    val servicePaymentRepository: com.example.ositopolarapp.features.servicepayments.domain.repository.ServicePaymentRepository by lazy {
        com.example.ositopolarapp.features.servicepayments.data.repository.ServicePaymentRepositoryImpl(
            apiService = servicePaymentApiService
        )
    }

    // Use Cases
    val createServicePaymentUseCase: com.example.ositopolarapp.features.servicepayments.domain.usecase.CreateServicePaymentUseCase by lazy {
        com.example.ositopolarapp.features.servicepayments.domain.usecase.CreateServicePaymentUseCase(
            repository = servicePaymentRepository
        )
    }

    // ============ MAINTENANCE MODULE ============

    // API Service
    val maintenanceApiService: com.example.ositopolarapp.features.maintenance.data.api.MaintenanceApiService by lazy {
        retrofit.create(com.example.ositopolarapp.features.maintenance.data.api.MaintenanceApiService::class.java)
    }

    // Repository
    val maintenanceRepository: com.example.ositopolarapp.features.maintenance.domain.repository.MaintenanceRepository by lazy {
        com.example.ositopolarapp.features.maintenance.data.repository.MaintenanceRepositoryImpl(
            apiService = maintenanceApiService
        )
    }

    // Use Cases
    val getMaintenanceForecastUseCase: com.example.ositopolarapp.features.maintenance.domain.usecase.GetMaintenanceForecastUseCase by lazy {
        com.example.ositopolarapp.features.maintenance.domain.usecase.GetMaintenanceForecastUseCase(
            repository = maintenanceRepository
        )
    }
}
