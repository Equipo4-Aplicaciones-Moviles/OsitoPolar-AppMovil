package com.example.ositopolarapp.features.authentication.ui.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// --- Pie de Página (Componente común) ---
@Composable
fun FooterContent() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp, bottom = 8.dp)
            .background(Color.White) // Asegura fondo blanco
    ) {
        Text(
            text = "© 2025 OsitoPolar. All rights reserved.",
            color = Color.Gray,
            fontSize = 10.sp
        )
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            SmallTextLink("Terms and Conditions")
            SmallTextLink("Privacy Policy")
            SmallTextLink("Cookie Policy")
        }
    }
}

// --- Enlace Pequeño (Componente común) ---
@Composable
fun SmallTextLink(text: String) {
    Text(
        text = text,
        color = Color.Gray,
        fontSize = 10.sp
    )
}