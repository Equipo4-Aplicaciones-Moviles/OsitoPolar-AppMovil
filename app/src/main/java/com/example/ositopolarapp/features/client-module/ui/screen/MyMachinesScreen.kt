package com.example.ositopolarapp.features.`client-module`.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.BaselineShift
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.ositopolarapp.ui.theme.OsitoPolarAccentBlue
import com.example.ositopolarapp.ui.theme.OsitoPolarGreenButton
import com.example.ositopolarapp.ui.theme.OsitoPolarRedButton
import com.example.ositopolarapp.features.`client-module`.ui.composables.FooterContent

@Composable
fun MyMachinesScreen(paddingValues: PaddingValues, navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .background(Color.White)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // ======= TÍTULO PRINCIPAL =======
        Text(
            text = "Mis equipos",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = OsitoPolarAccentBlue,
            modifier = Modifier.padding(bottom = 24.dp),
            textAlign = TextAlign.Center
        )

        // ======= TARJETA DEL EQUIPO =======
        MachineCardContent(navController = navController)

        Spacer(modifier = Modifier.height(32.dp))

        // ======= TÍTULO MANTENIMIENTOS =======
        Text(
            text = "Mantenimientos",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = OsitoPolarAccentBlue,
            modifier = Modifier.padding(bottom = 16.dp),
            textAlign = TextAlign.Center
        )

        MaintenanceCardContent()

        Spacer(modifier = Modifier.height(32.dp))

        FooterContent()
    }
}

// ===============================================================
//              TARJETA DE EQUIPO
// ===============================================================
@Composable
private fun MachineCardContent(navController: NavController) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(4.dp, RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier
                .padding(20.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // ---- Espacio para imagen (placeholder) ----
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .background(Color.Gray.copy(alpha = 0.2f), RoundedCornerShape(12.dp))
            )

            Spacer(modifier = Modifier.height(16.dp))

            // ---- Nombre y descripción del producto ----
            Text(
                text = "Industrial Freezer XK-400",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = Color.Black
            )
            Text(
                text = "Congelador XK-400",
                color = Color.Gray,
                fontSize = 14.sp,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            // ---- Temperatura ----
            Row(
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "25",
                    style = TextStyle(
                        fontSize = 48.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                )
                Text(
                    text = "°C",
                    style = TextStyle(
                        fontSize = 22.sp,
                        color = Color.Gray,
                        baselineShift = BaselineShift.Superscript
                    ),
                    modifier = Modifier.padding(bottom = 6.dp)
                )
            }

            Text(
                text = "(normal)",
                color = Color.Gray,
                fontSize = 14.sp,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // ---- Ubicación ----
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Default.LocationOn,
                    contentDescription = "Ubicación",
                    tint = Color(0xFFE57373),
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text("San Isidro Main Warehouse", color = Color.Gray, fontSize = 12.sp)
            }

            Spacer(modifier = Modifier.height(20.dp))

            // ---- Botones ----
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Botón Control
                Button(
                    onClick = { navController.navigate("machine_control") },
                    shape = RoundedCornerShape(50),
                    colors = ButtonDefaults.buttonColors(containerColor = OsitoPolarAccentBlue),
                    contentPadding = PaddingValues(horizontal = 20.dp, vertical = 10.dp)
                ) {
                    Icon(Icons.Default.Settings, contentDescription = "Control", modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Control", fontSize = 14.sp)
                }

                // Botón Editar
                Button(
                    onClick = { /* Acción editar */ },
                    shape = RoundedCornerShape(50),
                    colors = ButtonDefaults.buttonColors(containerColor = OsitoPolarGreenButton),
                    contentPadding = PaddingValues(horizontal = 20.dp, vertical = 10.dp)
                ) {
                    Icon(Icons.Default.Edit, contentDescription = "Editar", modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Editar", fontSize = 14.sp)
                }

                // Botón Eliminar
                Button(
                    onClick = { /* Acción eliminar */ },
                    shape = RoundedCornerShape(50),
                    colors = ButtonDefaults.buttonColors(containerColor = OsitoPolarRedButton),
                    contentPadding = PaddingValues(all = 8.dp),
                    modifier = Modifier.size(44.dp)
                ) {
                    Icon(Icons.Default.Delete, contentDescription = "Eliminar", modifier = Modifier.size(18.dp))
                }
            }
        }
    }
}

// ===============================================================
//              TARJETA DE MANTENIMIENTO
// ===============================================================
@Composable
private fun MaintenanceCardContent() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(3.dp, RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier
                .padding(20.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(90.dp)
                    .background(Color.Gray.copy(alpha = 0.2f), RoundedCornerShape(12.dp))
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Cámara frigorífica modular",
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp,
                color = Color.Black
            )
            Text(
                text = "Cliente: Nahuel Barrera",
                color = Color.Gray,
                fontSize = 13.sp,
                modifier = Modifier.padding(top = 2.dp)
            )

            Spacer(modifier = Modifier.height(20.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = { /* Acción Solicitar */ },
                    shape = RoundedCornerShape(50),
                    colors = ButtonDefaults.buttonColors(containerColor = OsitoPolarGreenButton),
                    contentPadding = PaddingValues(horizontal = 24.dp, vertical = 8.dp)
                ) {
                    Text("Solicitar", fontSize = 14.sp)
                }
                Button(
                    onClick = { /* Acción Pendiente */ },
                    shape = RoundedCornerShape(50),
                    colors = ButtonDefaults.buttonColors(containerColor = OsitoPolarRedButton),
                    contentPadding = PaddingValues(horizontal = 24.dp, vertical = 8.dp)
                ) {
                    Text("Pendiente", fontSize = 14.sp)
                }
            }
        }
    }
}
