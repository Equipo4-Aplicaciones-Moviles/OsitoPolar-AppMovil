package com.example.ositopolarapp.features.equipment.presentation.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ositopolarapp.features.equipment.presentation.state.EquipmentDetailViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EquipmentDetailScreen(
    equipmentId: Int,
    viewModel: EquipmentDetailViewModel,
    onNavigateBack: () -> Unit,
    onNavigateToAnalytics: (Int) -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(equipmentId) {
        viewModel.loadEquipment(equipmentId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalle del Equipo") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding).fillMaxSize()) {
            if (uiState.isLoading) {
                CircularProgressIndicator()
            } else {
                uiState.equipment?.let { eq ->
                    Column(
                        modifier = Modifier
                            .padding(16.dp)
                            .verticalScroll(rememberScrollState())
                    ) {
                        Text(eq.name, style = MaterialTheme.typography.headlineMedium)
                        Text("${eq.brand} ${eq.model}", style = MaterialTheme.typography.titleMedium)
                        Divider(modifier = Modifier.padding(vertical = 16.dp))

                        Text("Estado: ${eq.status}")
                        Text("Temperatura Actual: ${eq.temperature}°C")
                        Text("Consumo: ${eq.energyConsumption} kWh")

                        Spacer(modifier = Modifier.height(16.dp))
                        Text("Ubicación", style = MaterialTheme.typography.titleMedium)
                        Text("Sede: ${eq.location}")
                        Text("Dirección: ${eq.address}")

                        Spacer(modifier = Modifier.height(24.dp))

                        Button(
                            onClick = { onNavigateToAnalytics(eq.id) },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Ver Analíticas")
                        }
                    }
                }
            }
        }
    }
}