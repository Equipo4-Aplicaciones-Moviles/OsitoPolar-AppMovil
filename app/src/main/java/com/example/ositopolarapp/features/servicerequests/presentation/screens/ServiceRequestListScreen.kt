package com.example.ositopolarapp.features.servicerequests.presentation.screens

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.ositopolarapp.features.servicerequests.domain.model.ServiceRequest
import com.example.ositopolarapp.features.servicerequests.presentation.components.ServiceRequestCard
import com.example.ositopolarapp.features.servicerequests.presentation.components.RatingDialog
import com.example.ositopolarapp.features.servicerequests.presentation.state.ServiceRequestViewModel

/**
 * Service Request List Screen
 *
 * Displays all service requests for the current user.
 * Features:
 * - View list of service requests
 * - Rate completed services
 * - Create new service request
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ServiceRequestListScreen(
    viewModel: ServiceRequestViewModel,
    onNavigateToCreate: () -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    var showRatingDialog by remember { mutableStateOf(false) }
    var selectedRequest by remember { mutableStateOf<ServiceRequest?>(null) }

    // Show error toast
    LaunchedEffect(uiState.error) {
        uiState.error?.let {
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
            viewModel.clearError()
        }
    }

    // Rating dialog
    if (showRatingDialog && selectedRequest != null) {
        RatingDialog(
            serviceRequest = selectedRequest!!,
            onDismiss = {
                showRatingDialog = false
                selectedRequest = null
            },
            onRatingSubmit = { rating ->
                viewModel.addFeedback(selectedRequest!!.id, rating)
                showRatingDialog = false
                selectedRequest = null
                Toast.makeText(context, "Calificación enviada", Toast.LENGTH_SHORT).show()
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Solicitudes de Servicio",
                        fontWeight = FontWeight.Bold
                    )
                },
                actions = {
                    IconButton(onClick = { viewModel.loadServiceRequests() }) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "Recargar"
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onNavigateToCreate,
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Nueva solicitud"
                )
            }
        },
        modifier = modifier
    ) { paddingValues ->
        when {
            uiState.isLoading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            uiState.serviceRequests.isEmpty() -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Text(
                            text = "No tienes solicitudes de servicio",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Button(onClick = onNavigateToCreate) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = null,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Crear primera solicitud")
                        }
                    }
                }
            }

            else -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(
                        items = uiState.serviceRequests,
                        key = { it.id }
                    ) { serviceRequest ->
                        ServiceRequestCard(
                            serviceRequest = serviceRequest,
                            onRateClick = {
                                selectedRequest = serviceRequest
                                showRatingDialog = true
                            }
                        )
                    }
                }
            }
        }
    }
}
