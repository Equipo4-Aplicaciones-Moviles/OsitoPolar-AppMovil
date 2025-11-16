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
import com.example.ositopolarapp.features.authentication.presentation.screens.LoginScreen
import com.example.ositopolarapp.features.authentication.presentation.screens.RegistrationScreen
import com.example.ositopolarapp.features.authentication.presentation.state.*
import com.example.ositopolarapp.features.equipment.presentation.screens.EquipmentDetailScreen
import com.example.ositopolarapp.features.equipment.presentation.screens.EquipmentListScreen
import com.example.ositopolarapp.features.subscriptions.presentation.screens.PlansScreen

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

    val startDestination = when (authState) {
        AuthState.Loading -> ""
        AuthState.LoggedIn -> "dashboard"
        AuthState.LoggedOut -> "welcome"
    }

    // Handle deep link navigation
    LaunchedEffect(deepLinkUri) {
        if (deepLinkUri != null && deepLinkUri.path == "/success") {
            android.util.Log.d("AppNavigation", "Deep link detected, navigating to registration completion")
            // Clear the back stack and navigate to registration-complete
            navController.navigate("registration-complete") {
                popUpTo("welcome") { inclusive = false }
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
                        popUpTo("welcome") { inclusive = false }
                    }
                }
            )
        }

        // Route to handle deep link from Stripe payment success
        composable("registration-complete") {
            val registrationViewModel = viewModel<RegistrationViewModel>(factory = regFactory)
            val uiState by registrationViewModel.uiState.collectAsState()
            val context = androidx.compose.ui.platform.LocalContext.current

            // Track if we've already processed this deep link
            var hasProcessedDeepLink by remember { mutableStateOf(false) }

            LaunchedEffect(deepLinkUri) {
                if (deepLinkUri != null && deepLinkUri.path == "/success" && !hasProcessedDeepLink) {
                    hasProcessedDeepLink = true
                    val sessionId = deepLinkUri.getQueryParameter("session_id")
                    android.util.Log.d("AppNavigation", "Processing registration with session_id: $sessionId")
                    android.util.Log.d("AppNavigation", "Form data present in ViewModel: ${uiState.formData != null}")

                    if (sessionId != null) {
                        registrationViewModel.completeRegistration(sessionId)
                        // Clear the deep link after processing
                        onDeepLinkProcessed()
                    } else {
                        android.util.Log.e("AppNavigation", "session_id is null!")
                        android.widget.Toast.makeText(context, "Error: No se encontró el ID de sesión", android.widget.Toast.LENGTH_LONG).show()
                        onDeepLinkProcessed()
                        // Navigate back to plans screen on error
                        navController.navigate("plans") {
                            popUpTo("welcome") { inclusive = false }
                        }
                    }
                }
            }

            LaunchedEffect(uiState.registrationComplete) {
                if (uiState.registrationComplete && uiState.generatedUsername != null) {
                    android.util.Log.i("AppNavigation", "Registration complete")

                    if (uiState.generatedPassword != null) {
                        // Normal flow: show credentials screen
                        android.util.Log.i("AppNavigation", "Navigating to credentials screen")
                        navController.navigate("generated-credentials/${uiState.generatedUsername}/${uiState.generatedPassword}") {
                            popUpTo("welcome") { inclusive = false }
                        }
                    } else {
                        // Backend didn't return password - show error and go to login
                        android.util.Log.e("AppNavigation", "Backend returned null password!")
                        android.widget.Toast.makeText(
                            context,
                            "Registro exitoso! Usuario: ${uiState.generatedUsername}\nPor favor contacta al administrador para obtener tu contraseña.",
                            android.widget.Toast.LENGTH_LONG
                        ).show()

                        // Navigate to login after 2 seconds
                        kotlinx.coroutines.delay(2000)
                        navController.navigate("login") {
                            popUpTo("welcome") { inclusive = false }
                        }
                    }
                }
            }

            LaunchedEffect(uiState.error) {
                uiState.error?.let { error ->
                    android.util.Log.e("AppNavigation", "Registration error: $error")
                    android.widget.Toast.makeText(context, "Error al completar registro: $error", android.widget.Toast.LENGTH_LONG).show()
                }
            }

            // Show loading screen while processing
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                androidx.compose.foundation.layout.Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(16.dp),
                    modifier = Modifier.padding(24.dp)
                ) {
                    if (uiState.error != null) {
                        // Error State
                        Icon(
                            imageVector = Icons.Filled.Warning,
                            contentDescription = "Error",
                            tint = androidx.compose.material3.MaterialTheme.colorScheme.error,
                            modifier = Modifier.size(64.dp)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            "Error al completar registro",
                            style = androidx.compose.material3.MaterialTheme.typography.headlineSmall,
                            color = androidx.compose.material3.MaterialTheme.colorScheme.error
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            uiState.error ?: "Error desconocido",
                            style = androidx.compose.material3.MaterialTheme.typography.bodyMedium,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(24.dp))
                        androidx.compose.foundation.layout.Row(
                            horizontalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(8.dp)
                        ) {
                            Button(
                                onClick = {
                                    // Retry the registration
                                    deepLinkUri?.getQueryParameter("session_id")?.let { sessionId ->
                                        hasProcessedDeepLink = false
                                        registrationViewModel.completeRegistration(sessionId)
                                    }
                                }
                            ) {
                                Text("Reintentar")
                            }
                            OutlinedButton(
                                onClick = {
                                    navController.navigate("plans") {
                                        popUpTo("welcome") { inclusive = false }
                                    }
                                }
                            ) {
                                Text("Volver a Planes")
                            }
                        }
                    } else if (uiState.isLoading) {
                        // Loading State
                        CircularProgressIndicator()
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            "Completando registro...",
                            style = androidx.compose.material3.MaterialTheme.typography.headlineSmall
                        )
                        Text(
                            "Por favor espera mientras procesamos tu pago",
                            style = androidx.compose.material3.MaterialTheme.typography.bodyMedium,
                            color = androidx.compose.material3.MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        LinearProgressIndicator(
                            modifier = Modifier.fillMaxWidth(0.7f)
                        )
                    } else {
                        // Initial State
                        CircularProgressIndicator()
                        Text("Iniciando...")
                    }
                }
            }
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

                // Analytics ViewModel
                val analyticsViewModel = viewModel<com.example.ositopolarapp.features.analytics.presentation.state.AnalyticsViewModel>(
                    factory = analyticsFactory
                )

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
                        onRefresh = {
                            analyticsViewModel.refresh(equipmentId)
                            analyticsViewModel.loadAdvancedAnalytics(equipmentId)
                        },
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
            val coroutineScope = rememberCoroutineScope()

            com.example.ositopolarapp.features.profile.presentation.screens.TwoFactorSettingsScreen(
                is2FAEnabled = currentUser?.requires2FA ?: false,
                onEnable2FA = {
                    coroutineScope.launch {
                        currentUser?.username?.let { username ->
                            mainVM.enable2FA(username)
                        }
                    }
                },
                onDisable2FA = {
                    coroutineScope.launch {
                        currentUser?.username?.let { username ->
                            mainVM.disable2FA(username)
                        }
                    }
                },
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

        // Upgrade Plan
        composable("profile/upgrade-plan") {
            val context = androidx.compose.ui.platform.LocalContext.current

            com.example.ositopolarapp.features.subscriptions.presentation.screens.UpgradePlansScreen(
                viewModel = viewModel(factory = plansFactory),
                currentPlanId = currentUser?.planId,
                userType = currentUser?.userType ?: "Owner",
                onUpgradeConfirmed = { newPlanId ->
                    // TODO: Implement upgrade flow with Stripe payment
                    android.widget.Toast.makeText(
                        context,
                        "Upgrade to plan $newPlanId - Stripe integration pending",
                        android.widget.Toast.LENGTH_LONG
                    ).show()
                    navController.popBackStack()
                },
                onNavigateBack = { navController.popBackStack() }
            )
        }

        // Edit Profile
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

        // Rental Checkout
        composable("rental/checkout/{equipmentId}") { entry ->
            val equipmentId = entry.arguments?.getString("equipmentId")?.toIntOrNull()
            val context = androidx.compose.ui.platform.LocalContext.current

            if (equipmentId != null) {
                // Fetch equipment from API
                var equipment by remember { mutableStateOf<com.example.ositopolarapp.features.rentals.domain.model.RentalEquipment?>(null) }
                var isLoading by remember { mutableStateOf(true) }
                var error by remember { mutableStateOf<String?>(null) }

                // Get the ViewModel for rental operations
                val rentalViewModel = viewModel<com.example.ositopolarapp.features.rentals.presentation.viewmodel.RentalCatalogViewModel>(
                    factory = rentalFactory
                )
                val rentalUiState by rentalViewModel.uiState.collectAsState()

                LaunchedEffect(equipmentId) {
                    appContainer.getRentalEquipmentByIdUseCase(equipmentId)
                        .onSuccess { fetchedEquipment ->
                            equipment = fetchedEquipment
                            isLoading = false
                        }
                        .onFailure { exception ->
                            error = exception.message ?: "Error al cargar equipo"
                            isLoading = false
                        }
                }

                // Handle Stripe checkout URL
                LaunchedEffect(rentalUiState.checkoutUrl) {
                    rentalUiState.checkoutUrl?.let { url ->
                        try {
                            val intent = android.content.Intent(android.content.Intent.ACTION_VIEW, android.net.Uri.parse(url))
                            context.startActivity(intent)
                            rentalViewModel.clearCheckoutUrl()
                        } catch (e: Exception) {
                            android.util.Log.e("RentalCheckout", "Error opening Stripe checkout", e)
                            android.widget.Toast.makeText(context, "Error al abrir checkout de pago", android.widget.Toast.LENGTH_LONG).show()
                        }
                    }
                }

                when {
                    isLoading -> {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            CircularProgressIndicator()
                        }
                    }
                    error != null -> {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            androidx.compose.foundation.layout.Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(16.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.Warning,
                                    contentDescription = "Error",
                                    tint = androidx.compose.material3.MaterialTheme.colorScheme.error,
                                    modifier = Modifier.size(48.dp)
                                )
                                Text(
                                    text = error ?: "Error desconocido",
                                    color = androidx.compose.material3.MaterialTheme.colorScheme.error,
                                    textAlign = TextAlign.Center
                                )
                                Button(onClick = { navController.popBackStack() }) {
                                    Text("Volver")
                                }
                            }
                        }
                    }
                    equipment != null -> {
                        com.example.ositopolarapp.features.rentals.presentation.screens.RentalCheckoutScreen(
                            equipment = equipment!!,
                            onNavigateBack = { navController.popBackStack() },
                            onCheckoutSuccess = {
                                // The Stripe checkout URL will be opened automatically via LaunchedEffect above
                            },
                            onCreateRentalRequest = { equipmentId, months ->
                                // Call ViewModel to create rental request and get Stripe checkout URL
                                rentalViewModel.createRentalRequest(equipmentId, months)
                            }
                        )
                    }
                }
            } else {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Error: ID de equipo inválido")
                }
            }
        }

        // Service Request Wizard
        composable("service-request/create") {
            // Load equipment list for service request wizard
            val equipmentListVM = viewModel<com.example.ositopolarapp.features.equipment.presentation.state.EquipmentListViewModel>(
                factory = equipmentFactory
            )
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
