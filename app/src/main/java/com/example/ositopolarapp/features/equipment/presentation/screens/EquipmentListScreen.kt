package com.example.ositopolarapp.features.equipment.presentation.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ositopolarapp.features.equipment.presentation.components.EquipmentCard
import com.example.ositopolarapp.features.equipment.presentation.state.EquipmentListViewModel

@Composable
fun EquipmentListScreen(
    viewModel: EquipmentListViewModel,
    onNavigateToDetail: (Int) -> Unit,
    onNavigateToAdd: () -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = onNavigateToAdd) {
                Icon(Icons.Default.Add, contentDescription = "Agregar Equipo")
            }
        }
    ) { padding ->
        Box(
            modifier = modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            if (uiState.isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else if (uiState.equipmentList.isEmpty()) {
                // Mensaje de lista vacía
                Column(
                    modifier = Modifier.align(Alignment.Center),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("No tienes equipos registrados")
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(onClick = onNavigateToAdd) {
                        Text("Agregar primer equipo")
                    }
                }
            } else {
                // Lista de equipos
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp)
                ) {
                    // Barra de búsqueda simulada (opcional)
                    item {
                        OutlinedTextField(
                            value = "",
                            onValueChange = {},
                            placeholder = { Text("Buscar por nombre, marca o modelo") },
                            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                            enabled = false // Deshabilitado por ahora
                        )
                    }

                    items(uiState.equipmentList) { equipment ->
                        EquipmentCard(
                            equipment = equipment,
                            onClick = { onNavigateToDetail(equipment.id) }
                        )
                    }
                }
            }

            // Mostrar errores si existen
            if (uiState.error != null) {
                Text(
                    text = uiState.error!!,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.align(Alignment.BottomCenter).padding(16.dp)
                )
            }
        }
    }
}