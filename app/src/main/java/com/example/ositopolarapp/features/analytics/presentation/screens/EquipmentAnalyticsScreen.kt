package com.example.ositopolarapp.features.analytics.presentation.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.ositopolarapp.features.analytics.presentation.state.AnalyticsUiState
import com.example.ositopolarapp.features.equipment.domain.model.Equipment

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EquipmentAnalyticsScreen(
    equipment: Equipment,
    analyticsState: AnalyticsUiState,
    onRefresh: () -> Unit,
    onNavigateBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Analítica: ${equipment.name}") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            // Tarjeta de Resumen Actual
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Estado Actual", style = MaterialTheme.typography.titleMedium)
                    Spacer(modifier = Modifier.height(8.dp))

                    // CORRECCIÓN: Usamos los nombres nuevos del modelo
                    Text("Temperatura: ${equipment.temperature}°C")
                    Text("Consumo: ${equipment.energyConsumption} kWh")
                    Text("Estado: ${equipment.status}")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Gráfica Simulada (Placeholder porque quitamos lógica compleja)
            Card(modifier = Modifier.fillMaxWidth().height(200.dp)) {
                Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                    Text("Gráfico de rendimiento (Simulado)")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Estadísticas (Usamos valores calculados o del state)
            Text("Estadísticas", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))

            ListItem(
                headlineContent = { Text("Rango Óptimo") },
                trailingContent = { Text("-5°C a 5°C") } // Valor fijo visual
            )
            Divider()
            ListItem(
                headlineContent = { Text("Alertas este mes") },
                trailingContent = { Text("0") }
            )
        }
    }
}