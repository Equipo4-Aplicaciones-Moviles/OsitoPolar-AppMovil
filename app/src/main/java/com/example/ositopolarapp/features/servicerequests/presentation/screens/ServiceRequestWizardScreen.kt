package com.example.ositopolarapp.features.servicerequests.presentation.screens

import android.widget.Toast
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.ositopolarapp.features.equipment.domain.model.Equipment
import com.example.ositopolarapp.features.servicerequests.presentation.state.ServiceRequestViewModel

/**
 * Service Request Wizard Screen - 3 Steps
 *
 * Step 1: Equipment Selection & Service Details
 * Step 2: Scheduling & Priority
 * Step 3: Confirmation & Submit
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ServiceRequestWizardScreen(
    viewModel: ServiceRequestViewModel,
    equipmentList: List<Equipment>,
    userId: Int,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    var currentStep by remember { mutableStateOf(1) }
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()

    // Step 1 data
    var selectedEquipment by remember { mutableStateOf<Equipment?>(null) }
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var issueDetails by remember { mutableStateOf("") }
    var serviceType by remember { mutableStateOf("REPAIR") }

    // Step 2 data
    var priority by remember { mutableStateOf("MEDIUM") }
    var urgency by remember { mutableStateOf("NORMAL") }
    var isEmergency by remember { mutableStateOf(false) }
    var scheduledDate by remember { mutableStateOf("") }
    var timeSlot by remember { mutableStateOf("") }

    // Step 3 data
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
                    Column {
                        Text(
                            text = "Nueva Solicitud de Servicio",
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Paso $currentStep de 3",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = {
                        if (currentStep > 1) {
                            currentStep--
                        } else {
                            onNavigateBack()
                        }
                    }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Atrás"
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
        ) {
            // Progress Indicator
            LinearProgressIndicator(
                progress = currentStep / 3f,
                modifier = Modifier.fillMaxWidth()
            )

            // Step Content
            AnimatedContent(
                targetState = currentStep,
                label = "step_transition"
            ) { step ->
                when (step) {
                    1 -> Step1Equipment(
                        equipmentList = equipmentList,
                        selectedEquipment = selectedEquipment,
                        onEquipmentSelected = { selectedEquipment = it },
                        title = title,
                        onTitleChange = { title = it },
                        description = description,
                        onDescriptionChange = { description = it },
                        issueDetails = issueDetails,
                        onIssueDetailsChange = { issueDetails = it },
                        serviceType = serviceType,
                        onServiceTypeChange = { serviceType = it }
                    )

                    2 -> Step2Scheduling(
                        priority = priority,
                        onPriorityChange = { priority = it },
                        urgency = urgency,
                        onUrgencyChange = { urgency = it },
                        isEmergency = isEmergency,
                        onEmergencyChange = {
                            isEmergency = it
                            if (it) {
                                priority = "CRITICAL"
                                urgency = "CRITICAL"
                            }
                        },
                        scheduledDate = scheduledDate,
                        onScheduledDateChange = { scheduledDate = it },
                        timeSlot = timeSlot,
                        onTimeSlotChange = { timeSlot = it }
                    )

                    3 -> Step3Confirmation(
                        selectedEquipment = selectedEquipment,
                        title = title,
                        description = description,
                        serviceType = serviceType,
                        priority = priority,
                        isEmergency = isEmergency,
                        address = address,
                        onAddressChange = { address = it }
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            // Navigation Buttons
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                if (currentStep > 1) {
                    OutlinedButton(
                        onClick = { currentStep-- },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Anterior")
                    }
                }

                Button(
                    onClick = {
                        when (currentStep) {
                            1 -> {
                                if (selectedEquipment != null && title.isNotBlank() && description.isNotBlank()) {
                                    currentStep = 2
                                } else {
                                    Toast.makeText(
                                        context,
                                        "Por favor completa todos los campos",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }

                            2 -> {
                                currentStep = 3
                            }

                            3 -> {
                                if (address.isNotBlank()) {
                                    viewModel.createServiceRequest(
                                        title = title,
                                        description = description,
                                        issueDetails = issueDetails.ifBlank { null },
                                        equipmentId = selectedEquipment!!.id,
                                        reportedByUserId = userId,
                                        serviceType = serviceType,
                                        priority = priority,
                                        urgency = urgency,
                                        isEmergency = isEmergency,
                                        scheduledDate = scheduledDate.ifBlank { null },
                                        timeSlot = timeSlot.ifBlank { null },
                                        serviceAddress = address
                                    )
                                } else {
                                    Toast.makeText(
                                        context,
                                        "Por favor ingresa la dirección del servicio",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                        }
                    },
                    modifier = Modifier.weight(1f),
                    enabled = !uiState.isCreating
                ) {
                    if (uiState.isCreating) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    } else {
                        Icon(
                            imageVector = if (currentStep == 3) Icons.Default.Check else Icons.Default.ArrowForward,
                            contentDescription = null,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(if (currentStep == 3) "Crear Solicitud" else "Siguiente")
                }
            }
        }
    }
}

@Composable
private fun Step1Equipment(
    equipmentList: List<Equipment>,
    selectedEquipment: Equipment?,
    onEquipmentSelected: (Equipment) -> Unit,
    title: String,
    onTitleChange: (String) -> Unit,
    description: String,
    onDescriptionChange: (String) -> Unit,
    issueDetails: String,
    onIssueDetailsChange: (String) -> Unit,
    serviceType: String,
    onServiceTypeChange: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Paso 1: Equipo y Detalles",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )

        // Equipment Selection
        var expandedEquipment by remember { mutableStateOf(false) }
        ExposedDropdownMenuBox(
            expanded = expandedEquipment,
            onExpandedChange = { expandedEquipment = it }
        ) {
            OutlinedTextField(
                value = selectedEquipment?.name ?: "",
                onValueChange = {},
                readOnly = true,
                label = { Text("Equipo *") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedEquipment) },
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor()
            )
            ExposedDropdownMenu(
                expanded = expandedEquipment,
                onDismissRequest = { expandedEquipment = false }
            ) {
                equipmentList.forEach { equipment ->
                    DropdownMenuItem(
                        text = {
                            Column {
                                Text(equipment.name, fontWeight = FontWeight.SemiBold)
                                Text(
                                    "${equipment.type} - ${equipment.manufacturer}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        },
                        onClick = {
                            onEquipmentSelected(equipment)
                            expandedEquipment = false
                        }
                    )
                }
            }
        }

        // Service Type
        var expandedServiceType by remember { mutableStateOf(false) }
        ExposedDropdownMenuBox(
            expanded = expandedServiceType,
            onExpandedChange = { expandedServiceType = it }
        ) {
            OutlinedTextField(
                value = serviceType,
                onValueChange = {},
                readOnly = true,
                label = { Text("Tipo de Servicio *") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedServiceType) },
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor()
            )
            ExposedDropdownMenu(
                expanded = expandedServiceType,
                onDismissRequest = { expandedServiceType = false }
            ) {
                listOf(
                    "DIAGNOSTIC" to "Diagnóstico",
                    "PREVENTIVE" to "Preventivo",
                    "REPAIR" to "Reparación",
                    "INSTALLATION" to "Instalación",
                    "REMOVAL" to "Remoción"
                ).forEach { (value, label) ->
                    DropdownMenuItem(
                        text = { Text(label) },
                        onClick = {
                            onServiceTypeChange(value)
                            expandedServiceType = false
                        }
                    )
                }
            }
        }

        // Title
        OutlinedTextField(
            value = title,
            onValueChange = onTitleChange,
            label = { Text("Título *") },
            placeholder = { Text("Ej: Falla en el compresor") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        // Description
        OutlinedTextField(
            value = description,
            onValueChange = onDescriptionChange,
            label = { Text("Descripción *") },
            placeholder = { Text("Describe el problema...") },
            modifier = Modifier.fillMaxWidth(),
            minLines = 3
        )

        // Issue Details
        OutlinedTextField(
            value = issueDetails,
            onValueChange = onIssueDetailsChange,
            label = { Text("Detalles adicionales") },
            placeholder = { Text("Información extra del problema (opcional)") },
            modifier = Modifier.fillMaxWidth(),
            minLines = 2
        )
    }
}

@Composable
private fun Step2Scheduling(
    priority: String,
    onPriorityChange: (String) -> Unit,
    urgency: String,
    onUrgencyChange: (String) -> Unit,
    isEmergency: Boolean,
    onEmergencyChange: (Boolean) -> Unit,
    scheduledDate: String,
    onScheduledDateChange: (String) -> Unit,
    timeSlot: String,
    onTimeSlotChange: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Paso 2: Programación",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )

        // Emergency Switch
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = if (isEmergency) {
                    MaterialTheme.colorScheme.errorContainer
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
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        "Servicio de emergencia",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        "Requiere atención inmediata",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Switch(
                    checked = isEmergency,
                    onCheckedChange = onEmergencyChange
                )
            }
        }

        // Priority
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
                    .menuAnchor(),
                enabled = !isEmergency
            )
            ExposedDropdownMenu(
                expanded = expandedPriority,
                onDismissRequest = { expandedPriority = false }
            ) {
                listOf("LOW", "MEDIUM", "HIGH", "CRITICAL").forEach { p ->
                    DropdownMenuItem(
                        text = { Text(p) },
                        onClick = {
                            onPriorityChange(p)
                            expandedPriority = false
                        }
                    )
                }
            }
        }

        // Scheduled Date (optional)
        OutlinedTextField(
            value = scheduledDate,
            onValueChange = onScheduledDateChange,
            label = { Text("Fecha preferida (opcional)") },
            placeholder = { Text("YYYY-MM-DD") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        // Time Slot (optional)
        var expandedTimeSlot by remember { mutableStateOf(false) }
        ExposedDropdownMenuBox(
            expanded = expandedTimeSlot,
            onExpandedChange = { expandedTimeSlot = it }
        ) {
            OutlinedTextField(
                value = timeSlot,
                onValueChange = {},
                readOnly = true,
                label = { Text("Horario preferido (opcional)") },
                placeholder = { Text("Seleccionar horario") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedTimeSlot) },
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor()
            )
            ExposedDropdownMenu(
                expanded = expandedTimeSlot,
                onDismissRequest = { expandedTimeSlot = false }
            ) {
                listOf("Mañana (8AM-12PM)", "Tarde (12PM-5PM)", "Noche (5PM-8PM)").forEach { slot ->
                    DropdownMenuItem(
                        text = { Text(slot) },
                        onClick = {
                            onTimeSlotChange(slot)
                            expandedTimeSlot = false
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun Step3Confirmation(
    selectedEquipment: Equipment?,
    title: String,
    description: String,
    serviceType: String,
    priority: String,
    isEmergency: Boolean,
    address: String,
    onAddressChange: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Paso 3: Confirmación",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )

        // Summary Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Resumen",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                SummaryRow("Equipo:", selectedEquipment?.name ?: "")
                SummaryRow("Tipo:", serviceType)
                SummaryRow("Título:", title)
                SummaryRow("Prioridad:", priority)

                if (isEmergency) {
                    Surface(
                        color = MaterialTheme.colorScheme.error,
                        shape = MaterialTheme.shapes.small,
                        modifier = Modifier.padding(top = 8.dp)
                    ) {
                        Text(
                            text = "🚨 SERVICIO DE EMERGENCIA",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onError,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                        )
                    }
                }
            }
        }

        // Service Address
        OutlinedTextField(
            value = address,
            onValueChange = onAddressChange,
            label = { Text("Dirección del servicio *") },
            placeholder = { Text("Dirección donde se requiere el servicio") },
            modifier = Modifier.fillMaxWidth(),
            minLines = 2
        )

        // Terms
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "ℹ️ Nota Importante",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Un técnico se pondrá en contacto contigo para confirmar la fecha y hora del servicio.",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

@Composable
private fun SummaryRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onPrimaryContainer
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
        )
    }
}
