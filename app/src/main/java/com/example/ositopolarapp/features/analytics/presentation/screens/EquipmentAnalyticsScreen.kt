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
import com.example.ositopolarapp.features.analytics.presentation.state.AnalyticsUiState
import com.example.ositopolarapp.features.analytics.data.dto.*
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

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
 * Now integrated with real backend data via AnalyticsViewModel
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EquipmentAnalyticsScreen(
    equipment: Equipment,
    analyticsState: AnalyticsUiState,
    onRefresh: () -> Unit,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Extract temperature readings from analytics state
    val temperatureReadings = remember(analyticsState.readings) {
        analyticsState.readings
            .filter { it.type == "temperature" }
            .sortedBy { it.timestamp }
    }

    // Convert readings to chart data format (hour, temperature)
    val temperatureData = remember(temperatureReadings) {
        temperatureReadings.map { reading ->
            val instant = Instant.parse(reading.timestamp)
            val hour = instant.atZone(ZoneId.systemDefault()).hour
            Pair(hour, reading.value)
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
                    IconButton(onClick = onRefresh) {
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
        Box(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
            // Main content
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Show error if exists
                if (analyticsState.error != null) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.errorContainer
                        )
                    ) {
                        Text(
                            text = "Error: ${analyticsState.error}",
                            modifier = Modifier.padding(16.dp),
                            color = MaterialTheme.colorScheme.onErrorContainer
                        )
                    }
                }

                // Show message if no data
                if (!analyticsState.isLoading && temperatureData.isEmpty()) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant
                        )
                    ) {
                        Text(
                            text = "No hay datos de analíticas disponibles. Los datos se generan cuando el equipo envía lecturas de temperatura y energía.",
                            modifier = Modifier.padding(16.dp),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
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

            // Advanced Analytics Section
            Text(
                text = "Analíticas Avanzadas",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 8.dp)
            )

            // Health Score Card
            analyticsState.healthScore?.let { healthScore ->
                HealthScoreCard(healthScore = healthScore)
            }

            // Anomalies Card
            analyticsState.anomalies?.let { anomalies ->
                AnomaliesCard(anomalies = anomalies)
            }

            // Cost Analysis Card
            analyticsState.costAnalysis?.let { costAnalysis ->
                CostAnalysisCard(costAnalysis = costAnalysis)
            }

            // Maintenance Forecast Card
            analyticsState.maintenanceForecast?.let { forecast ->
                MaintenanceForecastCard(forecast = forecast)
            }
            }

            // Loading indicator
            if (analyticsState.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            }
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

// ========== ADVANCED ANALYTICS CARDS ==========

@Composable
private fun HealthScoreCard(healthScore: HealthScoreResponse) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = when (healthScore.status) {
                "Excellent" -> Color(0xFF4CAF50).copy(alpha = 0.1f)
                "Good" -> Color(0xFF8BC34A).copy(alpha = 0.1f)
                "Fair" -> Color(0xFFFFC107).copy(alpha = 0.1f)
                "Poor" -> Color(0xFFFF9800).copy(alpha = 0.1f)
                else -> Color(0xFFF44336).copy(alpha = 0.1f) // Critical
            }
        )
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Puntuación de Salud",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                Surface(
                    color = when (healthScore.status) {
                        "Excellent" -> Color(0xFF4CAF50)
                        "Good" -> Color(0xFF8BC34A)
                        "Fair" -> Color(0xFFFFC107)
                        "Poor" -> Color(0xFFFF9800)
                        else -> Color(0xFFF44336)
                    },
                    shape = MaterialTheme.shapes.small
                ) {
                    Text(
                        text = healthScore.status,
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                    )
                }
            }

            // Health Score visual
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = String.format("%.1f", healthScore.healthScore),
                    style = MaterialTheme.typography.displayLarge,
                    fontWeight = FontWeight.Bold,
                    color = when (healthScore.status) {
                        "Excellent" -> Color(0xFF4CAF50)
                        "Good" -> Color(0xFF8BC34A)
                        "Fair" -> Color(0xFFFFC107)
                        "Poor" -> Color(0xFFFF9800)
                        else -> Color(0xFFF44336)
                    }
                )
                Text(
                    text = " / 100",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Divider()

            // Metrics
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "Estabilidad de Temperatura",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = String.format("%.2f%%", healthScore.temperatureStability),
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "Desviación Promedio",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = String.format("%.2f°C", healthScore.averageDeviation),
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            // Analysis info
            Text(
                text = "Basado en ${healthScore.readingsCount} lecturas de las últimas ${healthScore.hoursAnalyzed}h",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun AnomaliesCard(anomalies: AnomaliesResponse) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Anomalías Detectadas",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                if (anomalies.totalAnomalies > 0) {
                    Surface(
                        color = Color(0xFFF44336).copy(alpha = 0.2f),
                        shape = MaterialTheme.shapes.small
                    ) {
                        Text(
                            text = "${anomalies.totalAnomalies}",
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFF44336),
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                        )
                    }
                }
            }

            if (anomalies.totalAnomalies == 0) {
                Text(
                    text = "✓ No se detectaron anomalías en las últimas ${anomalies.hoursAnalyzed}h",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFF4CAF50)
                )
            } else {
                // Show each anomaly
                anomalies.anomaliesDetected.forEach { anomaly ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant
                        )
                    ) {
                        Column(
                            modifier = Modifier.padding(12.dp),
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = anomaly.type,
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.SemiBold
                                )

                                Surface(
                                    color = when (anomaly.severity) {
                                        "Critical" -> Color(0xFFF44336)
                                        "High" -> Color(0xFFFF9800)
                                        "Medium" -> Color(0xFFFFC107)
                                        else -> Color(0xFF2196F3)
                                    }.copy(alpha = 0.2f),
                                    shape = MaterialTheme.shapes.extraSmall
                                ) {
                                    Text(
                                        text = anomaly.severity,
                                        style = MaterialTheme.typography.labelSmall,
                                        fontWeight = FontWeight.Bold,
                                        color = when (anomaly.severity) {
                                            "Critical" -> Color(0xFFF44336)
                                            "High" -> Color(0xFFFF9800)
                                            "Medium" -> Color(0xFFFFC107)
                                            else -> Color(0xFF2196F3)
                                        },
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                    )
                                }
                            }

                            Text(
                                text = anomaly.description,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                anomaly.duration?.let {
                                    Text(
                                        text = "Duración: $it",
                                        style = MaterialTheme.typography.labelSmall
                                    )
                                }

                                anomaly.temperatureChange?.let {
                                    Text(
                                        text = "Cambio: ${if (it > 0) "+" else ""}${String.format("%.1f°C", it)}",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = if (it > 0) Color(0xFFF44336) else Color(0xFF2196F3)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun CostAnalysisCard(costAnalysis: CostAnalysisResponse) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Análisis de Costos",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            // Current period
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Período Actual",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "$${String.format("%.2f", costAnalysis.currentPeriod.totalCost)}",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = "${String.format("%.2f", costAnalysis.currentPeriod.totalEnergyKwh)} kWh",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                // Trend indicator
                Surface(
                    color = when (costAnalysis.comparison.trend) {
                        "Decreasing" -> Color(0xFF4CAF50).copy(alpha = 0.2f)
                        "Increasing" -> Color(0xFFF44336).copy(alpha = 0.2f)
                        else -> Color(0xFF2196F3).copy(alpha = 0.2f)
                    },
                    shape = MaterialTheme.shapes.small
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = when (costAnalysis.comparison.trend) {
                                "Decreasing" -> "↓"
                                "Increasing" -> "↑"
                                else -> "→"
                            },
                            style = MaterialTheme.typography.headlineSmall,
                            color = when (costAnalysis.comparison.trend) {
                                "Decreasing" -> Color(0xFF4CAF50)
                                "Increasing" -> Color(0xFFF44336)
                                else -> Color(0xFF2196F3)
                            }
                        )
                        Text(
                            text = costAnalysis.comparison.trend,
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.SemiBold,
                            color = when (costAnalysis.comparison.trend) {
                                "Decreasing" -> Color(0xFF4CAF50)
                                "Increasing" -> Color(0xFFF44336)
                                else -> Color(0xFF2196F3)
                            }
                        )
                    }
                }
            }

            Divider()

            // Comparison with previous period
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "Cambio en Energía",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "${if (costAnalysis.comparison.energyChangePercent > 0) "+" else ""}${String.format("%.1f%%", costAnalysis.comparison.energyChangePercent)}",
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.SemiBold,
                        color = if (costAnalysis.comparison.energyChangePercent > 0) Color(0xFFF44336) else Color(0xFF4CAF50)
                    )
                }

                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "Cambio en Costo",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "${if (costAnalysis.comparison.costChangePercent > 0) "+" else ""}${String.format("%.1f%%", costAnalysis.comparison.costChangePercent)}",
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.SemiBold,
                        color = if (costAnalysis.comparison.costChangePercent > 0) Color(0xFFF44336) else Color(0xFF4CAF50)
                    )
                }
            }

            // Estimated monthly cost
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.secondaryContainer,
                shape = MaterialTheme.shapes.small
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Costo Mensual Estimado:",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = "$${String.format("%.2f", costAnalysis.comparison.estimatedMonthlyCost)}",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }

            // Cost per kWh info
            Text(
                text = "Tarifa: $${String.format("%.2f", costAnalysis.costPerKwh)}/kWh",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun MaintenanceForecastCard(forecast: MaintenanceForecastResponse) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = when (forecast.forecast.priority) {
                "Critical" -> Color(0xFFF44336).copy(alpha = 0.1f)
                "High" -> Color(0xFFFF9800).copy(alpha = 0.1f)
                "Medium" -> Color(0xFFFFC107).copy(alpha = 0.1f)
                else -> Color(0xFF4CAF50).copy(alpha = 0.1f)
            }
        )
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Pronóstico de Mantenimiento",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                Surface(
                    color = when (forecast.forecast.priority) {
                        "Critical" -> Color(0xFFF44336)
                        "High" -> Color(0xFFFF9800)
                        "Medium" -> Color(0xFFFFC107)
                        else -> Color(0xFF4CAF50)
                    },
                    shape = MaterialTheme.shapes.small
                ) {
                    Text(
                        text = forecast.forecast.priority,
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                    )
                }
            }

            // Next maintenance date
            forecast.forecast.nextMaintenanceDate?.let { date ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Próximo Mantenimiento",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = date,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    forecast.forecast.daysUntilMaintenance?.let { days ->
                        Surface(
                            color = MaterialTheme.colorScheme.secondaryContainer,
                            shape = MaterialTheme.shapes.small
                        ) {
                            Text(
                                text = "En $days días",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                            )
                        }
                    }
                }
            }

            Divider()

            // Confidence and basis
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "Confianza",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = forecast.forecast.confidence,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                forecast.forecast.estimatedCost?.let { cost ->
                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            text = "Costo Estimado",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = "$${String.format("%.2f", cost)}",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }

            // Based on
            Text(
                text = "Basado en: ${forecast.forecast.basedOn}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            // Recommendations
            if (forecast.recommendations.isNotEmpty()) {
                Divider()

                Text(
                    text = "Recomendaciones",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.SemiBold
                )

                forecast.recommendations.forEach { recommendation ->
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "•",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Text(
                            text = recommendation,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}
