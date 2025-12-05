package com.example.ositopolarapp.features.servicerequests.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.ositopolarapp.features.servicerequests.domain.model.ServiceRequest
import com.example.ositopolarapp.features.servicerequests.domain.model.ServiceRequestStatus

/**
 * Service Request Card Component
 *
 * Displays service request information in a card format.
 */
@Composable
fun ServiceRequestCard(
    serviceRequest: ServiceRequest,
    onRateClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Header: Order Number + Status Badge
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "#${serviceRequest.orderNumber}",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )

                ServiceRequestStatusBadge(status = serviceRequest.status)
            }

            // Title
            Text(
                text = serviceRequest.title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )

            // Description
            Text(
                text = serviceRequest.description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 2
            )

            // Service Type and Priority
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                AssistChip(
                    onClick = { },
                    label = { Text(serviceRequest.serviceType.name) },
                    enabled = false
                )
                AssistChip(
                    onClick = { },
                    label = { Text("${serviceRequest.priority.name} Priority") },
                    enabled = false
                )
            }

            // Emergency badge if applicable
            if (serviceRequest.isEmergency) {
                Surface(
                    color = MaterialTheme.colorScheme.errorContainer,
                    shape = MaterialTheme.shapes.small
                ) {
                    Text(
                        text = "🚨 EMERGENCY",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onErrorContainer,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            // Scheduled Date
            if (serviceRequest.scheduledDate != null) {
                Text(
                    text = "Programado: ${serviceRequest.scheduledDate} ${serviceRequest.timeSlot ?: ""}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // Rating display or button
            if (serviceRequest.rating != null) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = "Rating",
                        tint = Color(0xFFFFD700),
                        modifier = Modifier.size(20.dp)
                    )
                    Text(
                        text = "Calificación: ${serviceRequest.rating}/5",
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            } else if (serviceRequest.canBeRated()) {
                Button(
                    onClick = onRateClick,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Calificar Servicio")
                }
            }
        }
    }
}

@Composable
private fun ServiceRequestStatusBadge(status: ServiceRequestStatus) {
    val (backgroundColor, textColor) = when (status) {
        ServiceRequestStatus.PENDING -> Color(0xFFFFF3E0) to Color(0xFFE65100)
        ServiceRequestStatus.ACCEPTED -> Color(0xFFE3F2FD) to Color(0xFF1565C0)
        ServiceRequestStatus.IN_PROGRESS -> Color(0xFFE1F5FE) to Color(0xFF0277BD)
        ServiceRequestStatus.RESOLVED -> Color(0xFFE8F5E9) to Color(0xFF2E7D32)
        ServiceRequestStatus.REJECTED -> Color(0xFFFFEBEE) to Color(0xFFC62828)
        ServiceRequestStatus.CANCELLED -> Color(0xFFF5F5F5) to Color(0xFF616161)
    }

    Surface(
        color = backgroundColor,
        shape = MaterialTheme.shapes.small
    ) {
        Text(
            text = status.name,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold,
            color = textColor,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
        )
    }
}
