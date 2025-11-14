package com.example.ositopolarapp.features.equipment.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PowerOff
import androidx.compose.material.icons.filled.PowerSettingsNew
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

/**
 * Power toggle component for equipment.
 * Displays power status and allows toggling power on/off.
 */
@Composable
fun PowerToggle(
    isPoweredOn: Boolean,
    onToggle: (Boolean) -> Unit,
    enabled: Boolean = true,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = if (isPoweredOn) {
                Color(0xFF4CAF50).copy(alpha = 0.1f)
            } else {
                MaterialTheme.colorScheme.surfaceVariant
            }
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icon and Label
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = if (isPoweredOn) Icons.Default.PowerSettingsNew else Icons.Default.PowerOff,
                    contentDescription = "Power Status",
                    modifier = Modifier.size(32.dp),
                    tint = if (isPoweredOn) Color(0xFF4CAF50) else Color(0xFF9E9E9E)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = "Estado del Equipo",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = if (isPoweredOn) "Encendido" else "Apagado",
                        style = MaterialTheme.typography.bodyMedium,
                        color = if (isPoweredOn) Color(0xFF4CAF50) else Color(0xFF9E9E9E)
                    )
                }
            }

            // Toggle Switch
            Switch(
                checked = isPoweredOn,
                onCheckedChange = onToggle,
                enabled = enabled,
                colors = SwitchDefaults.colors(
                    checkedThumbColor = Color(0xFF4CAF50),
                    checkedTrackColor = Color(0xFF4CAF50).copy(alpha = 0.5f),
                    uncheckedThumbColor = Color(0xFF9E9E9E),
                    uncheckedTrackColor = Color(0xFF9E9E9E).copy(alpha = 0.5f)
                )
            )
        }
    }
}
