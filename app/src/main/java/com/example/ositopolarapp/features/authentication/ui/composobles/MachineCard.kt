package com.example.ositopolarapp.features.authentication.ui.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
// Importaciones de tema
import com.example.ositopolarapp.ui.theme.OsitoPolarAccentBlue
import com.example.ositopolarapp.ui.theme.OsitoPolarGreenButton
import com.example.ositopolarapp.ui.theme.OsitoPolarRedButton
// <<-- IMPORTACIÓN CORRECTA PARA NavHostController (Funcionará después de arreglar Gradle) -->>
import androidx.navigation.NavHostController
import com.example.ositopolarapp.features.authentication.ui.screen.MainDestinations

@Composable
fun MachineCard(
    title: String, subtitle: String, temperature: String,
    status: String, location: String,
    navController: NavHostController,
    controlRoute: String = MainDestinations.MACHINE_CONTROL_ROUTE
) {
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
            Icon(
                Icons.Default.Settings,
                contentDescription = "Estado del equipo",
                tint = OsitoPolarGreenButton,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = title, fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
                Text(text = subtitle, color = Color.Gray, fontSize = 12.sp)
            }
            Box(
                modifier = Modifier
                    .size(12.dp)
                    .background(OsitoPolarGreenButton, shape = CircleShape)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Box(
            modifier = Modifier
                .height(120.dp)
                .width(60.dp)
                .background(Color.Gray.copy(alpha = 0.2f), RoundedCornerShape(4.dp))
        ) { /* Placeholder */ }

        Spacer(modifier = Modifier.height(8.dp))

        Text(text = temperature, fontSize = 52.sp, fontWeight = FontWeight.Thin)
        Text(text = status, color = Color.Gray, fontSize = 14.sp, modifier = Modifier.padding(bottom = 12.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                Icons.Default.LocationOn,
                contentDescription = "Ubicación",
                tint = Color(0xFFE57373),
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(text = location, color = Color.Gray, fontSize = 12.sp)
        }

        Spacer(modifier = Modifier.height(20.dp))

        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                // La referencia a .navigate() funcionará después de arreglar Gradle
                onClick = { navController.navigate(controlRoute) },
                shape = RoundedCornerShape(50),
                colors = ButtonDefaults.buttonColors(containerColor = OsitoPolarAccentBlue),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Icon(Icons.Default.Settings, contentDescription = "Control", modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("Control", fontSize = 14.sp)
            }
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