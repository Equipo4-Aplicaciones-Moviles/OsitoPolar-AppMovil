package com.example.ositopolarapp.navigation

import android.net.Uri
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize

// --- FACTORIES ---
import com.example.ositopolarapp.core.di.*

// --- VIEWMODELS ---
import com.example.ositopolarapp.features.authentication.presentation.state.MainViewModel
import com.example.ositopolarapp.features.equipment.presentation.state.EquipmentListViewModel
import com.example.ositopolarapp.features.equipment.presentation.state.EquipmentDetailViewModel
import com.example.ositopolarapp.features.analytics.presentation.state.AnalyticsViewModel

// --- PANTALLAS ---
import com.example.ositopolarapp.features.authentication.presentation.screens.LoginScreen
import com.example.ositopolarapp.features.authentication.presentation.screens.ClientLoginScreen
import com.example.ositopolarapp.features.authentication.presentation.screens.ClientRegisterScreen
import com.example.ositopolarapp.features.authentication.presentation.screens.GeneratedCredentialsScreen
import com.example.ositopolarapp.features.subscriptions.presentation.screens.PlansScreen
import com.example.ositopolarapp.features.onboarding.presentation.screens.GetStartedScreen
import com.example.ositopolarapp.features.onboarding.presentation.screens.SelectProfileScreen
import com.example.ositopolarapp.features.public.presentation.screens.OwnerDashboardScreen
import com.example.ositopolarapp.features.equipment.presentation.screens.AddEquipmentScreen
import com.example.ositopolarapp.features.equipment.presentation.screens.EquipmentDetailScreen
import com.example.ositopolarapp.features.analytics.presentation.screens.EquipmentAnalyticsScreen
import com.example.ositopolarapp.features.authentication.presentation.state.AuthState
import com.example.ositopolarapp.features.servicerequests.presentation.screens.ServiceRequestWizardScreen

// --- PANTALLAS DE PERFIL Y AJUSTES ---
import com.example.ositopolarapp.features.profile.presentation.screens.UpdateProfileScreen
import com.example.ositopolarapp.features.profile.presentation.screens.PaymentHistoryScreen
import com.example.ositopolarapp.features.profile.presentation.screens.TwoFactorSettingsScreen
import com.example.ositopolarapp.features.profile.presentation.screens.SettingsScreen

// --- PANTALLAS DE SUSCRIPCIONES (AQUÍ ESTABA EL ERROR) ---
import com.example.ositopolarapp.features.subscriptions.presentation.screens.UpgradePlansScreen

@Composable
fun AppNavigation(
    appContainer: AppContainer,
    deepLinkUri: Uri?,
    onDeepLinkProcessed: () -> Unit
) {
    val navController = rememberNavController()

    // Factories
    val mainFactory = remember { MainViewModelFactory(appContainer) }
    val authFactory = remember { AuthViewModelFactory(appContainer) }
    val equipmentFactory = remember { EquipmentViewModelFactory(appContainer) }
    val plansFactory = remember { PlansViewModelFactory(appContainer) }
    val analyticsFactory = remember { AnalyticsViewModelFactory(appContainer) }
    val serviceFactory = remember { ServiceRequestViewModelFactory(appContainer) }
    val rentalFactory = remember { RentalViewModelFactory(appContainer) }

    val mainVM = viewModel<MainViewModel>(factory = mainFactory)
    val authState by mainVM.authState.collectAsState()
    val currentUser by mainVM.currentUser.collectAsState()

    val startDestination = when (authState) {
        AuthState.Loading -> "loading_route"
        AuthState.LoggedIn -> "dashboard"
        AuthState.LoggedOut -> "get_started"
        AuthState.Loading -> TODO()
        AuthState.LoggedIn -> TODO()
        AuthState.LoggedOut -> TODO()
    }

    if (authState == AuthState.Loading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    NavHost(navController = navController, startDestination = startDestination) {

        // --- ONBOARDING ---
        composable("loading_route") { Box(Modifier.fillMaxSize()) { CircularProgressIndicator() } }
        composable("get_started") { GetStartedScreen(onNavigateToSelectProfile = { navController.navigate("select_profile") }) }
        composable("select_profile") { SelectProfileScreen(onNavigateNext = { navController.navigate("client_login") }) }

        // --- AUTH ---
        composable("client_login") {
            ClientLoginScreen(
                onLoginSuccess = { navController.navigate("dashboard") { popUpTo("get_started") { inclusive = true } } },
                onRegisterClicked = { navController.navigate("register_new/1/Client") },
                onForgotPasswordClicked = {}
            )
        }
        composable("login") {
            LoginScreen(
                viewModel = viewModel(factory = authFactory),
                onLoginSuccess = { navController.navigate("dashboard") { popUpTo("login") { inclusive = true } } },
                onGoToRegister = { navController.navigate("register_new/1/Owner") }
            )
        }

        // --- REGISTRO ---
        composable("plans") {
            PlansScreen(
                viewModel = viewModel(factory = plansFactory),
                onPlanSelected = { id, type -> navController.navigate("register_new/$id/$type") },
                onNavigateBack = { navController.popBackStack() }
            )
        }
        composable(
            route = "register_new/{planId}/{userType}",
            arguments = listOf(navArgument("planId") { type = NavType.IntType }, navArgument("userType") { type = NavType.StringType })
        ) { entry ->
            ClientRegisterScreen(
                viewModel = viewModel(factory = authFactory),
                planId = entry.arguments?.getInt("planId") ?: 1,
                userType = entry.arguments?.getString("userType") ?: "Owner",
                onSignInClicked = { navController.navigate("login") }
            )
        }
        composable(
            route = "registration_callback?session_id={sessionId}",
            deepLinks = listOf(navDeepLink { uriPattern = "ositopolar://registration?session_id={sessionId}"; action = android.content.Intent.ACTION_VIEW }),
            arguments = listOf(navArgument("sessionId") { type = NavType.StringType; nullable = true })
        ) { entry ->
            GeneratedCredentialsScreen(
                viewModel = viewModel(factory = authFactory),
                sessionId = entry.arguments?.getString("sessionId"),
                onLoginClicked = { navController.navigate("client_login") { popUpTo(0) } }
            )
        }

        // --- DASHBOARD ---
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
                    navController.navigate("get_started") { popUpTo("dashboard") { inclusive = true } }
                }
            )
        }

        // --- EQUIPOS ---
        composable("equipment/add") {
            AddEquipmentScreen(
                viewModel = viewModel(factory = equipmentFactory),
                ownerId = currentUser?.id ?: 0,
                onNavigateBack = { navController.popBackStack() }
            )
        }
        composable("equipment/detail/{equipmentId}") { entry ->
            val id = entry.arguments?.getString("equipmentId")?.toIntOrNull()
            if (id != null) {
                EquipmentDetailScreen(
                    equipmentId = id,
                    viewModel = viewModel(factory = equipmentFactory),
                    onNavigateBack = { navController.popBackStack() },
                    onNavigateToAnalytics = { navController.navigate("equipment/analytics/$id") }
                )
            }
        }
        composable("equipment/analytics/{equipmentId}") { entry ->
            val id = entry.arguments?.getString("equipmentId")?.toIntOrNull()
            if (id != null) {
                val eqVM = viewModel<EquipmentDetailViewModel>(factory = equipmentFactory)
                val anVM = viewModel<AnalyticsViewModel>(factory = analyticsFactory)
                LaunchedEffect(id) { eqVM.loadEquipment(id); anVM.loadAnalytics(id) }

                val eqState by eqVM.uiState.collectAsState()
                val anState by anVM.uiState.collectAsState()

                if (eqState.equipment != null) {
                    EquipmentAnalyticsScreen(
                        equipment = eqState.equipment!!,
                        analyticsState = anState,
                        onRefresh = {},
                        onNavigateBack = { navController.popBackStack() }
                    )
                }
            }
        }

        // --- SERVICIOS ---
        composable("service-request/create") {
            val equipmentViewModel = viewModel<EquipmentListViewModel>(factory = equipmentFactory)
            val eqState by equipmentViewModel.uiState.collectAsState()
            LaunchedEffect(Unit) { if (eqState.equipmentList.isEmpty()) equipmentViewModel.loadEquipments() }

            ServiceRequestWizardScreen(
                viewModel = viewModel(factory = serviceFactory),
                equipmentList = eqState.equipmentList,
                userId = currentUser?.id ?: 0,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        // --- PERFIL ---
        composable("profile/edit/{profileId}") {
            UpdateProfileScreen(
                profileApiService = appContainer.profileApiService,
                profileId = it.arguments?.getString("profileId")?.toInt() ?: 0,
                onNavigateBack = { navController.popBackStack() }
            )
        }
        composable("profile/payment-history") { PaymentHistoryScreen(userType = "Owner", onNavigateBack = { navController.popBackStack() }) }
        composable("profile/2fa-settings") { TwoFactorSettingsScreen(is2FAEnabled = false, onEnable2FA = {}, onDisable2FA = {}, onNavigateBack = { navController.popBackStack() }) }
        composable("profile/settings") { SettingsScreen(preferencesManager = appContainer.preferencesManager, onNavigateBack = { navController.popBackStack() }) }

        // --- AQUÍ ESTABA EL ERROR DE UPGRADE PLAN ---
        composable("profile/upgrade-plan") {
            UpgradePlansScreen(
                viewModel = viewModel(factory = plansFactory),
                currentPlanId = 1,
                userType = "Owner",
                onUpgradeConfirmed = {},
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}