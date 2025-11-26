package com.example.ositopolarapp.navigation

import android.net.Uri
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.launch
import com.example.ositopolarapp.core.di.*

// --- PANTALLAS ORIGINALES ---
import com.example.ositopolarapp.features.authentication.presentation.screens.LoginScreen
import com.example.ositopolarapp.features.authentication.presentation.screens.RegistrationScreen
import com.example.ositopolarapp.features.authentication.presentation.state.*
import com.example.ositopolarapp.features.equipment.presentation.screens.EquipmentDetailScreen
import com.example.ositopolarapp.features.equipment.presentation.screens.EquipmentListScreen
import com.example.ositopolarapp.features.subscriptions.presentation.screens.PlansScreen
import com.example.ositopolarapp.features.public.presentation.screens.ExploreHomeScreen
import com.example.ositopolarapp.features.public.presentation.screens.OwnerDashboardScreen
// Asegúrate de importar tus otras pantallas originales (PaymentHistory, Settings, etc.)
// si están en paquetes distintos.

// --- NUEVAS PANTALLAS DE ONBOARDING Y CLIENTE ---
import com.example.ositopolarapp.features.onboarding.presentation.screens.GetStartedScreen
import com.example.ositopolarapp.features.onboarding.presentation.screens.SelectProfileScreen
import com.example.ositopolarapp.features.authentication.presentation.screens.ClientLoginScreen
import com.example.ositopolarapp.features.authentication.presentation.screens.ClientRegisterScreen

import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun AppNavigation(
    appContainer: AppContainer,
    deepLinkUri: Uri?,
    onDeepLinkProcessed: () -> Unit
) {

    val navController = rememberNavController()

    // --- FACTORIES & VIEWMODELS ---
    val mainFactory = remember { MainViewModelFactory(appContainer) }
    val regFactory = remember { RegistrationViewModelFactory(appContainer) }
    val authFactory = remember { AuthViewModelFactory(appContainer) }
    val equipmentFactory = remember { EquipmentViewModelFactory(appContainer) }
    val plansFactory = remember { PlansViewModelFactory(appContainer) }
    val analyticsFactory = remember { AnalyticsViewModelFactory(appContainer) }
    val rentalFactory = remember { RentalViewModelFactory(appContainer) }

    val mainVM = viewModel<MainViewModel>(factory = mainFactory)
    val authState by mainVM.authState.collectAsState()
    val currentUser by mainVM.currentUser.collectAsState()

    // --- LÓGICA DE INICIO ---
    val startDestination = when (authState) {
        AuthState.Loading -> "loading_route" // Ruta temporal
        AuthState.LoggedIn -> "dashboard"
        AuthState.LoggedOut -> "get_started" // AHORA INICIA EN GET STARTED
    }

    // --- MANEJO DE DEEP LINKS (STRIPE) ---
    LaunchedEffect(deepLinkUri) {
        if (deepLinkUri != null && deepLinkUri.path == "/success") {
            android.util.Log.d("AppNavigation", "Deep link detected, navigating to registration completion")
            navController.navigate("registration-complete") {
                popUpTo("get_started") { inclusive = false }
                launchSingleTop = true
            }
        }
    }

    if (authState == AuthState.Loading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    NavHost(navController = navController, startDestination = startDestination) {

        // ==========================================
        // 1. FLUJO DE ONBOARDING (NUEVO)
        // ==========================================

        composable("get_started") {
            GetStartedScreen(
                onNavigateToSelectProfile = {
                    navController.navigate("select_profile")
                }
            )
        }

        composable("select_profile") {
            // Nota: He asumido que has actualizado SelectProfileScreen para pasar el tipo de perfil.
            // Si tu SelectProfileScreen solo tiene un botón "Continuar", navegará por defecto a ClientLogin
            // o necesitarás agregar lógica de selección allí.
            SelectProfileScreen(
                onNavigateNext = {
                    // AQUÍ DEFINES LA LÓGICA DE DERIVACIÓN
                    // Por ejemplo, podrías navegar a una pantalla intermedia o asumir Cliente por ahora.
                    // Si tienes estado en SelectProfileScreen, úsalo para decidir:

                    // Opción A: Ir a Login de Cliente
                    navController.navigate("client_login")

                    // Opción B (Si fuera empresa): navController.navigate("login")
                }
            )
        }

        // ==========================================
        // 2. AUTENTICACIÓN CLIENTE (NUEVO)
        // ==========================================

        composable("client_login") {
            ClientLoginScreen(
                onLoginSuccess = {
                    // Al loguearse exitosamente, ir al Dashboard
                    // (Aquí deberías llamar a tu ViewModel de Auth real)
                    navController.navigate("dashboard") {
                        popUpTo("get_started") { inclusive = true }
                    }
                },
                onRegisterClicked = {
                    navController.navigate("client_register")
                },
                onForgotPasswordClicked = {
                    // Implementar lógica de olvido de contraseña
                }
            )
        }

        composable("client_register") {
            ClientRegisterScreen(
                onRegistrationSuccess = { username, password ->
                    // Al registrarse, ir al Dashboard o Login
                    navController.navigate("dashboard") {
                        popUpTo("get_started") { inclusive = true }
                    }
                },
                onSignInClicked = {
                    navController.popBackStack() // Volver al login
                }
            )
        }

        // ==========================================
        // 3. AUTENTICACIÓN EMPRESA (ORIGINAL)
        // ==========================================

        composable("explore") {
            ExploreHomeScreen(
                onNavigateToLogin = { navController.navigate("login") },
                onNavigateToPlans = { navController.navigate("plans") }
            )
        }

        composable("login") {
            LoginScreen(
                viewModel = viewModel(factory = authFactory),
                onLoginSuccess = {
                    navController.navigate("dashboard") {
                        popUpTo("login") { inclusive = true }
                    }
                },
                onGoToRegister = { navController.navigate("plans") }
            )
        }

        composable("plans") {
            PlansScreen(
                viewModel = viewModel(factory = plansFactory),
                onPlanSelected = { planId, userType ->
                    navController.navigate("register/$planId")
                },
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable("register/{planId}") { entry ->
            val planId = entry.arguments?.getString("planId")?.toIntOrNull() ?: 1
            RegistrationScreen(
                viewModel = viewModel(factory = regFactory),
                planId = planId,
                userType = "Owner",
                onRegistrationSuccess = { username, password ->
                    navController.navigate("generated-credentials/$username/$password") {
                        popUpTo("register/{planId}") { inclusive = true }
                    }
                }
            )
        }

        composable("generated-credentials/{username}/{password}") { entry ->
            val username = entry.arguments?.getString("username") ?: ""
            val password = entry.arguments?.getString("password") ?: ""
            com.example.ositopolarapp.features.authentication.presentation.screens.GeneratedCredentialsScreen(
                username = username,
                password = password,
                onNavigateToLogin = {
                    navController.navigate("login") {
                        popUpTo("get_started") { inclusive = false }
                    }
                }
            )
        }

        // ==========================================
        // 4. LÓGICA DE REGISTRO COMPLETO (STRIPE)
        // ==========================================

        composable("registration-complete") {
            val registrationViewModel = viewModel<RegistrationViewModel>(factory = regFactory)
            val uiState by registrationViewModel.uiState.collectAsState()
            val context = androidx.compose.ui.platform.LocalContext.current
            var hasProcessedDeepLink by remember { mutableStateOf(false) }

            LaunchedEffect(deepLinkUri) {
                if (deepLinkUri != null && deepLinkUri.path == "/success" && !hasProcessedDeepLink) {
                    hasProcessedDeepLink = true
                    val sessionId = deepLinkUri.getQueryParameter("session_id")
                    if (sessionId != null) {
                        registrationViewModel.completeRegistration(sessionId)
                        onDeepLinkProcessed()
                    } else {
                        android.widget.Toast.makeText(context, "Error: No session ID", android.widget.Toast.LENGTH_LONG).show()
                        onDeepLinkProcessed()
                        navController.navigate("plans") { popUpTo("get_started") { inclusive = false } }
                    }
                }
            }

            // Lógica de UI para éxito/error (Copiada de tu original)
            LaunchedEffect(uiState.registrationComplete) {
                if (uiState.registrationComplete && uiState.generatedUsername != null) {
                    if (uiState.generatedPassword != null) {
                        navController.navigate("generated-credentials/${uiState.generatedUsername}/${uiState.generatedPassword}") {
                            popUpTo("get_started") { inclusive = false }
                        }
                    } else {
                        // Fallback error
                        kotlinx.coroutines.delay(2000)
                        navController.navigate("login") { popUpTo("get_started") { inclusive = false } }
                    }
                }
            }

            // UI de Carga/Error
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                if (uiState.error != null) {
                    Text("Error: ${uiState.error}")
                    // ... (Tu UI completa de error aquí si la deseas restaurar)
                } else {
                    CircularProgressIndicator()
                }
            }
        }

        // ==========================================
        // 5. DASHBOARD Y CARACTERÍSTICAS
        // ==========================================

        composable("dashboard") {
            OwnerDashboardScreen(
                appContainer = appContainer,
                navController = navController,
                username = currentUser?.username ?: "Usuario",
                userType = currentUser?.userType ?: "Owner",
                profileId = currentUser?.profileId ?: 0,
                requires2FA = currentUser?.requires2FA ?: false,
                onLogout = {
                    mainVM.logout()
                    navController.navigate("get_started") {
                        popUpTo("dashboard") { inclusive = true }
                    }
                }
            )
        }

        // Rutas de Equipos
        composable("equipment/detail/{equipmentId}") { entry ->
            val equipmentId = entry.arguments?.getString("equipmentId")?.toIntOrNull()
            if (equipmentId != null) {
                EquipmentDetailScreen(
                    equipmentId = equipmentId,
                    viewModel = viewModel(factory = equipmentFactory),
                    onNavigateBack = { navController.popBackStack() },
                    onNavigateToAnalytics = { id -> navController.navigate("equipment/analytics/$id") }
                )
            }
        }

        composable("equipment/analytics/{equipmentId}") { entry ->
            val equipmentId = entry.arguments?.getString("equipmentId")?.toIntOrNull()
            if (equipmentId != null) {
                val equipmentViewModel = viewModel<com.example.ositopolarapp.features.equipment.presentation.state.EquipmentDetailViewModel>(factory = equipmentFactory)
                val analyticsViewModel = viewModel<com.example.ositopolarapp.features.analytics.presentation.state.AnalyticsViewModel>(factory = analyticsFactory)

                LaunchedEffect(equipmentId) {
                    equipmentViewModel.loadEquipment(equipmentId)
                    analyticsViewModel.loadAnalytics(equipmentId)
                    analyticsViewModel.loadAdvancedAnalytics(equipmentId)
                }

                val equipmentUiState by equipmentViewModel.uiState.collectAsState()
                val analyticsUiState by analyticsViewModel.uiState.collectAsState()

                if (equipmentUiState.equipment != null) {
                    com.example.ositopolarapp.features.analytics.presentation.screens.EquipmentAnalyticsScreen(
                        equipment = equipmentUiState.equipment!!,
                        analyticsState = analyticsUiState,
                        onRefresh = { /*...*/ },
                        onNavigateBack = { navController.popBackStack() }
                    )
                } else {
                    Box(modifier = Modifier.fillMaxSize()) { CircularProgressIndicator() }
                }
            }
        }

        composable("equipment/add") {
            com.example.ositopolarapp.features.equipment.presentation.screens.AddEquipmentScreen(
                viewModel = viewModel(factory = equipmentFactory),
                ownerId = currentUser?.id ?: 0,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        // Rutas de Perfil
        composable("profile/payment-history") {
            com.example.ositopolarapp.features.profile.presentation.screens.PaymentHistoryScreen(
                userType = currentUser?.userType ?: "Owner",
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable("profile/2fa-settings") {
            val coroutineScope = rememberCoroutineScope()
            com.example.ositopolarapp.features.profile.presentation.screens.TwoFactorSettingsScreen(
                is2FAEnabled = currentUser?.requires2FA ?: false,
                onEnable2FA = {
                    coroutineScope.launch { currentUser?.username?.let { mainVM.enable2FA(it) } }
                },
                onDisable2FA = {
                    coroutineScope.launch { currentUser?.username?.let { mainVM.disable2FA(it) } }
                },
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable("profile/settings") {
            com.example.ositopolarapp.features.profile.presentation.screens.SettingsScreen(
                preferencesManager = appContainer.preferencesManager,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable("profile/upgrade-plan") {
            com.example.ositopolarapp.features.subscriptions.presentation.screens.UpgradePlansScreen(
                viewModel = viewModel(factory = plansFactory),
                currentPlanId = currentUser?.planId,
                userType = currentUser?.userType ?: "Owner",
                onUpgradeConfirmed = { /*...*/ navController.popBackStack() },
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable("profile/edit/{profileId}") { entry ->
            val profileId = entry.arguments?.getString("profileId")?.toIntOrNull()
            if (profileId != null) {
                com.example.ositopolarapp.features.profile.presentation.screens.UpdateProfileScreen(
                    profileApiService = appContainer.profileApiService,
                    profileId = profileId,
                    onNavigateBack = { navController.popBackStack() }
                )
            }
        }

        // Rutas de Renta y Servicios
        composable("rental/checkout/{equipmentId}") { entry ->
            val equipmentId = entry.arguments?.getString("equipmentId")?.toIntOrNull()
            val rentalViewModel = viewModel<com.example.ositopolarapp.features.rentals.presentation.viewmodel.RentalCatalogViewModel>(factory = rentalFactory)
            // ... (Lógica de carga de equipo original) ...
            // Asumiendo que existe el composable RentalCheckoutScreen
            Box(Modifier.fillMaxSize()) { Text("Rental Checkout Placeholder") }
        }

        composable("service-request/create") {
            val equipmentListVM = viewModel<com.example.ositopolarapp.features.equipment.presentation.state.EquipmentListViewModel>(factory = equipmentFactory)
            val equipmentUiState by equipmentListVM.uiState.collectAsState()

            com.example.ositopolarapp.features.servicerequests.presentation.screens.ServiceRequestWizardScreen(
                viewModel = viewModel(factory = com.example.ositopolarapp.core.di.ServiceRequestViewModelFactory(appContainer)),
                equipmentList = equipmentUiState.equipmentList,
                userId = currentUser?.id ?: 0,
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}