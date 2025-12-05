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
 * Upgrade Plans Screen
 * Shows all available plans with current plan highlighted
 * Allows upgrading to different plans
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpgradePlansScreen(
    viewModel: PlansViewModel,
    currentPlanId: Int?,
    userType: String,
    onUpgradeConfirmed: (planId: Int) -> Unit,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    // Set user type on init
    LaunchedEffect(Unit) {
        viewModel.switchUserType(userType)
    }

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
                        text = "Mejorar Plan",
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
                    val plans = if (userType == "Owner") {
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
                                // Header
                                Column(
                                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                                ) {
                                    Text(
                                        text = "Selecciona tu nuevo plan",
                                        style = MaterialTheme.typography.titleLarge,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        text = "Mejora tu plan para acceder a más funcionalidades",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }

                                Spacer(modifier = Modifier.height(16.dp))
                            }

                            items(
                                items = plans,
                                key = { it.id }
                            ) { plan ->
                                val isCurrentPlan = currentPlanId == plan.id

                                Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
                                    // Current plan badge
                                    if (isCurrentPlan) {
                                        Surface(
                                            color = MaterialTheme.colorScheme.primary,
                                            shape = MaterialTheme.shapes.small,
                                            modifier = Modifier.padding(bottom = 8.dp)
                                        ) {
                                            Text(
                                                text = "Plan Actual",
                                                style = MaterialTheme.typography.labelSmall,
                                                fontWeight = FontWeight.Bold,
                                                color = MaterialTheme.colorScheme.onPrimary,
                                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                                            )
                                        }
                                    }

                                    PlanCard(
                                        plan = plan,
                                        isSelected = uiState.selectedPlan?.id == plan.id,
                                        onSelect = {
                                            if (!isCurrentPlan) {
                                                viewModel.selectPlan(plan)
                                                onUpgradeConfirmed(plan.id)
                                            }
                                        },
                                        enabled = !isCurrentPlan
                                    )
                                }
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
