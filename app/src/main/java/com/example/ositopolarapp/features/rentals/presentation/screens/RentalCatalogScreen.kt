package com.example.ositopolarapp.features.rentals.presentation.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.ositopolarapp.features.rentals.domain.model.RentalEquipment
import com.example.ositopolarapp.features.rentals.domain.model.RentalStatus

/**
 * Rental Catalog Screen
 *
 * Browse equipment available for rent from providers.
 * NOTE: Using mock data as backend endpoints not yet implemented.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RentalCatalogScreen(
    onRentEquipment: (RentalEquipment) -> Unit,
    modifier: Modifier = Modifier
) {
    // Mock data (replace with real API call when backend is ready)
    val rentalEquipment = remember {
        listOf(
            RentalEquipment(
                id = 1,
                name = "Refrigerador Industrial R-1000",
                type = "Refrigerator",
                manufacturer = "ColdTech",
                monthlyPrice = 500.0,
                description = "Refrigerador de alta capacidad para almacenamiento industrial",
                providerId = 1,
                providerName = "Samsung Provider",
                status = RentalStatus.AVAILABLE
            ),
            RentalEquipment(
                id = 2,
                name = "Congelador Vertical F-200",
                type = "Freezer",
                manufacturer = "FreezeMaster",
                monthlyPrice = 650.0,
                description = "Congelador vertical con control de temperatura preciso",
                providerId = 2,
                providerName = "LG Provider",
                status = RentalStatus.AVAILABLE
            ),
            RentalEquipment(
                id = 3,
                name = "Cámara Fría CR-5000",
                type = "ColdRoom",
                manufacturer = "IndustrialCool",
                monthlyPrice = 1200.0,
                description = "Cámara fría de gran capacidad para almacenamiento masivo",
                providerId = 1,
                providerName = "Samsung Provider",
                status = RentalStatus.AVAILABLE
            ),
            RentalEquipment(
                id = 4,
                name = "Refrigerador Compacto R-300",
                type = "Refrigerator",
                manufacturer = "ColdTech",
                monthlyPrice = 350.0,
                description = "Refrigerador compacto ideal para espacios reducidos",
                providerId = 3,
                providerName = "Whirlpool Provider",
                status = RentalStatus.RENTED
            )
        )
    }

    val availableEquipment = rentalEquipment.filter { it.status == RentalStatus.AVAILABLE }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "Rentar Equipos",
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "${availableEquipment.size} disponibles",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { /* TODO: Show filter dialog */ }) {
                        Icon(
                            imageVector = Icons.Default.FilterList,
                            contentDescription = "Filtros"
                        )
                    }
                }
            )
        },
        modifier = modifier
    ) { paddingValues ->
        if (availableEquipment.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "🧊",
                        style = MaterialTheme.typography.displayMedium
                    )
                    Text(
                        text = "No hay equipos disponibles para rentar",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Adaptive(minSize = 280.dp),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentPadding = PaddingValues(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(
                    items = availableEquipment,
                    key = { it.id }
                ) { equipment ->
                    RentalEquipmentCard(
                        equipment = equipment,
                        onRentClick = { onRentEquipment(equipment) }
                    )
                }
            }
        }
    }
}

@Composable
private fun RentalEquipmentCard(
    equipment: RentalEquipment,
    onRentClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Type badge
            Surface(
                color = MaterialTheme.colorScheme.primaryContainer,
                shape = MaterialTheme.shapes.small
            ) {
                Text(
                    text = equipment.type,
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }

            Text(
                text = equipment.name,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = equipment.description,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 2
            )

            Divider()

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Proveedor",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = equipment.providerName,
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            Divider()

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Precio mensual",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = equipment.getFormattedPrice(),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                Button(onClick = onRentClick) {
                    Text("Rentar")
                }
            }
        }
    }
}
