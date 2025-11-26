package com.example.ositopolarapp.features.onboarding.presentation.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ositopolarapp.R
import com.example.ositopolarapp.navigation.ui.composables.OsitoButton
// Importamos el nuevo color fuerte
import com.example.ositopolarapp.ui.theme.OsitoGradientStart
import com.example.ositopolarapp.ui.theme.OsitoBlack
import com.example.ositopolarapp.ui.theme.OsitoGray

@Composable
fun GetStartedScreen(
    onNavigateToSelectProfile: () -> Unit
) {
    // 1. FONDO: Degradado de Azul Cielo -> Blanco
    val backgroundGradient = Brush.verticalGradient(
        colors = listOf(
            OsitoGradientStart, // Azul (Arriba)
            Color.White         // FFFFFF (Abajo)
        )
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = backgroundGradient)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Empuja contenido al centro
            Spacer(modifier = Modifier.weight(1f))

            // Imagen
            Image(
                painter = painterResource(id = R.drawable.osito_polar_welcome),
                contentDescription = "Oso polar",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp),
                contentScale = ContentScale.Fit
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Título
            Text(
                text = "Gestión de frío\nen un solo clic.",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = OsitoBlack,
                textAlign = TextAlign.Center,
                lineHeight = 40.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Descripción
            Text(
                text = "Monitorea, previene fallas y optimiza tus equipos desde un solo lugar.",
                fontSize = 16.sp,
                color = OsitoGray,
                textAlign = TextAlign.Center
            )

            // Empuja botón al fondo
            Spacer(modifier = Modifier.weight(1f))

            // Botón
            OsitoButton(
                text = "Get started",
                onClick = onNavigateToSelectProfile
            )

            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}