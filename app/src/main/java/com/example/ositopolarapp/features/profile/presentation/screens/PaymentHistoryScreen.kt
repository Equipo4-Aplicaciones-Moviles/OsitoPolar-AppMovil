package com.example.ositopolarapp.features.profile.presentation.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/**
 * Payment History Screen
 *
 * Displays payment history for users (subscriptions, rentals, services).
 * NOTE: Using mock data as backend endpoints not yet implemented.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaymentHistoryScreen(
    userType: String, // "Owner" or "Provider"
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Mock payment data
    val payments = remember {
        listOf(
            PaymentRecord(
                id = 1,
                type = "Subscription",
                description = "Plan Básico - Enero 2025",
                amount = 29.99,
                date = "2025-01-01",
                status = "Completado",
                method = "Visa ****1234"
            ),
            PaymentRecord(
                id = 2,
                type = "Rental",
                description = "Renta Refrigerador R-500",
                amount = 500.00,
                date = "2025-01-05",
                status = "Completado",
                method = "Mastercard ****5678"
            ),
            PaymentRecord(
                id = 3,
                type = "Service",
                description = "Mantenimiento preventivo",
                amount = 150.00,
                date = "2025-01-10",
                status = "Completado",
                method = "Visa ****1234"
            ),
            PaymentRecord(
                id = 4,
                type = "Subscription",
                description = "Plan Básico - Febrero 2025",
                amount = 29.99,
                date = "2025-02-01",
                status = "Pendiente",
                method = "Visa ****1234"
            )
        )
    }

    val totalPaid = payments.filter { it.status == "Completado" }.sumOf { it.amount }
    val pendingPayments = payments.count { it.status == "Pendiente" }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Historial de Pagos",
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
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Summary Cards
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    SummaryCard(
                        title = "Total Pagado",
                        value = "$${String.format("%.2f", totalPaid)}",
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.weight(1f)
                    )

                    SummaryCard(
                        title = "Pendientes",
                        value = pendingPayments.toString(),
                        color = MaterialTheme.colorScheme.secondary,
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            // Payment List
            item {
                Text(
                    text = "Transacciones",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
            }

            items(
                items = payments,
                key = { it.id }
            ) { payment ->
                PaymentCard(payment = payment)
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
private fun SummaryCard(
    title: String,
    value: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = color.copy(alpha = 0.1f)
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = value,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = color
            )
        }
    }
}

@Composable
private fun PaymentCard(payment: PaymentRecord) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Icon
            Surface(
                shape = MaterialTheme.shapes.medium,
                color = getPaymentTypeColor(payment.type).copy(alpha = 0.2f),
                modifier = Modifier.size(48.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Default.Receipt,
                        contentDescription = null,
                        tint = getPaymentTypeColor(payment.type)
                    )
                }
            }

            // Details
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = payment.type,
                        style = MaterialTheme.typography.labelSmall,
                        color = getPaymentTypeColor(payment.type),
                        fontWeight = FontWeight.Bold
                    )
                    PaymentStatusBadge(status = payment.status)
                }

                Text(
                    text = payment.description,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold
                )

                Text(
                    text = payment.date,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Text(
                    text = payment.method,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Divider(modifier = Modifier.padding(vertical = 8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "$${String.format("%.2f", payment.amount)}",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )

                    if (payment.status == "Completado") {
                        IconButton(onClick = { /* TODO: Download receipt */ }) {
                            Icon(
                                imageVector = Icons.Default.Download,
                                contentDescription = "Descargar recibo",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun PaymentStatusBadge(status: String) {
    val (backgroundColor, textColor) = when (status) {
        "Completado" -> Color(0xFFE8F5E9) to Color(0xFF2E7D32)
        "Pendiente" -> Color(0xFFFFF3E0) to Color(0xFFE65100)
        "Fallido" -> Color(0xFFFFEBEE) to Color(0xFFC62828)
        else -> Color(0xFFF5F5F5) to Color(0xFF616161)
    }

    Surface(
        color = backgroundColor,
        shape = MaterialTheme.shapes.small
    ) {
        Text(
            text = status,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold,
            color = textColor,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
        )
    }
}

private fun getPaymentTypeColor(type: String): Color {
    return when (type) {
        "Subscription" -> Color(0xFF2196F3)
        "Rental" -> Color(0xFF9C27B0)
        "Service" -> Color(0xFFFF9800)
        else -> Color(0xFF9E9E9E)
    }
}

data class PaymentRecord(
    val id: Int,
    val type: String,
    val description: String,
    val amount: Double,
    val date: String,
    val status: String,
    val method: String
)
