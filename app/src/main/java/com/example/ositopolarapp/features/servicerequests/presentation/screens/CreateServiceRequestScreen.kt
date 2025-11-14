package com.example.ositopolarapp.features.servicerequests.presentation.screens

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
import com.example.ositopolarapp.features.servicerequests.presentation.state.ServiceRequestViewModel

/**
 * Create Service Request Screen
 *
 * Simplified form for creating a new service request.
 * TODO: Convert to 3-step wizard as per Vue.js frontend
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateServiceRequestScreen(
    viewModel: ServiceRequestViewModel,
    equipmentId: Int, // Pass from navigation
    userId: Int, // Get from auth state
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var issueDetails by remember { mutableStateOf("") }
    var serviceType by remember { mutableStateOf("REPAIR") }
    var priority by remember { mutableStateOf("MEDIUM") }
    var urgency by remember { mutableStateOf("NORMAL") }
    var isEmergency by remember { mutableStateOf(false) }
    var address by remember { mutableStateOf("") }

    // Handle success
    LaunchedEffect(uiState.createSuccess) {
        if (uiState.createSuccess) {
            Toast.makeText(context, "Solicitud creada exitosamente", Toast.LENGTH_SHORT).show()
            viewModel.clearCreateSuccess()
            onNavigateBack()
        }
    }

    // Show error toast
    LaunchedEffect(uiState.error) {
        uiState.error?.let {
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
            viewModel.clearError()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Nueva Solicitud de Servicio",
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
            // Title
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Título") },
                placeholder = { Text("Ej: Falla en el compresor") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            // Description
            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Descripción") },
                placeholder = { Text("Describe el problema...") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3
            )

            // Issue Details
            OutlinedTextField(
                value = issueDetails,
                onValueChange = { issueDetails = it },
                label = { Text("Detalles adicionales (opcional)") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 2
            )

            // Service Type Dropdown
            var expandedServiceType by remember { mutableStateOf(false) }
            ExposedDropdownMenuBox(
                expanded = expandedServiceType,
                onExpandedChange = { expandedServiceType = it }
            ) {
                OutlinedTextField(
                    value = serviceType,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Tipo de Servicio") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedServiceType) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor()
                )
                ExposedDropdownMenu(
                    expanded = expandedServiceType,
                    onDismissRequest = { expandedServiceType = false }
                ) {
                    listOf("DIAGNOSTIC", "PREVENTIVE", "REPAIR", "INSTALLATION", "REMOVAL").forEach { type ->
                        DropdownMenuItem(
                            text = { Text(type) },
                            onClick = {
                                serviceType = type
                                expandedServiceType = false
                            }
                        )
                    }
                }
            }

            // Priority Dropdown
            var expandedPriority by remember { mutableStateOf(false) }
            ExposedDropdownMenuBox(
                expanded = expandedPriority,
                onExpandedChange = { expandedPriority = it }
            ) {
                OutlinedTextField(
                    value = priority,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Prioridad") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedPriority) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor()
                )
                ExposedDropdownMenu(
                    expanded = expandedPriority,
                    onDismissRequest = { expandedPriority = false }
                ) {
                    listOf("LOW", "MEDIUM", "HIGH", "CRITICAL").forEach { p ->
                        DropdownMenuItem(
                            text = { Text(p) },
                            onClick = {
                                priority = p
                                expandedPriority = false
                            }
                        )
                    }
                }
            }

            // Emergency Switch
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Servicio de emergencia", style = MaterialTheme.typography.bodyLarge)
                Switch(
                    checked = isEmergency,
                    onCheckedChange = {
                        isEmergency = it
                        if (it) {
                            priority = "CRITICAL"
                            urgency = "CRITICAL"
                        }
                    }
                )
            }

            // Address
            OutlinedTextField(
                value = address,
                onValueChange = { address = it },
                label = { Text("Dirección del servicio") },
                placeholder = { Text("Dirección donde se requiere el servicio") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 2
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Submit Button
            Button(
                onClick = {
                    if (title.isNotBlank() && description.isNotBlank() && address.isNotBlank()) {
                        viewModel.createServiceRequest(
                            title = title,
                            description = description,
                            issueDetails = issueDetails.ifBlank { null },
                            equipmentId = equipmentId,
                            reportedByUserId = userId,
                            serviceType = serviceType,
                            priority = priority,
                            urgency = urgency,
                            isEmergency = isEmergency,
                            scheduledDate = null,
                            timeSlot = null,
                            serviceAddress = address
                        )
                    } else {
                        Toast.makeText(
                            context,
                            "Por favor completa todos los campos requeridos",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = !uiState.isCreating
            ) {
                if (uiState.isCreating) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                }
                Text("Crear Solicitud")
            }
        }
    }
}
