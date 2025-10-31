package com.example.ositopolarapp.features.authentication.ui.screen

// IMPORTS NECESARIOS
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
// Importaciones para Scroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.shape.RoundedCornerShape
// Importaciones de temas (colores)
import com.example.ositopolarapp.ui.theme.OsitoPolarAccentBlue
import com.example.ositopolarapp.ui.theme.OsitoPolarGreenButton
import com.example.ositopolarapp.ui.theme.OsitoPolarRedButton
// Importa el Footer común
import com.example.ositopolarapp.features.authentication.ui.composables.FooterContent
// Importaciones para imágenes (descomentar si usas painterResource)
// import androidx.compose.foundation.Image
// import androidx.compose.ui.res.painterResource
// import com.example.ositopolarapp.R

// ----------------------------------------------------------------------
//                    COMPOSABLE PRINCIPAL: HomeScreen
// ----------------------------------------------------------------------
@Composable
fun HomeScreen(paddingValues: PaddingValues) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues) // Usa el padding del Scaffold
            .background(Color.White)
            .padding(horizontal = 20.dp), // Padding horizontal general
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Columna principal del contenido (CON SCROLL)
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(vertical = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // --- 1. MIS EQUIPOS (Resumen) ---
            SectionTitle("Mis equipos")
            MachineCardResumen(
                title = "Vitrina vertical para congelados"
            )

            Spacer(modifier = Modifier.height(28.dp))

            // --- 2. MANTENIMIENTOS (Resumen) ---
            SectionTitle("Mantenimientos")
            MaintenanceResumenItem(
                title = "Vitrina vertical para congelados",
                status = "Pendiente",
                statusColor = OsitoPolarRedButton
            )
            MaintenanceResumenItem(
                title = "Exhibidora de helados",
                status = "Realizado",
                statusColor = OsitoPolarGreenButton
            )

            Spacer(modifier = Modifier.height(28.dp))

            // --- 3. ALQUILER (Resumen) ---
            SectionTitle("Alquiler")
            RentResumenCard(
                title = "Cámara frigorífica modular",
                price = "200 $ / mes"
            )

            Spacer(modifier = Modifier.height(28.dp))

            // --- 4. ESTADOS DE CUENTA (Resumen - Alineación corregida) ---
            SectionTitle("Estados de cuenta")
            AccountStatementItem(
                client = "FRITMO CORP",
                amount = "S/. 2351.23",
                status = "Recibido",
                statusColor = OsitoPolarGreenButton
            )
            AccountStatementItem(
                client = "COOLPROV S.A.C.",
                amount = "S/. 458.5",
                status = "Pendiente",
                statusColor = OsitoPolarRedButton
            )

            Spacer(modifier = Modifier.height(24.dp))
        }

        // Pie de página (Importado)
        FooterContent()
    }
}

// =========================================================================
//                  COMPONENTES AUXILIARES PARA HOME SCREEN
// =========================================================================

@Composable
fun SectionTitle(title: String) {
    Text(
        text = title,
        fontSize = 28.sp,
        fontWeight = FontWeight.Bold,
        color = OsitoPolarAccentBlue,
        modifier = Modifier.padding(bottom = 16.dp),
        textAlign = TextAlign.Center
    )
}

@Composable
fun MachineCardResumen(title: String) {
    Box(
        modifier = Modifier
            .border(1.dp, Color.LightGray, RoundedCornerShape(8.dp))
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .background(Color.Gray.copy(alpha = 0.3f), RoundedCornerShape(4.dp))
            ) { /* Placeholder */ }
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = title, fontWeight = FontWeight.SemiBold)
        }
    }
}

@Composable
fun MaintenanceResumenItem(title: String, status: String, statusColor: Color) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = title, color = Color.Black, fontSize = 14.sp)
        Text(text = status, color = statusColor, fontWeight = FontWeight.Medium, fontSize = 14.sp)
    }
}

@Composable
fun RentResumenCard(title: String, price: String) {
    Box(
        modifier = Modifier
            .border(1.dp, Color.LightGray, RoundedCornerShape(8.dp))
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .background(Color.Gray.copy(alpha = 0.3f), RoundedCornerShape(4.dp))
            ) { /* Placeholder */ }
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = title, fontWeight = FontWeight.SemiBold)
            Text(text = price, color = OsitoPolarRedButton, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = { /* Acción Solicitar */ },
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = OsitoPolarGreenButton)
            ) {
                Text("Solicitar", fontSize = 14.sp)
            }
        }
    }
}

@Composable
fun AccountStatementItem(client: String, amount: String, status: String, statusColor: Color) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = client,
            fontWeight = FontWeight.Medium,
            fontSize = 14.sp,
            modifier = Modifier.weight(0.4f)
        )
        Text(
            text = amount,
            color = Color.Black,
            fontSize = 14.sp,
            modifier = Modifier.weight(0.3f),
            textAlign = TextAlign.Center
        )
        Text(
            text = status,
            color = statusColor,
            fontWeight = FontWeight.Medium,
            fontSize = 14.sp,
            modifier = Modifier.weight(0.3f),
            textAlign = TextAlign.End
        )
    }
}

// FooterContent y SmallTextLinkHome ahora se importan desde CommonComposables.kt