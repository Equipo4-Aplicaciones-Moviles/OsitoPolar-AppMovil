package com.example.ositopolarapp.features.public.presentation.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.ositopolarapp.core.di.AppContainer
import com.example.ositopolarapp.core.di.EquipmentViewModelFactory
import com.example.ositopolarapp.core.di.RentalViewModelFactory
import com.example.ositopolarapp.core.di.ServiceRequestViewModelFactory
// Imports de tus pantallas
import com.example.ositopolarapp.features.equipment.presentation.screens.EquipmentListScreen
import com.example.ositopolarapp.features.profile.presentation.screens.ProfileScreen
import com.example.ositopolarapp.features.notifications.presentation.screens.NotificationsScreen // Si existe, si no, comenta esto
import com.example.ositopolarapp.features.rentals.presentation.screens.RentalCatalogScreen
import com.example.ositopolarapp.features.servicerequests.presentation.screens.ServiceRequestListScreen

@Composable
fun OwnerDashboardScreen(
    appContainer: AppContainer,
    navController: NavHostController,
    username: String,
    userType: String,
    profileId: Int,
    requires2FA: Boolean,
    onLogout: () -> Unit
) {
    var selectedTab by remember { mutableStateOf(DashboardTab.MY_MACHINES) }

    // Factories
    val equipmentFactory = remember { EquipmentViewModelFactory(appContainer) }
    val rentalFactory = remember { RentalViewModelFactory(appContainer) }
    val serviceFactory = remember { ServiceRequestViewModelFactory(appContainer) }

    Scaffold(
        bottomBar = {
            NavigationBar {
                DashboardTab.entries.forEach { tab ->
                    NavigationBarItem(
                        icon = { Icon(tab.icon, contentDescription = tab.label) },
                        label = { Text(tab.label) },
                        selected = selectedTab == tab,
                        onClick = { selectedTab = tab }
                    )
                }
            }
        }
    ) { paddingValues ->
        when (selectedTab) {
            DashboardTab.MY_MACHINES -> {
                EquipmentListScreen(
                    viewModel = viewModel(factory = equipmentFactory),
                    onNavigateToDetail = { id -> navController.navigate("equipment/detail/$id") },
                    onNavigateToAdd = { navController.navigate("equipment/add") },
                    modifier = Modifier.padding(paddingValues)
                )
            }
            DashboardTab.RENTALS -> {
                RentalCatalogScreen(
                    viewModel = viewModel(factory = rentalFactory),
                    onRentEquipment = { eq -> navController.navigate("rental/checkout/${eq.id}") },
                    modifier = Modifier.padding(paddingValues)
                )
            }
            DashboardTab.SERVICE_REQUESTS -> {
                ServiceRequestListScreen(
                    viewModel = viewModel(factory = serviceFactory),
                    onNavigateToCreate = { navController.navigate("service-request/create") },
                    modifier = Modifier.padding(paddingValues)
                )
            }
            DashboardTab.PROFILE -> {
                // Aquí usamos el ProfileScreen que arreglamos antes
                ProfileScreen(
                    username = username,
                    userType = userType,
                    planName = "Plan Actual",
                    onLogout = onLogout,
                    onNavigateToEditProfile = { navController.navigate("profile/edit/$profileId") },
                    onNavigateToPaymentHistory = { navController.navigate("profile/payment-history") },
                    onNavigateTo2FASettings = { navController.navigate("profile/2fa-settings") },
                    onNavigateToSettings = { navController.navigate("profile/settings") },
                    onNavigateToUpgradePlan = { navController.navigate("profile/upgrade-plan") },
                    modifier = Modifier.padding(paddingValues)
                )
            }
            DashboardTab.NOTIFICATIONS -> {
                // Si no tienes NotificationsScreen, pon un Text("Notificaciones")
                Text("Notificaciones", modifier = Modifier.padding(paddingValues))
            }
        }
    }
}

enum class DashboardTab(val label: String, val icon: ImageVector) {
    MY_MACHINES("Equipos", Icons.Default.Home),
    RENTALS("Rentas", Icons.Default.ShoppingCart),
    SERVICE_REQUESTS("Servicios", Icons.Default.Build),
    PROFILE("Perfil", Icons.Default.Person),
    NOTIFICATIONS("Alertas", Icons.Default.Notifications)
}