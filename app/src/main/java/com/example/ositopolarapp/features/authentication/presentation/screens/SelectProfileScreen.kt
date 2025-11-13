package com.example.ositopolarapp.features.authentication.presentation.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.ositopolarapp.ui.theme.OsitoPolarAppTheme
import androidx.compose.foundation.shape.RoundedCornerShape

@Composable
fun SelectProfileScreen(
    onClientClicked: () -> Unit,  // Acción para navegar a Login Cliente
    onProviderClicked: () -> Unit // Acción para navegar a Login Empresa
) {
    // Surface usará automáticamente el color 'background' (F5F7FA)
    Surface(modifier = Modifier.fillMaxSize()) {
        // Usamos un Box para alinear la tarjeta al centro y el footer al fondo
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp)
                .padding(vertical = 64.dp)// Padding general de la pantalla
                .background(color = MaterialTheme.colorScheme.surfaceVariant)
        ) {



            // --- 1. El Rectángulo Central (La Tarjeta) ---
            Card(
                modifier = Modifier
                    .align(Alignment.Center) // Centramos la tarjeta
                    .fillMaxWidth()
                    // Damos un padding horizontal para que no toque los bordes
                    .padding(16.dp),

                colors = CardDefaults.cardColors(
                    // Usamos el color 'surface' (F2 EBEFF5)
                    containerColor = MaterialTheme.colorScheme.surface,
                    contentColor = MaterialTheme.colorScheme.surface
                ),
                // Añadimos el borde (F1 CFD8E8)
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.surfaceVariant)
            ) {
                // Columna interna para el contenido de la tarjeta
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        // Padding interno de la tarjeta
                        .padding(horizontal = 32.dp, vertical = 48.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {

                    // Texto del Logo
                    Text(
                        text = "OsitoPolar",
                        style = MaterialTheme.typography.headlineLarge,
                        // Usamos el color 'secondary' (208AC9) para el título
                        color = MaterialTheme.colorScheme.secondary,
                        modifier = Modifier.padding(bottom = 64.dp)
                    )

                    // Botón de Cliente
                    Button(
                        onClick = onClientClicked,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(text = "Cliente")
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Botón de Empresa
                    Button(
                        onClick = onProviderClicked,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(text = "Empresa")
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SelectProfilePreview() {
    OsitoPolarAppTheme {
        SelectProfileScreen(
            onClientClicked =  {},
            onProviderClicked = {}
        )
    }
}

