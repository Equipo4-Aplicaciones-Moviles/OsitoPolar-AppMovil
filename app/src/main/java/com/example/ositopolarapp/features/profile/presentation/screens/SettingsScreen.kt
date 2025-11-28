package com.example.ositopolarapp.features.profile.presentation.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
// --- CORRECCIÓN DE IMPORTS ---
import com.example.ositopolarapp.core.data.network.PreferencesManager // Faltaba .network
import com.example.ositopolarapp.features.profile.data.dto.ProfileDto // Importa si lo usas

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    preferencesManager: PreferencesManager,
    onNavigateBack: () -> Unit
) {
    // Estado para el tema (leemos directamente de prefs)
    var isDarkTheme by remember { mutableStateOf(preferencesManager.getThemePreference()) }
    var notificationsEnabled by remember { mutableStateOf(preferencesManager.getNotificationPreference()) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Configuración") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize()
        ) {
            Text("General", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(16.dp))

            // Switch Tema Oscuro
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Tema Oscuro")
                Switch(
                    checked = isDarkTheme,
                    onCheckedChange = { checked ->
                        isDarkTheme = checked
                        preferencesManager.saveThemePreference(checked)
                    }
                )
            }

            Divider(modifier = Modifier.padding(vertical = 16.dp))

            // Switch Notificaciones
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Notificaciones")
                Switch(
                    checked = notificationsEnabled,
                    onCheckedChange = { checked ->
                        notificationsEnabled = checked
                        preferencesManager.saveNotificationPreference(checked)
                    }
                )
            }
        }
    }
}