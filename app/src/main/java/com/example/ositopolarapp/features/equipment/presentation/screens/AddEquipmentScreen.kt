package com.example.ositopolarapp.features.equipment.presentation.screens

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.ositopolarapp.features.equipment.presentation.state.EquipmentListViewModel
import kotlin.random.Random

/**
 * Add Equipment Screen
 *
 * Form to add new equipment to the user's inventory.
 * Features:
 * - Basic information (name, type, model, manufacturer)
 * - Temperature settings
 * - Location information
 * - Energy consumption settings
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEquipmentScreen(
    viewModel: EquipmentListViewModel,
    ownerId: Int,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()

    // Form state
    var name by remember { mutableStateOf("") }
    var type by remember { mutableStateOf("Refrigerator") }
    var model by remember { mutableStateOf("") }
    var manufacturer by remember { mutableStateOf("") }
    var serialNumber by remember { mutableStateOf(generateSerialNumber()) }
    var code by remember { mutableStateOf(generateCode()) }
    var cost by remember { mutableStateOf("") }
    var technicalDetails by remember { mutableStateOf("") }

    // Temperature settings
    var currentTemperature by remember { mutableStateOf("2.0") }
    var setTemperature by remember { mutableStateOf("2.0") }
    var optimalMin by remember { mutableStateOf("1.5") }
    var optimalMax by remember { mutableStateOf("2.5") }

    // Location
    var locationName by remember { mutableStateOf("") }
    var locationAddress by remember { mutableStateOf("") }
    var latitude by remember { mutableStateOf("") }
    var longitude by remember { mutableStateOf("") }

    // Energy
    var energyConsumptionCurrent by remember { mutableStateOf("1.5") }
    var energyConsumptionUnit by remember { mutableStateOf("kWh") }
    var energyConsumptionAverage by remember { mutableStateOf("1.4") }

    // Status
    var isPoweredOn by remember { mutableStateOf(true) }
    var status by remember { mutableStateOf("Active") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Agregar Equipo",
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
            // Basic Information Section
            Text(
                text = "Información Básica",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )

            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Nombre del equipo *") },
                placeholder = { Text("Ej: Refrigerador Principal") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            // Type Dropdown
            var expandedType by remember { mutableStateOf(false) }
            ExposedDropdownMenuBox(
                expanded = expandedType,
                onExpandedChange = { expandedType = it }
            ) {
                OutlinedTextField(
                    value = type,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Tipo *") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedType) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor()
                )
                ExposedDropdownMenu(
                    expanded = expandedType,
                    onDismissRequest = { expandedType = false }
                ) {
                    listOf("Freezer", "ColdRoom", "Refrigerator").forEach { t ->
                        DropdownMenuItem(
                            text = { Text(t) },
                            onClick = {
                                type = t
                                expandedType = false
                            }
                        )
                    }
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedTextField(
                    value = model,
                    onValueChange = { model = it },
                    label = { Text("Modelo *") },
                    placeholder = { Text("R-500") },
                    modifier = Modifier.weight(1f),
                    singleLine = true
                )
                OutlinedTextField(
                    value = manufacturer,
                    onValueChange = { manufacturer = it },
                    label = { Text("Fabricante *") },
                    placeholder = { Text("ColdTech") },
                    modifier = Modifier.weight(1f),
                    singleLine = true
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedTextField(
                    value = serialNumber,
                    onValueChange = { serialNumber = it },
                    label = { Text("Número de serie") },
                    modifier = Modifier.weight(1f),
                    singleLine = true
                )
                OutlinedTextField(
                    value = code,
                    onValueChange = { code = it },
                    label = { Text("Código") },
                    modifier = Modifier.weight(1f),
                    singleLine = true
                )
            }

            OutlinedTextField(
                value = cost,
                onValueChange = { cost = it },
                label = { Text("Costo") },
                placeholder = { Text("5000.00") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            OutlinedTextField(
                value = technicalDetails,
                onValueChange = { technicalDetails = it },
                label = { Text("Detalles técnicos") },
                placeholder = { Text("Características especiales del equipo") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 2
            )

            Divider()

            // Temperature Settings
            Text(
                text = "Configuración de Temperatura",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedTextField(
                    value = currentTemperature,
                    onValueChange = { currentTemperature = it },
                    label = { Text("Temp. actual (°C)") },
                    modifier = Modifier.weight(1f),
                    singleLine = true
                )
                OutlinedTextField(
                    value = setTemperature,
                    onValueChange = { setTemperature = it },
                    label = { Text("Temp. objetivo (°C)") },
                    modifier = Modifier.weight(1f),
                    singleLine = true
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedTextField(
                    value = optimalMin,
                    onValueChange = { optimalMin = it },
                    label = { Text("Temp. mín (°C)") },
                    modifier = Modifier.weight(1f),
                    singleLine = true
                )
                OutlinedTextField(
                    value = optimalMax,
                    onValueChange = { optimalMax = it },
                    label = { Text("Temp. máx (°C)") },
                    modifier = Modifier.weight(1f),
                    singleLine = true
                )
            }

            Divider()

            // Location
            Text(
                text = "Ubicación",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )

            OutlinedTextField(
                value = locationName,
                onValueChange = { locationName = it },
                label = { Text("Nombre de ubicación *") },
                placeholder = { Text("Almacén Principal") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            OutlinedTextField(
                value = locationAddress,
                onValueChange = { locationAddress = it },
                label = { Text("Dirección *") },
                placeholder = { Text("Av. Principal 123, Lima") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 2
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedTextField(
                    value = latitude,
                    onValueChange = { latitude = it },
                    label = { Text("Latitud") },
                    placeholder = { Text("-12.0464") },
                    modifier = Modifier.weight(1f),
                    singleLine = true
                )
                OutlinedTextField(
                    value = longitude,
                    onValueChange = { longitude = it },
                    label = { Text("Longitud") },
                    placeholder = { Text("-77.0428") },
                    modifier = Modifier.weight(1f),
                    singleLine = true
                )
            }

            Divider()

            // Energy Consumption
            Text(
                text = "Consumo Energético",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedTextField(
                    value = energyConsumptionCurrent,
                    onValueChange = { energyConsumptionCurrent = it },
                    label = { Text("Consumo actual") },
                    modifier = Modifier.weight(1f),
                    singleLine = true
                )
                OutlinedTextField(
                    value = energyConsumptionUnit,
                    onValueChange = { energyConsumptionUnit = it },
                    label = { Text("Unidad") },
                    modifier = Modifier.weight(1f),
                    singleLine = true
                )
            }

            OutlinedTextField(
                value = energyConsumptionAverage,
                onValueChange = { energyConsumptionAverage = it },
                label = { Text("Consumo promedio") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Divider()

            // Status
            Text(
                text = "Estado",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Encendido", style = MaterialTheme.typography.bodyLarge)
                Switch(
                    checked = isPoweredOn,
                    onCheckedChange = { isPoweredOn = it }
                )
            }

            var expandedStatus by remember { mutableStateOf(false) }
            ExposedDropdownMenuBox(
                expanded = expandedStatus,
                onExpandedChange = { expandedStatus = it }
            ) {
                OutlinedTextField(
                    value = status,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Estado operacional") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedStatus) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor()
                )
                ExposedDropdownMenu(
                    expanded = expandedStatus,
                    onDismissRequest = { expandedStatus = false }
                ) {
                    listOf("Active", "Inactive", "Maintenance", "Retired").forEach { s ->
                        DropdownMenuItem(
                            text = { Text(s) },
                            onClick = {
                                status = s
                                expandedStatus = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Submit Button
            Button(
                onClick = {
                    if (name.isNotBlank() && model.isNotBlank() && manufacturer.isNotBlank() &&
                        locationName.isNotBlank() && locationAddress.isNotBlank()
                    ) {
                        // TODO: Call viewModel.createEquipment()
                        Toast.makeText(context, "Equipo agregado exitosamente", Toast.LENGTH_SHORT)
                            .show()
                        onNavigateBack()
                    } else {
                        Toast.makeText(
                            context,
                            "Por favor completa todos los campos requeridos",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                },
                modifier = Modifier.fillMaxWidth().height(56.dp)
            ) {
                Text("Agregar Equipo", style = MaterialTheme.typography.titleMedium)
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

private fun generateSerialNumber(): String {
    val prefix = "SN"
    val randomNum = Random.nextInt(100000, 999999)
    return "$prefix-$randomNum"
}

private fun generateCode(): String {
    val prefix = "EQ"
    val randomNum = Random.nextInt(1000, 9999)
    return "$prefix-$randomNum"
}
