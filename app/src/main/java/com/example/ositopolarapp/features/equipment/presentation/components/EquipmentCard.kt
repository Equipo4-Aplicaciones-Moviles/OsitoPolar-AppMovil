package com.example.ositopolarapp.features.equipment.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DeviceThermostat
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.ositopolarapp.features.equipment.domain.model.Equipment

@Composable
fun EquipmentCard(
    equipment: Equipment,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 16.dp)
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = equipment.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                // Chip de estado simple
                Surface(
                    color = if (equipment.status == "Active") Color(0xFFE8F5E9) else Color(0xFFFFEBEE),
                    shape = MaterialTheme.shapes.small
                ) {
                    Text(
                        text = equipment.status,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        color = if (equipment.status == "Active") Color(0xFF2E7D32) else Color(0xFFC62828),
                        style = MaterialTheme.typography.labelSmall
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // CORRECCIÓN: Usamos 'brand' y 'model'
            Text(text = "${equipment.brand} - ${equipment.model}", style = MaterialTheme.typography.bodyMedium)

            Spacer(modifier = Modifier.height(12.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp),
                    tint = Color.Gray
                )
                Spacer(modifier = Modifier.width(4.dp))
                // CORRECCIÓN: Usamos 'location' en vez de 'locationName'
                Text(text = equipment.location, style = MaterialTheme.typography.bodySmall, color = Color.Gray)

                Spacer(modifier = Modifier.weight(1f))

                Icon(
                    imageVector = Icons.Default.DeviceThermostat,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp),
                    tint = Color.Gray
                )
                Spacer(modifier = Modifier.width(4.dp))
                // CORRECCIÓN: Usamos 'temperature'
                Text(text = "${equipment.temperature}°C", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold)
            }
        }
    }
}