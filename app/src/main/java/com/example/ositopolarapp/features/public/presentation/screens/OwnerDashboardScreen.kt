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
import com.example.ositopolarapp.features.equipment.presentation.screens.EquipmentListScreen
import com.example.ositopolarapp.core.di.AppContainer
import com.example.ositopolarapp.core.di.EquipmentViewModelFactory

/**
 * Owner Dashboard - Main screen with Bottom Navigation
 *
 * Bottom Navigation Tabs:
 * 1. My Machines - Equipment list
 * 2. Rentals - Browse and manage rentals
 * 3. Service Requests - Create and view service requests
 * 4. Profile - User profile and settings
 * 5. Notifications - Notification center (with badge)
 */
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
    var notificationCount by remember { mutableStateOf(3) } // TODO: Replace with real count

    val equipmentFactory = remember { EquipmentViewModelFactory(appContainer) }

    Scaffold(
        bottomBar = {
            NavigationBar {
                DashboardTab.entries.forEach { tab ->
                    NavigationBarItem(
                        icon = {
                            if (tab == DashboardTab.NOTIFICATIONS && notificationCount > 0) {
                                BadgedBox(
                                    badge = {
                                        Badge {
                                            Text(
                                                text = if (notificationCount > 9) "9+" else notificationCount.toString()
                                            )
                                        }
                                    }
                                ) {
                                    Icon(tab.icon, contentDescription = tab.label)
                                }
                            } else {
                                Icon(tab.icon, contentDescription = tab.label)
                            }
                        },
                        label = { Text(tab.label) },
                        selected = selectedTab == tab,
                        onClick = { selectedTab = tab }
                    )
                }
            }
        }
    ) { paddingValues ->
        // Content based on selected tab
        when (selectedTab) {
            DashboardTab.MY_MACHINES -> {
                EquipmentListScreen(
                    viewModel = viewModel(factory = equipmentFactory),
                    onNavigateToDetail = { equipmentId ->
                        navController.navigate("equipment/detail/$equipmentId")
                    },
                    onNavigateToAdd = {
                        navController.navigate("equipment/add")
                    },
                    modifier = Modifier.padding(paddingValues)
                )
            }
            DashboardTab.RENTALS -> {
                val rentalViewModel = viewModel<com.example.ositopolarapp.features.rentals.presentation.viewmodel.RentalCatalogViewModel>(
                    factory = com.example.ositopolarapp.core.di.RentalViewModelFactory(appContainer)
                )

                com.example.ositopolarapp.features.rentals.presentation.screens.RentalCatalogScreen(
                    onRentEquipment = { equipment ->
                        navController.navigate("rental/checkout/${equipment.id}")
                    },
                    viewModel = rentalViewModel,
                    modifier = Modifier.padding(paddingValues)
                )
            }
            DashboardTab.SERVICE_REQUESTS -> {
                com.example.ositopolarapp.features.servicerequests.presentation.screens.ServiceRequestListScreen(
                    viewModel = viewModel(factory = com.example.ositopolarapp.core.di.ServiceRequestViewModelFactory(appContainer)),
                    onNavigateToCreate = {
                        navController.navigate("service-request/create")
                    },
                    modifier = Modifier.padding(paddingValues)
                )
            }
            DashboardTab.PROFILE -> {
                com.example.ositopolarapp.features.profile.presentation.screens.ProfileScreen(
                    username = username,
                    userType = userType,
                    planName = "Plan Básico", // TODO: Get from user profile API
                    onLogout = onLogout,
                    onNavigateToEditProfile = {
                        navController.navigate("profile/edit/$profileId")
                    },
                    onNavigateToPaymentHistory = {
                        navController.navigate("profile/payment-history")
                    },
                    onNavigateTo2FASettings = {
                        navController.navigate("profile/2fa-settings")
                    },
                    onNavigateToSettings = {
                        navController.navigate("profile/settings")
                    },
                    onNavigateToUpgradePlan = {
                        navController.navigate("profile/upgrade-plan")
                    },
                    modifier = Modifier.padding(paddingValues)
                )
            }
            DashboardTab.NOTIFICATIONS -> {
                com.example.ositopolarapp.features.notifications.presentation.screens.NotificationsScreen(
                    onMarkAllAsRead = { notificationCount = 0 },
                    modifier = Modifier.padding(paddingValues)
                )
            }
        }
    }
}

/**
 * Dashboard Tab enum
 */
enum class DashboardTab(val label: String, val icon: ImageVector) {
    MY_MACHINES("Equipos", Icons.Default.Home),
    RENTALS("Rentas", Icons.Default.ShoppingCart),
    SERVICE_REQUESTS("Servicios", Icons.Default.Build),
    PROFILE("Perfil", Icons.Default.Person),
    NOTIFICATIONS("Notif.", Icons.Default.Notifications)
}
