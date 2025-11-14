package com.example.ositopolarapp.features.equipment.presentation.screens

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.ositopolarapp.features.equipment.presentation.components.PowerToggle
import com.example.ositopolarapp.features.equipment.presentation.components.TemperatureControl
import com.example.ositopolarapp.features.equipment.presentation.components.TemperatureStatusIndicator
import com.example.ositopolarapp.features.equipment.presentation.state.EquipmentDetailViewModel

/**
 * Equipment Detail Screen - Displays detailed equipment information and control panel.
 * Allows user to monitor and control equipment temperature and power state.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EquipmentDetailScreen(
    equipmentId: Int,
    viewModel: EquipmentDetailViewModel,
    onNavigateBack: () -> Unit,
    onNavigateToAnalytics: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    // Load equipment on first composition
    LaunchedEffect(equipmentId) {
        viewModel.loadEquipment(equipmentId)
    }

    // Show error toast
    LaunchedEffect(uiState.error) {
        uiState.error?.let {
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
            viewModel.clearError()
        }
    }

    // Show operations update error toast
    LaunchedEffect(uiState.operationsUpdateError) {
        uiState.operationsUpdateError?.let {
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
            viewModel.clearOperationsUpdateError()
        }
    }

    // Show success toast
    LaunchedEffect(uiState.operationsUpdateSuccess) {
        if (uiState.operationsUpdateSuccess) {
            Toast.makeText(context, "Actualizado correctamente", Toast.LENGTH_SHORT).show()
            viewModel.clearOperationsUpdateSuccess()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = uiState.equipment?.name ?: "Detalles del Equipo",
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
                },
                actions = {
                    // Analytics button
                    IconButton(
                        onClick = { onNavigateToAnalytics(equipmentId) },
                        enabled = uiState.equipment != null
                    ) {
                        Icon(
                            imageVector = Icons.Default.BarChart,
                            contentDescription = "Ver analíticas"
                        )
                    }

                    // Refresh button
                    IconButton(onClick = { viewModel.reload() }) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "Recargar"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        when {
            uiState.isLoading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            uiState.equipment == null -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No se pudo cargar el equipo",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }

            else -> {
                val equipment = uiState.equipment!!

                Column(
                    modifier = modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .verticalScroll(rememberScrollState())
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Equipment Info Card
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surface
                        )
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Información del Equipo",
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.Bold
                                )
                                TemperatureStatusIndicator(
                                    status = equipment.getTemperatureStatus(),
                                    modifier = Modifier.size(16.dp)
                                )
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            InfoRow(label = "Tipo", value = equipment.getTypeDisplay())
                            InfoRow(label = "Marca", value = equipment.manufacturer)
                            InfoRow(label = "Modelo", value = equipment.model)
                            InfoRow(label = "Número de Serie", value = equipment.serialNumber)
                            InfoRow(label = "Código", value = equipment.code)

                            if (equipment.locationName.isNotBlank()) {
                                InfoRow(label = "Ubicación", value = equipment.locationName)
                            }

                            if (equipment.technicalDetails.isNotBlank()) {
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "Detalles Técnicos",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Text(
                                    text = equipment.technicalDetails,
                                    style = MaterialTheme.typography.bodyMedium,
                                    modifier = Modifier.padding(top = 4.dp)
                                )
                            }
                        }
                    }

                    // Power Toggle
                    PowerToggle(
                        isPoweredOn = equipment.isPoweredOn,
                        onToggle = { isPoweredOn ->
                            viewModel.togglePower(equipmentId, isPoweredOn)
                        },
                        enabled = !uiState.isUpdatingOperations
                    )

                    // Temperature Control
                    TemperatureControl(
                        currentTemperature = equipment.currentTemperature,
                        setTemperature = equipment.setTemperature,
                        optimalMin = equipment.optimalTemperatureMin,
                        optimalMax = equipment.optimalTemperatureMax,
                        onTemperatureChange = { newTemp ->
                            viewModel.updateTemperature(equipmentId, newTemp)
                        },
                        enabled = !uiState.isUpdatingOperations && equipment.isPoweredOn
                    )

                    // Energy Consumption Card
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant
                        )
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Bolt,
                                    contentDescription = "Energy",
                                    tint = Color(0xFFFFC107)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Consumo Energético",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column {
                                    Text(
                                        text = "Actual",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                    Text(
                                        text = "${equipment.energyConsumptionCurrent} ${equipment.energyConsumptionUnit}",
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold
                                    )
                                }

                                Column(horizontalAlignment = Alignment.End) {
                                    Text(
                                        text = "Promedio",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                    Text(
                                        text = "${equipment.energyConsumptionAverage} ${equipment.energyConsumptionUnit}",
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }

                    // Analytics Preview Button
                    Button(
                        onClick = { onNavigateToAnalytics(equipmentId) },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(
                            imageVector = Icons.Default.BarChart,
                            contentDescription = null
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Ver Analíticas Completas")
                    }
                }
            }
        }
    }
}

/**
 * Reusable info row component for displaying label-value pairs.
 */
@Composable
private fun InfoRow(
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.weight(1f)
        )
    }
}
