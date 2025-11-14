package com.example.ositopolarapp.features.servicerequests.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.ositopolarapp.features.servicerequests.domain.model.ServiceRequest

/**
 * Rating Dialog Component
 *
 * Dialog for rating completed service requests.
 * Displays 1-5 star rating system.
 */
@Composable
fun RatingDialog(
    serviceRequest: ServiceRequest,
    onDismiss: () -> Unit,
    onRatingSubmit: (Int) -> Unit
) {
    var selectedRating by remember { mutableStateOf(0) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Calificar Servicio",
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "¿Cómo calificarías el servicio?",
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center
                )

                Text(
                    text = serviceRequest.title,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.primary
                )

                // Star Rating
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    (1..5).forEach { star ->
                        IconButton(
                            onClick = { selectedRating = star }
                        ) {
                            Icon(
                                imageVector = if (star <= selectedRating) {
                                    Icons.Filled.Star
                                } else {
                                    Icons.Outlined.StarOutline
                                },
                                contentDescription = "$star stars",
                                tint = if (star <= selectedRating) {
                                    Color(0xFFFFD700) // Gold color
                                } else {
                                    MaterialTheme.colorScheme.outline
                                },
                                modifier = Modifier.size(40.dp)
                            )
                        }
                    }
                }

                if (selectedRating > 0) {
                    Text(
                        text = getRatingText(selectedRating),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = { onRatingSubmit(selectedRating) },
                enabled = selectedRating > 0
            ) {
                Text("Enviar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}

private fun getRatingText(rating: Int): String {
    return when (rating) {
        1 -> "Muy insatisfecho"
        2 -> "Insatisfecho"
        3 -> "Neutral"
        4 -> "Satisfecho"
        5 -> "Muy satisfecho"
        else -> ""
    }
}
