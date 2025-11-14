package com.example.ositopolarapp.features.subscriptions.presentation.screens

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.ositopolarapp.features.subscriptions.presentation.components.PlanCard
import com.example.ositopolarapp.features.subscriptions.presentation.state.PlansViewModel

/**
 * Plans/Subscriptions selection screen.
 * Displays plans for Owners and Providers with tabs.
 * User selects a plan to proceed with registration.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlansScreen(
    viewModel: PlansViewModel,
    onPlanSelected: (planId: Int, userType: String) -> Unit,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    // Show error toast
    LaunchedEffect(uiState.error) {
        uiState.error?.let {
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
            viewModel.clearError()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Selecciona tu Plan",
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Volver"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Tab Row
            TabRow(
                selectedTabIndex = if (uiState.selectedUserType == "Owner") 0 else 1
            ) {
                Tab(
                    selected = uiState.selectedUserType == "Owner",
                    onClick = { viewModel.switchUserType("Owner") },
                    text = { Text("Propietarios") }
                )
                Tab(
                    selected = uiState.selectedUserType == "Provider",
                    onClick = { viewModel.switchUserType("Provider") },
                    text = { Text("Proveedores") }
                )
            }

            // Content
            when {
                uiState.isLoading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

                else -> {
                    val plans = if (uiState.selectedUserType == "Owner") {
                        uiState.ownerPlans
                    } else {
                        uiState.providerPlans
                    }

                    if (plans.isEmpty()) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = "No hay planes disponibles",
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                                Button(onClick = { viewModel.loadPlans() }) {
                                    Text("Reintentar")
                                }
                            }
                        }
                    } else {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(vertical = 16.dp)
                        ) {
                            item {
                                // Header text
                                Text(
                                    text = if (uiState.selectedUserType == "Owner") {
                                        "Planes para Propietarios de Equipos"
                                    } else {
                                        "Planes para Proveedores de Servicios"
                                    },
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                                )

                                Text(
                                    text = if (uiState.selectedUserType == "Owner") {
                                        "Gestiona tus equipos de refrigeración"
                                    } else {
                                        "Ofrece servicios de mantenimiento y reparación"
                                    },
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
                                )

                                Spacer(modifier = Modifier.height(8.dp))
                            }

                            items(
                                items = plans,
                                key = { it.id }
                            ) { plan ->
                                PlanCard(
                                    plan = plan,
                                    isSelected = uiState.selectedPlan?.id == plan.id,
                                    onSelect = {
                                        viewModel.selectPlan(plan)
                                        // Immediately navigate to registration
                                        onPlanSelected(plan.id, uiState.selectedUserType)
                                    }
                                )
                            }

                            item {
                                Spacer(modifier = Modifier.height(80.dp))
                            }
                        }
                    }
                }
            }
        }
    }
}
