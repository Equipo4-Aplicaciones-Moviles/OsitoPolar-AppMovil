package com.example.ositopolarapp.navigation

import android.net.Uri
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.ositopolarapp.core.di.*
import com.example.ositopolarapp.features.authentication.presentation.screens.LoginScreen
import com.example.ositopolarapp.features.authentication.presentation.screens.RegistrationScreen
import com.example.ositopolarapp.features.authentication.presentation.state.*
import com.example.ositopolarapp.features.equipment.presentation.screens.EquipmentDetailScreen
import com.example.ositopolarapp.features.equipment.presentation.screens.EquipmentListScreen
import com.example.ositopolarapp.features.subscriptions.presentation.screens.PlansScreen

import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Text
import androidx.compose.foundation.layout.fillMaxSize

@Composable
fun AppNavigation(
    appContainer: AppContainer,
    deepLinkUri: Uri?,
    onDeepLinkProcessed: () -> Unit
) {

    val navController = rememberNavController()

    val mainFactory = remember { MainViewModelFactory(appContainer) }
    val regFactory = remember { RegistrationViewModelFactory(appContainer) }
    val authFactory = remember { AuthViewModelFactory(appContainer) }
    val equipmentFactory = remember { EquipmentViewModelFactory(appContainer) }
    val plansFactory = remember { PlansViewModelFactory(appContainer) }

    val mainVM = viewModel<MainViewModel>(factory = mainFactory)
    val authState by mainVM.authState.collectAsState()
    val currentUser by mainVM.currentUser.collectAsState()

    val startDestination = when (authState) {
        AuthState.Loading -> ""
        AuthState.LoggedIn -> "dashboard"
        AuthState.LoggedOut -> "welcome"
    }

    if (authState == AuthState.Loading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    NavHost(navController = navController, startDestination = startDestination) {

        composable("welcome") {
            com.example.ositopolarapp.features.public.presentation.screens.WelcomeScreen(
                onNavigateToLogin = {
                    navController.navigate("login")
                },
                onNavigateToExplore = {
                    navController.navigate("explore")
                }
            )
        }

        composable("explore") {
            com.example.ositopolarapp.features.public.presentation.screens.ExploreHomeScreen(
                onNavigateToLogin = {
                    navController.navigate("login")
                },
                onNavigateToPlans = {
                    navController.navigate("plans")
                }
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
                deepLinkUri = deepLinkUri,
                onRegistrationSuccess = {
                    navController.navigate("login") {
                        popUpTo("login") { inclusive = true }
                    }
                },
                onDeepLinkProcessed = onDeepLinkProcessed
            )
        }

        composable("dashboard") {
            com.example.ositopolarapp.features.public.presentation.screens.OwnerDashboardScreen(
                appContainer = appContainer,
                navController = navController,
                username = currentUser?.username ?: "Usuario",
                userType = currentUser?.userType ?: "Owner",
                profileId = currentUser?.profileId ?: 0,
                requires2FA = currentUser?.requires2FA ?: false,
                onLogout = {
                    mainVM.logout()
                    // Navigation will be handled automatically by AuthState change
                    // But we can also manually navigate to ensure immediate UI update
                    navController.navigate("welcome") {
                        popUpTo("dashboard") { inclusive = true }
                    }
                }
            )
        }

        composable("equipment/detail/{equipmentId}") { entry ->
            val equipmentId = entry.arguments?.getString("equipmentId")?.toIntOrNull()

            if (equipmentId != null) {
                EquipmentDetailScreen(
                    equipmentId = equipmentId,
                    viewModel = viewModel(factory = equipmentFactory),
                    onNavigateBack = { navController.popBackStack() },
                    onNavigateToAnalytics = { id ->
                        navController.navigate("equipment/analytics/$id")
                    }
                )
            } else {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Error: ID de equipo inválido")
                }
            }
        }

        // Equipment Analytics
        composable("equipment/analytics/{equipmentId}") { entry ->
            val equipmentId = entry.arguments?.getString("equipmentId")?.toIntOrNull()

            if (equipmentId != null) {
                // Load equipment first
                val equipmentViewModel = viewModel<com.example.ositopolarapp.features.equipment.presentation.state.EquipmentDetailViewModel>(
                    factory = equipmentFactory
                )

                LaunchedEffect(equipmentId) {
                    equipmentViewModel.loadEquipment(equipmentId)
                }

                val uiState by equipmentViewModel.uiState.collectAsState()

                if (uiState.equipment != null) {
                    com.example.ositopolarapp.features.analytics.presentation.screens.EquipmentAnalyticsScreen(
                        equipment = uiState.equipment!!,
                        onNavigateBack = { navController.popBackStack() }
                    )
                } else {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }
            }
        }

        // Add Equipment
        composable("equipment/add") {
            com.example.ositopolarapp.features.equipment.presentation.screens.AddEquipmentScreen(
                viewModel = viewModel(factory = equipmentFactory),
                ownerId = currentUser?.id ?: 0,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        // Payment History
        composable("profile/payment-history") {
            com.example.ositopolarapp.features.profile.presentation.screens.PaymentHistoryScreen(
                userType = currentUser?.userType ?: "Owner",
                onNavigateBack = { navController.popBackStack() }
            )
        }

        // 2FA Settings
        composable("profile/2fa-settings") {
            com.example.ositopolarapp.features.profile.presentation.screens.TwoFactorSettingsScreen(
                is2FAEnabled = currentUser?.requires2FA ?: false,
                onEnable2FA = { /* TODO: Call backend */ },
                onDisable2FA = { /* TODO: Call backend */ },
                onNavigateBack = { navController.popBackStack() }
            )
        }

        // App Settings
        composable("profile/settings") {
            com.example.ositopolarapp.features.profile.presentation.screens.SettingsScreen(
                preferencesManager = appContainer.preferencesManager,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        // Rental Checkout
        composable("rental/checkout/{equipmentId}") { /* TODO: Implement with mock rental equipment */ }

        // Service Request Wizard
        composable("service-request/create") {
            // TODO: Pass equipment list and user ID from auth state
            com.example.ositopolarapp.features.servicerequests.presentation.screens.ServiceRequestWizardScreen(
                viewModel = viewModel(factory = com.example.ositopolarapp.core.di.ServiceRequestViewModelFactory(appContainer)),
                equipmentList = emptyList(), // TODO: Load from equipment list
                userId = 1, // TODO: Get from auth state
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}
