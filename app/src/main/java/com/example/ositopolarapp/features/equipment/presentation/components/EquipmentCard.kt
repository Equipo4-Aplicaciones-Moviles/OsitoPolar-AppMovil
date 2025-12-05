package com.example.ositopolarapp.features.equipment.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.ositopolarapp.features.equipment.domain.model.Equipment
import com.example.ositopolarapp.features.equipment.domain.model.TemperatureStatus

/**
 * Card component for displaying equipment in a list or grid.
 * Shows equipment name, type, current temperature, and status indicator.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EquipmentCard(
    equipment: Equipment,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Header: Name and Status Indicator
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = equipment.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = equipment.getTypeDisplay(),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                // Status Indicator
                TemperatureStatusIndicator(
                    status = equipment.getTemperatureStatus(),
                    modifier = Modifier.size(12.dp)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Temperature Display
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Thermostat,
                    contentDescription = "Temperature",
                    modifier = Modifier.size(20.dp),
                    tint = Color(equipment.getStatusColor())
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "${equipment.currentTemperature}°C",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = Color(equipment.getStatusColor())
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "/ ${equipment.setTemperature}°C",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Footer: Power Status and Location
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Power Status
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = if (equipment.isPoweredOn) Icons.Default.PowerSettingsNew else Icons.Default.PowerOff,
                        contentDescription = "Power",
                        modifier = Modifier.size(16.dp),
                        tint = if (equipment.isPoweredOn) Color(0xFF4CAF50) else Color(0xFF9E9E9E)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = if (equipment.isPoweredOn) "Encendido" else "Apagado",
                        style = MaterialTheme.typography.bodySmall,
                        color = if (equipment.isPoweredOn) Color(0xFF4CAF50) else Color(0xFF9E9E9E)
                    )
                }

                // Location indicator
                if (equipment.hasLocation()) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = "Location",
                            modifier = Modifier.size(16.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = equipment.locationName,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            maxLines = 1
                        )
                    }
                }
            }
        }
    }
}

/**
 * Temperature status indicator dot.
 */
@Composable
fun TemperatureStatusIndicator(
    status: TemperatureStatus,
    modifier: Modifier = Modifier
) {
    val color = when (status) {
        TemperatureStatus.NORMAL -> Color(0xFF4CAF50)      // Green
        TemperatureStatus.WARNING -> Color(0xFFFFC107)     // Amber
        TemperatureStatus.CRITICAL -> Color(0xFFF44336)    // Red
        TemperatureStatus.OFF -> Color(0xFF9E9E9E)         // Gray
    }

    Box(
        modifier = modifier
            .clip(CircleShape)
            .background(color)
    )
}
