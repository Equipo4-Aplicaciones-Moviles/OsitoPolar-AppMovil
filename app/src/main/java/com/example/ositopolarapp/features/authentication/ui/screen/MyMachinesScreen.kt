package com.example.ositopolarapp.features.authentication.ui.screen

// IMPORTS DEL TEMA
import com.example.ositopolarapp.ui.theme.OsitoPolarAccentBlue
import com.example.ositopolarapp.ui.theme.OsitoPolarGreenButton
import com.example.ositopolarapp.ui.theme.OsitoPolarRedButton

// IMPORTS DE COMPOSE Y MATERIAL
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState // Para Scroll
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll // Para Scroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.LocationOn
// Usamos Settings que está garantizado que funciona
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight // Import FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
// <<-- IMPORTACIÓN NECESARIA PARA NavController -->>
import androidx.navigation.NavController
// Importa el Footer común (Asumiendo que está en composables)
import com.example.ositopolarapp.features.authentication.ui.composables.FooterContent

// <<-- FIRMA CORREGIDA: AÑADIDO navController -->>
@Composable
fun MyMachinesScreen(paddingValues: PaddingValues, navController: NavController) {
    // Columna principal con Scroll
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .background(Color.White)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // --- Título Principal ---
        Text(
            text = "Mis equipos",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = OsitoPolarAccentBlue,
            modifier = Modifier.padding(bottom = 24.dp),
            textAlign = TextAlign.Center
        )

        // --- Tarjeta del Equipo ---
        // Se pasa el navController al componente interno
        MachineCardContent(navController = navController)

        Spacer(modifier = Modifier.height(32.dp))

        // --- Título Mantenimientos ---
        Text(
            text = "Mantenimientos",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = OsitoPolarAccentBlue,
            modifier = Modifier.padding(bottom = 16.dp),
            textAlign = TextAlign.Center
        )

        // --- Tarjeta de Mantenimiento ---
        MaintenanceCardContent()

        Spacer(modifier = Modifier.height(32.dp))

        // --- Pie de Página (Importado) ---
        FooterContent()
    }
}

// =========================================================================
//         COMPONENTES INTERNOS PARA MyMachinesScreen
// =========================================================================

@Composable
// <<-- FIRMA CORREGIDA: AÑADIDO navController -->>
private fun MachineCardContent(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, Color.LightGray.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icono de estado (Settings en verde)
            Icon(
                Icons.Default.Settings,
                contentDescription = "Estado del equipo",
                tint = OsitoPolarGreenButton,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = "Industrial Freezer XK-400", fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
                Text(text = "Congelador XK-400", color = Color.Gray, fontSize = 12.sp)
            }
            // Círculo de estado verde
            Box(
                modifier = Modifier
                    .size(12.dp)
                    .background(OsitoPolarGreenButton, shape = CircleShape)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // PLACEHOLDER PARA LA IMAGEN
        Box(
            modifier = Modifier
                .height(120.dp)
                .width(60.dp)
                .background(Color.Gray.copy(alpha = 0.2f), RoundedCornerShape(4.dp))
        ) { /* Placeholder */ }

        Spacer(modifier = Modifier.height(8.dp))

        // Temperatura con peso Thin
        Text(text = "25°", fontSize = 52.sp, fontWeight = FontWeight.Thin)
        Text(text = "(normal)", color = Color.Gray, fontSize = 14.sp, modifier = Modifier.padding(bottom = 12.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                Icons.Default.LocationOn,
                contentDescription = "Ubicación",
                tint = Color(0xFFE57373), // Rojo/rosa suave
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(text = "San Isidro Main Warehouse", color = Color.Gray, fontSize = 12.sp)
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Botones (Control, Editar, Eliminar - Forma píldora)
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically // Alinea botones verticalmente
        ) {
            // Botón Control (Usa navController)
            Button(
                onClick = { navController.navigate(MainDestinations.MACHINE_CONTROL_ROUTE) },
                shape = RoundedCornerShape(50),
                colors = ButtonDefaults.buttonColors(containerColor = OsitoPolarAccentBlue),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Icon(Icons.Default.Settings, contentDescription = "Control", modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("Control", fontSize = 14.sp)
            }
            // Botón Editar
            Button(
                onClick = { /* Acción Editar */ },
                shape = RoundedCornerShape(50),
                colors = ButtonDefaults.buttonColors(containerColor = OsitoPolarAccentBlue),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Icon(Icons.Default.Edit, contentDescription = "Editar", modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("Editar", fontSize = 14.sp)
            }
            // Botón Eliminar (Solo icono)
            Button(
                onClick = { /* Acción Eliminar */ },
                shape = RoundedCornerShape(50),
                colors = ButtonDefaults.buttonColors(containerColor = OsitoPolarAccentBlue),
                contentPadding = PaddingValues(all = 8.dp),
                modifier = Modifier.size(40.dp)
            ) {
                Icon(Icons.Default.Delete, contentDescription = "Eliminar", modifier = Modifier.size(18.dp))
            }
        }
    }
}

@Composable
private fun MaintenanceCardContent() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, Color.LightGray.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Placeholder para la imagen de la cámara
        Box(
            modifier = Modifier
                .height(80.dp)
                .width(80.dp)
                .background(Color.Gray.copy(alpha = 0.2f), RoundedCornerShape(4.dp))
                .padding(bottom = 8.dp)
        ) { /* Placeholder */ }

        Text(text = "Cámara frigorífica modular", fontWeight = FontWeight.SemiBold, fontSize = 16.sp, modifier = Modifier.padding(bottom = 4.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(text = "Cliente:", color = Color.Gray, fontSize = 12.sp)
            Text(text = "Nahuel Barrera", color = Color.Black, fontSize = 12.sp)
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Botones (Solicitar, Pendiente - Forma píldora)
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            Button(onClick = { /* Acción */ },
                shape = RoundedCornerShape(50),
                colors = ButtonDefaults.buttonColors(containerColor = OsitoPolarGreenButton),
                contentPadding = PaddingValues(horizontal = 24.dp, vertical = 8.dp)) {
                Text("Solicitar", fontSize = 14.sp)
            }
            Button(onClick = { /* Acción */ },
                shape = RoundedCornerShape(50),
                colors = ButtonDefaults.buttonColors(containerColor = OsitoPolarRedButton),
                contentPadding = PaddingValues(horizontal = 24.dp, vertical = 8.dp)) {
                Text("Pendiente", fontSize = 14.sp)
            }
        }
    }
}

// FooterContent se importa desde CommonComposables.kt