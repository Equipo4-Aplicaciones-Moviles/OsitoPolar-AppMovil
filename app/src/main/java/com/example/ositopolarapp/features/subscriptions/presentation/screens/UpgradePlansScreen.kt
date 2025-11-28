package com.example.ositopolarapp.features.subscriptions.presentation.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.ositopolarapp.features.subscriptions.presentation.state.PlansViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpgradePlansScreen(
    viewModel: PlansViewModel,
    currentPlanId: Int,
    userType: String,
    onUpgradeConfirmed: () -> Unit,
    onNavigateBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mejorar Plan") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "Actualizar Plan ($userType)",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text("Funcionalidad próximamente")

                Spacer(modifier = Modifier.height(24.dp))

                Button(onClick = onNavigateBack) {
                    Text("Volver")
                }
            }
        }
    }
}