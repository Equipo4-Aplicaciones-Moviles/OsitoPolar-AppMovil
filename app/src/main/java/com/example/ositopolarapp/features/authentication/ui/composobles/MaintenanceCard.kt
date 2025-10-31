package com.example.ositopolarapp.features.authentication.ui.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
// Importaciones de tema
import com.example.ositopolarapp.ui.theme.OsitoPolarGreenButton
import com.example.ositopolarapp.ui.theme.OsitoPolarRedButton

@Composable
fun MaintenanceCard(
    title: String, client: String, statusPending: Boolean
) {
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

        Text(text = title, fontWeight = FontWeight.SemiBold, fontSize = 16.sp, modifier = Modifier.padding(bottom = 4.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(text = "Cliente:", color = Color.Gray, fontSize = 12.sp)
            Text(text = client, color = Color.Black, fontSize = 12.sp)
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