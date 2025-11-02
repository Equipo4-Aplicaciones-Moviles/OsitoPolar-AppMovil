package com.example.ositopolarapp.features.`client-module`.ui.screen

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
// Importaciones de temas (colores)
import com.example.ositopolarapp.ui.theme.OsitoPolarAccentBlue
import com.example.ositopolarapp.ui.theme.OsitoPolarGreenButton
import com.example.ositopolarapp.ui.theme.OsitoPolarRedButton
// Importa el Footer común
import com.example.ositopolarapp.features.`client-module`.ui.composables.FooterContent

// ----------------------------------------------------------------------
//                    COMPOSABLE PRINCIPAL: HomeScreen
// ----------------------------------------------------------------------
@Composable
fun HomeScreen(paddingValues: PaddingValues) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .background(Color.White) // Fondo blanco puro
            .padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
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

            // --- 2. MANTENIMIENTOS ---
            SectionTitle("Mantenimientos")
            MaintenanceCardItem(
                title = "Vitrina vertical para congelados",
                status = "Pendiente",
                statusColor = OsitoPolarRedButton
            )
            Spacer(modifier = Modifier.height(8.dp))
            MaintenanceCardItem(
                title = "Exhibidora de helados",
                status = "Realizado",
                statusColor = OsitoPolarGreenButton
            )

            Spacer(modifier = Modifier.height(28.dp))

            // --- 3. ALQUILER ---
            SectionTitle("Alquiler")
            RentResumenCard(
                title = "Cámara frigorífica modular",
                price = "200 $ / mes"
            )

            Spacer(modifier = Modifier.height(28.dp))

            // --- 4. ESTADOS DE CUENTA ---
            SectionTitle("Estados de cuenta")
            AccountStatementCard(
                client = "FRITMO CORP",
                amount = "S/. 2351.23",
                status = "Recibido",
                statusColor = OsitoPolarGreenButton
            )
            Spacer(modifier = Modifier.height(8.dp))
            AccountStatementCard(
                client = "COOLPROV S.A.C.",
                amount = "S/. 458.5",
                status = "Pendiente",
                statusColor = OsitoPolarRedButton
            )

            Spacer(modifier = Modifier.height(24.dp))
        }

        // Pie de página
        FooterContent()
    }
}

// =========================================================================
//                  COMPONENTES AUXILIARES
// =========================================================================

@Composable
fun SectionTitle(title: String) {
    Text(
        text = title,
        fontSize = 22.sp,
        fontWeight = FontWeight.Bold,
        color = OsitoPolarAccentBlue,
        modifier = Modifier
            .padding(bottom = 16.dp)
            .fillMaxWidth(),
        textAlign = TextAlign.Center
    )
}

@Composable
fun MachineCardResumen(title: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth(0.95f),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .background(Color.Gray.copy(alpha = 0.3f), RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.Center
            ) { /* Placeholder centralizado */ }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = title,
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
fun MaintenanceCardItem(title: String, status: String, statusColor: Color) {
    Card(
        modifier = Modifier
            .fillMaxWidth(0.95f),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        // Usamos Column para poder centrar verticalmente si el contenido crece
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title,
                    color = Color.Black,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.weight(0.8f)
                )
                Text(
                    text = status,
                    color = statusColor,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.weight(0.2f),
                    textAlign = TextAlign.End
                )
            }
        }
    }
}

@Composable
fun RentResumenCard(title: String, price: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth(0.95f),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .background(Color.Gray.copy(alpha = 0.3f), RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.Center
            ) { /* Placeholder centralizado */ }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = title,
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            Text(
                text = price,
                color = OsitoPolarRedButton,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                modifier = Modifier.padding(top = 6.dp)
            )

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
fun AccountStatementCard(client: String, amount: String, status: String, statusColor: Color) {
    Card(
        modifier = Modifier
            .fillMaxWidth(0.95f),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = client,
                fontWeight = FontWeight.Medium,
                fontSize = 14.sp,
                modifier = Modifier.weight(0.45f)
            )
            Text(
                text = amount,
                color = Color.Black,
                fontSize = 14.sp,
                modifier = Modifier.weight(0.30f),
                textAlign = TextAlign.Center
            )
            Text(
                text = status,
                color = statusColor,
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp,
                modifier = Modifier.weight(0.25f),
                textAlign = TextAlign.End
            )
        }
    }
}
