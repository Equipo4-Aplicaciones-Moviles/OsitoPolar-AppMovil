package com.example.ositopolarapp.features.equipment.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

/**
 * Temperature control component with slider and +/- buttons.
 * Allows user to adjust equipment temperature within optimal range.
 */
@Composable
fun TemperatureControl(
    currentTemperature: Double,
    setTemperature: Double,
    optimalMin: Double,
    optimalMax: Double,
    onTemperatureChange: (Double) -> Unit,
    enabled: Boolean = true,
    modifier: Modifier = Modifier
) {
    var sliderValue by remember(setTemperature) { mutableStateOf(setTemperature.toFloat()) }

    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Title
            Text(
                text = "Control de Temperatura",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Current and Set Temperature Display
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
                        text = "${currentTemperature}°C",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = getTemperatureColor(currentTemperature, optimalMin, optimalMax)
                    )
                }

                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "Objetivo",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "${sliderValue.toInt()}°C",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Temperature Slider with +/- buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Minus button
                IconButton(
                    onClick = {
                        if (sliderValue > optimalMin) {
                            sliderValue -= 0.5f
                            onTemperatureChange(sliderValue.toDouble())
                        }
                    },
                    enabled = enabled && sliderValue > optimalMin
                ) {
                    Icon(
                        imageVector = Icons.Default.Remove,
                        contentDescription = "Decrease temperature"
                    )
                }

                // Slider
                Slider(
                    value = sliderValue,
                    onValueChange = { sliderValue = it },
                    onValueChangeFinished = {
                        onTemperatureChange(sliderValue.toDouble())
                    },
                    valueRange = (optimalMin - 5.0).toFloat()..(optimalMax + 5.0).toFloat(),
                    steps = ((optimalMax - optimalMin + 10) * 2).toInt(), // 0.5 degree steps
                    enabled = enabled,
                    modifier = Modifier.weight(1f),
                    colors = SliderDefaults.colors(
                        thumbColor = MaterialTheme.colorScheme.primary,
                        activeTrackColor = MaterialTheme.colorScheme.primary,
                        inactiveTrackColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                )

                // Plus button
                IconButton(
                    onClick = {
                        if (sliderValue < optimalMax + 5) {
                            sliderValue += 0.5f
                            onTemperatureChange(sliderValue.toDouble())
                        }
                    },
                    enabled = enabled && sliderValue < optimalMax + 5
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Increase temperature"
                    )
                }
            }

            // Optimal Range Indicator
            Text(
                text = "Rango óptimo: ${optimalMin}°C - ${optimalMax}°C",
                style = MaterialTheme.typography.bodySmall,
                color = if (sliderValue in optimalMin..optimalMax) {
                    Color(0xFF4CAF50)
                } else {
                    Color(0xFFFFC107)
                },
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}

/**
 * Returns color based on temperature status.
 */
private fun getTemperatureColor(temp: Double, min: Double, max: Double): Color {
    return when {
        !temp.isFinite() -> Color(0xFF9E9E9E)
        temp < min || temp > max -> {
            val minDiff = kotlin.math.abs(temp - min)
            val maxDiff = kotlin.math.abs(temp - max)
            val threshold = kotlin.math.abs(max - min) * 0.2

            if (minDiff > threshold || maxDiff > threshold) {
                Color(0xFFF44336) // Critical - Red
            } else {
                Color(0xFFFFC107) // Warning - Amber
            }
        }
        else -> Color(0xFF4CAF50) // Normal - Green
    }
}
