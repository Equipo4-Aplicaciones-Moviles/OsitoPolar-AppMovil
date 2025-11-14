package com.example.ositopolarapp.features.analytics.presentation.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.ositopolarapp.features.equipment.domain.model.Equipment

/**
 * Equipment Analytics Screen
 *
 * Displays detailed analytics for a single equipment:
 * - Current status card
 * - Temperature trend (last 24 hours)
 * - Energy consumption
 * - Health metrics
 * - Performance insights
 *
 * NOTE: Using mock data for charts. Real implementation would fetch from:
 * GET /api/v1/analytics/equipments/{id}/readings
 * GET /api/v1/analytics/equipments/{id}/summaries
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EquipmentAnalyticsScreen(
    equipment: Equipment,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Mock temperature data for last 24 hours
    val temperatureData = remember {
        List(24) { hour ->
            val temp = equipment.optimalTemperatureMin +
                       (Math.random() * (equipment.optimalTemperatureMax - equipment.optimalTemperatureMin))
            Pair(hour, temp)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "Analytics",
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = equipment.name,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
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
                    IconButton(onClick = { /* TODO: Refresh data */ }) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "Actualizar"
                        )
                    }
                }
            )
        },
        modifier = modifier
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Current Status Card
            CurrentStatusCard(equipment = equipment)

            // Temperature Overview Card
            TemperatureOverviewCard(
                current = equipment.currentTemperature,
                target = equipment.setTemperature,
                min = equipment.optimalTemperatureMin,
                max = equipment.optimalTemperatureMax
            )

            // Temperature Trend Chart
            TemperatureTrendCard(
                data = temperatureData,
                optimalMin = equipment.optimalTemperatureMin,
                optimalMax = equipment.optimalTemperatureMax
            )

            // Energy Consumption Card
            EnergyConsumptionCard(
                current = equipment.energyConsumptionCurrent,
                average = equipment.energyConsumptionAverage,
                unit = equipment.energyConsumptionUnit
            )

            // Health Metrics Card
            HealthMetricsCard(equipment = equipment)

            // Performance Insights
            PerformanceInsightsCard(equipment = equipment)
        }
    }
}

@Composable
private fun CurrentStatusCard(equipment: Equipment) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = if (equipment.isPoweredOn) {
                MaterialTheme.colorScheme.primaryContainer
            } else {
                MaterialTheme.colorScheme.surfaceVariant
            }
        )
    ) {
        Row(
            modifier = Modifier.padding(20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    text = "Estado Actual",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
                )
                Text(
                    text = if (equipment.isPoweredOn) "Operando" else "Apagado",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Text(
                    text = equipment.status.name,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                )
            }

            Surface(
                shape = MaterialTheme.shapes.extraLarge,
                color = if (equipment.isPoweredOn) {
                    Color(0xFF4CAF50)
                } else {
                    Color(0xFF9E9E9E)
                },
                modifier = Modifier.size(60.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text = if (equipment.isPoweredOn) "ON" else "OFF",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
        }
    }
}

@Composable
private fun TemperatureOverviewCard(
    current: Double,
    target: Double,
    min: Double,
    max: Double
) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Temperatura",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                TemperatureMetric(
                    label = "Actual",
                    value = String.format("%.1f°C", current),
                    color = MaterialTheme.colorScheme.primary
                )
                TemperatureMetric(
                    label = "Objetivo",
                    value = String.format("%.1f°C", target),
                    color = MaterialTheme.colorScheme.secondary
                )
            }

            Divider()

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Rango óptimo:",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = String.format("%.1f°C - %.1f°C", min, max),
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

@Composable
private fun TemperatureMetric(label: String, value: String, color: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = value,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = color
        )
    }
}

@Composable
private fun TemperatureTrendCard(
    data: List<Pair<Int, Double>>,
    optimalMin: Double,
    optimalMax: Double
) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Tendencia (últimas 24h)",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            // Simple bar chart representation
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                data.takeLast(12).forEachIndexed { index, (hour, temp) ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "${24 - (12 - index)}h",
                            style = MaterialTheme.typography.labelSmall,
                            modifier = Modifier.width(30.dp)
                        )

                        val barColor = when {
                            temp < optimalMin || temp > optimalMax -> Color(0xFFF44336)
                            else -> Color(0xFF4CAF50)
                        }

                        LinearProgressIndicator(
                            progress = (temp / (optimalMax + 2)).toFloat(),
                            modifier = Modifier.weight(1f),
                            color = barColor
                        )

                        Text(
                            text = String.format("%.1f°", temp),
                            style = MaterialTheme.typography.labelSmall,
                            modifier = Modifier.width(45.dp)
                        )
                    }
                }
            }

            Divider()

            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                LegendItem(color = Color(0xFF4CAF50), label = "Normal")
                LegendItem(color = Color(0xFFF44336), label = "Fuera de rango")
            }
        }
    }
}

@Composable
private fun LegendItem(color: Color, label: String) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Surface(
            modifier = Modifier.size(12.dp),
            shape = MaterialTheme.shapes.small,
            color = color
        ) {}
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall
        )
    }
}

@Composable
private fun EnergyConsumptionCard(
    current: Double,
    average: Double,
    unit: String
) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Consumo Energético",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "Actual",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "$current $unit",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "Promedio",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "$average $unit",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            val efficiency = ((average / current) * 100).coerceIn(0.0, 150.0)
            val efficiencyColor = when {
                efficiency >= 95 -> Color(0xFF4CAF50)
                efficiency >= 80 -> Color(0xFFFFC107)
                else -> Color(0xFFF44336)
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Eficiencia:",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = String.format("%.1f%%", efficiency),
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    color = efficiencyColor
                )
            }
        }
    }
}

@Composable
private fun HealthMetricsCard(equipment: Equipment) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Métricas de Salud",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            HealthMetricRow(
                label = "Temperatura",
                status = if (equipment.currentTemperature in equipment.optimalTemperatureMin..equipment.optimalTemperatureMax)
                    "Normal" else "Requiere atención",
                statusColor = if (equipment.currentTemperature in equipment.optimalTemperatureMin..equipment.optimalTemperatureMax)
                    Color(0xFF4CAF50) else Color(0xFFFFC107)
            )

            HealthMetricRow(
                label = "Energía",
                status = "Óptimo",
                statusColor = Color(0xFF4CAF50)
            )

            HealthMetricRow(
                label = "Estado General",
                status = equipment.status.name,
                statusColor = Color(0xFF4CAF50)
            )
        }
    }
}

@Composable
private fun HealthMetricRow(label: String, status: String, statusColor: Color) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium
        )
        Surface(
            color = statusColor.copy(alpha = 0.2f),
            shape = MaterialTheme.shapes.small
        ) {
            Text(
                text = status,
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.SemiBold,
                color = statusColor,
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
            )
        }
    }
}

@Composable
private fun PerformanceInsightsCard(equipment: Equipment) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.tertiaryContainer
        )
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "💡",
                    style = MaterialTheme.typography.headlineSmall
                )
                Text(
                    text = "Insights de Rendimiento",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }

            Text(
                text = "• El equipo está operando dentro de parámetros normales",
                style = MaterialTheme.typography.bodyMedium
            )

            if (equipment.isPoweredOn) {
                Text(
                    text = "• Consumo energético está ${if (equipment.energyConsumptionCurrent <= equipment.energyConsumptionAverage) "por debajo" else "por encima"} del promedio",
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Text(
                text = "• Próximo mantenimiento recomendado en 30 días",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}
