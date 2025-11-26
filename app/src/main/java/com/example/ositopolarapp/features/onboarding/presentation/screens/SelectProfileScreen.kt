package com.example.ositopolarapp.features.onboarding.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Inventory2 // Ícono de Caja/Inventario
import androidx.compose.material.icons.outlined.Store
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ositopolarapp.navigation.ui.composables.OsitoButton
import com.example.ositopolarapp.ui.theme.OsitoBlack
import com.example.ositopolarapp.ui.theme.OsitoBluePrimary
import com.example.ositopolarapp.ui.theme.OsitoGradientStart
import com.example.ositopolarapp.ui.theme.OsitoGray

@Composable
fun SelectProfileScreen(
    onNavigateNext: () -> Unit
) {
    val backgroundGradient = Brush.verticalGradient(
        colors = listOf(OsitoGradientStart, Color.White)
    )

    var selectedProfile by remember { mutableStateOf<String?>(null) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = backgroundGradient)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(60.dp))

            // --- TARJETA: CLIENTE ---
            ProfileCardItem(
                role = "Soy un",
                title = "Cliente",
                description = "Quiero monitorear mis equipos de frío y gestionar el mantenimiento.",
                icon = Icons.Outlined.Store,
                isSelected = selectedProfile == "client",
                onClick = { selectedProfile = "client" }
            )

            Spacer(modifier = Modifier.height(24.dp))

            // --- TARJETA: EMPRESA ---
            // CAMBIO: Usamos Icons.Outlined.Inventory2 para que parezca una CAJA como en Figma
            ProfileCardItem(
                role = "Soy una",
                title = "Empresa",
                description = "Quiero gestionar el inventario de equipos y las ventas.",
                icon = Icons.Outlined.Inventory2,
                isSelected = selectedProfile == "company",
                onClick = { selectedProfile = "company" }
            )

            Spacer(modifier = Modifier.weight(1f))

            OsitoButton(
                text = "Continuar",
                onClick = onNavigateNext
            )

            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}

@Composable
fun ProfileCardItem(
    role: String,
    title: String,
    description: String,
    icon: ImageVector,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val borderColor = if (isSelected) OsitoBluePrimary else Color.Transparent
    val borderWidth = if (isSelected) 2.dp else 0.dp

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .border(borderWidth, borderColor, RoundedCornerShape(24.dp)),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        // Ajuste de sombra: Un poco más suave
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(24.dp) // Padding interno generoso
                .fillMaxWidth(),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                // AJUSTE: Texto más pequeño y peso Medium (no Bold) para diferenciarse del título
                Text(
                    text = role,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = OsitoBlack
                )

                // Espacio reducido entre "Soy un" y "Cliente" para que se lean juntos
                Spacer(modifier = Modifier.height(2.dp))

                // AJUSTE: Texto muy grande y ExtraBold
                Text(
                    text = title,
                    fontSize = 34.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = OsitoBlack,
                    lineHeight = 34.sp // Altura de línea ajustada
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = description,
                    fontSize = 14.sp,
                    color = OsitoGray,
                    lineHeight = 20.sp
                )
            }

            // Icono más grande y alineado
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier
                    .size(50.dp)
                    .padding(start = 8.dp),
                tint = OsitoBlack
            )
        }
    }
}