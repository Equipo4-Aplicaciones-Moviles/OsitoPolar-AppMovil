package com.example.ositopolarapp.features.equipment.presentation.screens

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ositopolarapp.features.equipment.presentation.state.EquipmentListViewModel // O AddEquipmentViewModel si lo separaste

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEquipmentScreen(
    viewModel: EquipmentListViewModel, // Asegúrate de usar el ViewModel correcto
    ownerId: Int,
    onNavigateBack: () -> Unit
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    // Estados del Formulario
    var name by remember { mutableStateOf("") }
    var type by remember { mutableStateOf("Refrigerator") } // Valor por defecto (String)
    var model by remember { mutableStateOf("") }
    var serialNumber by remember { mutableStateOf("") }
    var brand by remember { mutableStateOf("") } // Antes manufacturer
    var locationName by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }

    // Coordenadas (Simuladas o ingresadas)
    var latitude by remember { mutableStateOf("-12.0464") }
    var longitude by remember { mutableStateOf("-77.0428") }

    // Dropdown Logic
    var expanded by remember { mutableStateOf(false) }
    val equipmentTypes = listOf("Refrigerator", "Freezer", "ColdRoom", "Industrial")

    // Efecto para manejar éxito/error
    LaunchedEffect(uiState.error) {
        uiState.error?.let {
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
            viewModel.clearError()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Agregar Equipo") },
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
                .padding(16.dp)
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Información Básica", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = name, onValueChange = { name = it },
                label = { Text("Nombre del equipo") },
                modifier = Modifier.fillMaxWidth()
            )

            // Selector de Tipo
            Box(modifier = Modifier.fillMaxWidth()) {
                ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = it }) {
                    OutlinedTextField(
                        value = type,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Tipo") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                        modifier = Modifier.menuAnchor().fillMaxWidth()
                    )
                    ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                        equipmentTypes.forEach { item ->
                            DropdownMenuItem(
                                text = { Text(item) },
                                onClick = { type = item; expanded = false }
                            )
                        }
                    }
                }
            }

            Row(modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value = model, onValueChange = { model = it },
                    label = { Text("Modelo") }, modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(8.dp))
                OutlinedTextField(
                    value = brand, onValueChange = { brand = it },
                    label = { Text("Marca") }, modifier = Modifier.weight(1f)
                )
            }

            OutlinedTextField(
                value = serialNumber, onValueChange = { serialNumber = it },
                label = { Text("Número de Serie") }, modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))
            Text("Ubicación", style = MaterialTheme.typography.titleMedium)

            OutlinedTextField(
                value = locationName, onValueChange = { locationName = it },
                label = { Text("Nombre de Ubicación") }, modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = address, onValueChange = { address = it },
                label = { Text("Dirección") }, modifier = Modifier.fillMaxWidth()
            )

            Row(modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value = latitude, onValueChange = { latitude = it },
                    label = { Text("Latitud") }, modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(8.dp))
                OutlinedTextField(
                    value = longitude, onValueChange = { longitude = it },
                    label = { Text("Longitud") }, modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    if (name.isBlank() || serialNumber.isBlank()) {
                        Toast.makeText(context, "Llene los campos obligatorios", Toast.LENGTH_SHORT).show()
                    } else {
                        // --- AQUÍ ESTABA EL ERROR DE PARÁMETROS ---
                        viewModel.addEquipment(
                            ownerId = ownerId,
                            name = name,
                            type = type, // Pasamos String directo
                            model = model,
                            serialNumber = serialNumber,
                            brand = brand, // Antes era manufacturer
                            location = locationName, // Antes locationName
                            address = address, // Antes locationAddress
                            latitude = latitude.toDoubleOrNull() ?: 0.0,
                            longitude = longitude.toDoubleOrNull() ?: 0.0
                        )
                        // Volver atrás tras guardar (opcional, o esperar éxito en UI state)
                        onNavigateBack()
                    }
                },
                modifier = Modifier.fillMaxWidth().height(50.dp)
            ) {
                Text("Guardar Equipo")
            }
        }
    }
}