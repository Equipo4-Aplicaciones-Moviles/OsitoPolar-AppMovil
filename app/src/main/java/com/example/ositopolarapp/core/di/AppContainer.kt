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

    // Retrofit
    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(ApiConfig.BASE_URL)
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

    // Auth Use Cases
    val signInUseCase = SignInUseCase(authRepository)
    val verifyTwoFactorUseCase = VerifyTwoFactorUseCase(authRepository)
    val checkAuthUseCase = CheckAuthUseCase(authRepository)
    val getCurrentUserUseCase = GetCurrentUserUseCase(authRepository)
    val logoutUseCase = LogoutUseCase(authRepository)
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
}
