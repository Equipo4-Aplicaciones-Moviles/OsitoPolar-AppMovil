package com.example.ositopolarapp.features.profile.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ositopolarapp.ui.theme.OsitoBluePrimary

// ESTA ES LA VERSIÓN CORRECTA QUE EL DASHBOARD ESTÁ BUSCANDO
@Composable
fun ProfileScreen(
    username: String,
    userType: String,
    planName: String,
    onLogout: () -> Unit,
    onNavigateToEditProfile: () -> Unit,
    onNavigateToPaymentHistory: () -> Unit,
    onNavigateTo2FASettings: () -> Unit,
    onNavigateToSettings: () -> Unit,
    onNavigateToUpgradePlan: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // 1. Cabecera (Avatar y Nombre)
        Box(
            modifier = Modifier
                .size(100.dp)
                .background(OsitoBluePrimary.copy(alpha = 0.1f), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = if (username.isNotEmpty()) username.take(1).uppercase() else "U",
                style = MaterialTheme.typography.displayMedium,
                color = OsitoBluePrimary
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = username,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = userType,
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(24.dp))

        // 2. Tarjeta de Plan
        Card(
            colors = CardDefaults.cardColors(containerColor = OsitoBluePrimary),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text("Plan Actual", color = Color.White.copy(alpha = 0.8f), fontSize = 12.sp)
                    Text(planName, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                }
                Button(
                    onClick = onNavigateToUpgradePlan,
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                    contentPadding = PaddingValues(horizontal = 12.dp)
                ) {
                    Text("Mejorar", color = OsitoBluePrimary)
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Configuración",
            modifier = Modifier.fillMaxWidth(),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        // 3. Opciones del Menú
        ProfileOptionItem(Icons.Default.Edit, "Editar Perfil", onNavigateToEditProfile)
        ProfileOptionItem(Icons.Default.Settings, "Configuración General", onNavigateToSettings)
        ProfileOptionItem(Icons.Default.Security, "Seguridad 2FA", onNavigateTo2FASettings)
        ProfileOptionItem(Icons.Default.Receipt, "Historial de Pagos", onNavigateToPaymentHistory)

        Spacer(modifier = Modifier.height(24.dp))

        // 4. Botón Cerrar Sesión
        Button(
            onClick = onLogout,
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFEBEE), contentColor = Color.Red),
            modifier = Modifier.fillMaxWidth().height(50.dp)
        ) {
            Icon(Icons.Default.ExitToApp, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Cerrar Sesión")
        }
    }
}

@Composable
fun ProfileOptionItem(icon: ImageVector, title: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(imageVector = icon, contentDescription = null, tint = Color.Gray)
        Spacer(modifier = Modifier.width(16.dp))
        Text(text = title, modifier = Modifier.weight(1f), fontSize = 16.sp)
        Icon(imageVector = Icons.Default.ChevronRight, contentDescription = null, tint = Color.Gray)
    }
    Divider(color = Color.LightGray.copy(alpha = 0.3f))
}