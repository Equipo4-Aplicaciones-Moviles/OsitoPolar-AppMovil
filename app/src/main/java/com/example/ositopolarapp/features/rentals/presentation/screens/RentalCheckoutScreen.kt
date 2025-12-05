package com.example.ositopolarapp.features.rentals.presentation.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.ositopolarapp.features.rentals.domain.model.RentalEquipment

/**
 * Rental Checkout Screen
 *
 * Allows user to configure rental details and complete payment via Stripe.
 * Flow:
 * 1. Configure rental (quantity, duration, address)
 * 2. Review pricing summary
 * 3. Payment (Stripe integration)
 * 4. Confirmation
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RentalCheckoutScreen(
    equipment: RentalEquipment,
    onNavigateBack: () -> Unit,
    onCheckoutSuccess: () -> Unit,
    onCreateRentalRequest: ((equipmentId: Int, months: Int) -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    var quantity by remember { mutableStateOf(1) }
    var rentalMonths by remember { mutableStateOf(1) }
    var deliveryAddress by remember { mutableStateOf("") }
    var city by remember { mutableStateOf("") }
    var postalCode by remember { mutableStateOf("") }
    var preferredStartDate by remember { mutableStateOf("") }

    var showPaymentDialog by remember { mutableStateOf(false) }

    // Calculate costs
    val monthlySubtotal = equipment.monthlyFee * quantity
    val totalCost = monthlySubtotal * rentalMonths
    val deposit = totalCost * 0.2 // 20% deposit
    val setupFee = 50.0
    val totalDue = deposit + setupFee

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Configurar Renta",
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
            // Equipment Card
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
                        text = equipment.name,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "${equipment.type} - ${equipment.manufacturer}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                    )
                    Text(
                        text = "Proveedor: ${equipment.providerName}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
                    )
                }
            }

            // Rental Configuration Section
            Text(
                text = "Configuración de Renta",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )

            // Quantity
            Card(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Cantidad",
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        FilledTonalButton(
                            onClick = { if (quantity > 1) quantity-- },
                            enabled = quantity > 1
                        ) {
                            Text("-")
                        }
                        Text(
                            text = quantity.toString(),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        FilledTonalButton(
                            onClick = { if (quantity < 10) quantity++ }
                        ) {
                            Text("+")
                        }
                    }
                }
            }

            // Rental Duration
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Duración",
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            FilledTonalButton(
                                onClick = { if (rentalMonths > 1) rentalMonths-- },
                                enabled = rentalMonths > 1
                            ) {
                                Text("-")
                            }
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = rentalMonths.toString(),
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = if (rentalMonths == 1) "mes" else "meses",
                                    style = MaterialTheme.typography.labelSmall
                                )
                            }
                            FilledTonalButton(
                                onClick = { if (rentalMonths < 24) rentalMonths++ }
                            ) {
                                Text("+")
                            }
                        }
                    }

                    // Quick select buttons
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        listOf(3, 6, 12).forEach { months ->
                            FilterChip(
                                selected = rentalMonths == months,
                                onClick = { rentalMonths = months },
                                label = { Text("$months meses") },
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
            }

            // Delivery Information
            Text(
                text = "Información de Entrega",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )

            OutlinedTextField(
                value = deliveryAddress,
                onValueChange = { deliveryAddress = it },
                label = { Text("Dirección de entrega *") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = null
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                minLines = 2
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedTextField(
                    value = city,
                    onValueChange = { city = it },
                    label = { Text("Ciudad *") },
                    modifier = Modifier.weight(1f),
                    singleLine = true
                )
                OutlinedTextField(
                    value = postalCode,
                    onValueChange = { postalCode = it },
                    label = { Text("C.P. *") },
                    modifier = Modifier.weight(1f),
                    singleLine = true
                )
            }

            OutlinedTextField(
                value = preferredStartDate,
                onValueChange = { preferredStartDate = it },
                label = { Text("Fecha de inicio preferida") },
                placeholder = { Text("YYYY-MM-DD") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.CalendarMonth,
                        contentDescription = null
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Divider()

            // Pricing Summary
            Text(
                text = "Resumen de Costos",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )

            Card(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    PricingRow(
                        "Renta mensual ($${equipment.monthlyFee} × $quantity)",
                        monthlySubtotal
                    )
                    PricingRow("Duración", "$rentalMonths meses", showPrice = false)
                    PricingRow("Subtotal", monthlySubtotal * rentalMonths)

                    Divider()

                    PricingRow("Depósito (20%)", deposit)
                    PricingRow("Tarifa de instalación", setupFee)

                    Divider()

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Total a pagar hoy",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "$${String.format("%.2f", totalDue)}",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }

                    Text(
                        text = "Pagos mensuales: $${String.format("%.2f", monthlySubtotal)}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Checkout Button
            Button(
                onClick = {
                    if (onCreateRentalRequest != null) {
                        // Call backend to create rental request and get Stripe checkout URL
                        onCreateRentalRequest(equipment.id, rentalMonths)
                    } else {
                        // Fallback to mock dialog if no callback provided
                        showPaymentDialog = true
                    }
                },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                enabled = deliveryAddress.isNotBlank() && city.isNotBlank() && postalCode.isNotBlank()
            ) {
                Icon(
                    imageVector = Icons.Default.CreditCard,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Proceder al Pago", style = MaterialTheme.typography.titleMedium)
            }

            // Terms
            Text(
                text = "• El depósito será reembolsado al finalizar la renta\n" +
                       "• Los pagos mensuales se cobrarán automáticamente\n" +
                       "• Cancelación con 30 días de anticipación",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }

    // Payment Dialog (Stripe Integration Placeholder)
    if (showPaymentDialog) {
        AlertDialog(
            onDismissRequest = { showPaymentDialog = false },
            title = { Text("Pago con Stripe") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Total: $${String.format("%.2f", totalDue)}")
                    Text(
                        "Integración con Stripe aquí",
                        style = MaterialTheme.typography.bodySmall
                    )
                    Text(
                        "En producción, se abrirá el checkout de Stripe o WebView",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            },
            confirmButton = {
                Button(onClick = {
                    showPaymentDialog = false
                    onCheckoutSuccess()
                }) {
                    Text("Simular Pago Exitoso")
                }
            },
            dismissButton = {
                TextButton(onClick = { showPaymentDialog = false }) {
                    Text("Cancelar")
                }
            }
        )
    }
}

@Composable
private fun PricingRow(label: String, amount: Double, showPrice: Boolean = true) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium
        )
        if (showPrice) {
            Text(
                text = "$${String.format("%.2f", amount)}",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
private fun PricingRow(label: String, value: String, showPrice: Boolean = false) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
