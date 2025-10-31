package com.example.ositopolarapp.features.`client-module`.ui.screen

// IMPORTS NECESARIOS
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape // Para la imagen de perfil
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale // Para escalar imagen
import androidx.compose.ui.res.painterResource // Para cargar imagen drawable
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign // Importar TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
// Importaciones de tema
import com.example.ositopolarapp.ui.theme.OsitoPolarAccentBlue
// Importa el Footer común
import com.example.ositopolarapp.features.`client-module`.ui.composables.FooterContent
// Importar ID de recurso (si tienes imagen)
import com.example.ositopolarapp.R // Asegúrate que tu paquete R sea correcto

@Composable
fun AccountScreen(paddingValues: PaddingValues) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues) // Padding del Scaffold
            .background(Color.White)
            .verticalScroll(rememberScrollState()) // Habilitar scroll
            .padding(horizontal = 20.dp, vertical = 24.dp), // Padding interno
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // --- TÍTULO PRINCIPAL ---
        Text(
            text = "Mi cuenta",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = OsitoPolarAccentBlue,
            modifier = Modifier.padding(bottom = 32.dp),
            textAlign = TextAlign.Center
        )

        // --- SECCIÓN DE PERFIL ---
        ProfileSection(
            name = "Alberto Lionel",
            email = "Alberto.Lionel10@gmail.com"
        )

        Spacer(modifier = Modifier.height(32.dp))

        // --- SECCIÓN TIPO DE PLAN ACTUAL ---
        CurrentPlanSection(
            planName = "Bronce",
            machineLimit = "(Hasta 10 máquinas)"
        )

        Spacer(modifier = Modifier.height(48.dp)) // Más espacio antes de los planes

        // --- SECCIÓN PLANES DISPONIBLES (CON CENTRADO EXPLÍCITO) ---
        Box(
            modifier = Modifier.fillMaxWidth(), // Ocupa el ancho
            contentAlignment = Alignment.Center // Centra el contenido (la Card)
        ) {
            AvailablePlanCard(
                planName = "Plan Oro",
                machineLimit = "(Hasta 20 máquinas)",
                price = "S/. 350 / mes"
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            AvailablePlanCard(
                planName = "Plan Diamante",
                machineLimit = "(Hasta 50 máquinas)",
                price = "S/. 500 / mes"
            )
        }

        // Espacio antes del footer
        Spacer(modifier = Modifier.weight(1f)) // Empuja el footer hacia abajo si hay espacio
        Spacer(modifier = Modifier.height(32.dp))
        FooterContent() // Usa el Composable común importado
    }
}

// =========================================================================
//         COMPONENTES AUXILIARES PARA AccountScreen (AvailablePlanCard CORREGIDO)
// =========================================================================

@Composable
private fun ProfileSection(name: String, email: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        // Placeholder para Imagen de Perfil Circular
        Box(
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape)
                .background(Color.Gray.copy(alpha = 0.2f))
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_launcher_foreground), // Usa un placeholder
                contentDescription = "Foto de perfil",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "Nombre:", color = Color.Gray, fontSize = 12.sp)
        Text(text = name, fontWeight = FontWeight.SemiBold, fontSize = 16.sp, color = Color.Black)
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = "Email:", color = Color.Gray, fontSize = 12.sp)
        Text(text = email, fontWeight = FontWeight.SemiBold, fontSize = 16.sp, color = Color.Black)
    }
}

@Composable
private fun CurrentPlanSection(planName: String, machineLimit: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = "Tipo de plan:", fontWeight = FontWeight.Bold, color = OsitoPolarAccentBlue, fontSize = 16.sp)
        Text(text = planName, fontSize = 14.sp, color = Color.DarkGray)
        Text(text = machineLimit, fontSize = 12.sp, color = Color.Gray)
    }
}

@Composable
private fun AvailablePlanCard(planName: String, machineLimit: String, price: String) {
    Card(
        modifier = Modifier.fillMaxWidth(0.8f), // Ancho de la tarjeta
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                // <<-- CORRECCIÓN: Asegurar padding horizontal interno y alineación -->>
                .fillMaxWidth(), // Hacer que la columna ocupe el ancho de la tarjeta
            // <<-- CORRECCIÓN: Centrar horizontalmente TODO el contenido -->>
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = planName,
                fontWeight = FontWeight.Bold,
                color = OsitoPolarAccentBlue,
                fontSize = 20.sp,
                textAlign = TextAlign.Center // Centrar texto explícitamente
            )
            Text(
                text = machineLimit,
                fontSize = 12.sp,
                color = Color.Gray,
                textAlign = TextAlign.Center // Centrar texto explícitamente
            )
            Text(
                text = price,
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp,
                color = Color.Black,
                textAlign = TextAlign.Center // Centrar texto explícitamente
            )
            Button(
                onClick = { /* Acción Actualizar Plan */ },
                shape = RoundedCornerShape(50),
                colors = ButtonDefaults.buttonColors(containerColor = OsitoPolarAccentBlue),
                contentPadding = PaddingValues(horizontal = 24.dp, vertical = 8.dp)
                // El botón se centrará por la Column padre
            ) {
                Text("Actualizar", fontSize = 14.sp)
            }
        }
    }
}