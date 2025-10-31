package com.example.ositopolarapp.features.authentication.ui.screen

// IMPORTS NECESARIOS
import androidx.compose.foundation.ExperimentalFoundationApi // Necesario para Pager
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.* // Importación general para layout
import androidx.compose.foundation.pager.HorizontalPager // El componente Pager
import androidx.compose.foundation.pager.rememberPagerState // Para manejar el estado del Pager
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape // Para el indicador
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
// Importaciones específicas y verificadas para los iconos
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.Thermostat
import androidx.compose.material3.* // Importación general para Surface y Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip // Para redondear
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector // Para el parámetro Icono
import androidx.compose.ui.text.SpanStyle // Necesario para estilos de texto
import androidx.compose.ui.text.buildAnnotatedString // Necesario para estilos de texto
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign // Necesario para centrar texto
import androidx.compose.ui.text.withStyle // Necesario para estilos de texto
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
// Importaciones de tema
import com.example.ositopolarapp.ui.theme.OsitoPolarAccentBlue
import com.example.ositopolarapp.ui.theme.OsitoPolarGreenButton
// Importa el Footer común
import com.example.ositopolarapp.features.authentication.ui.composables.FooterContent

@OptIn(ExperimentalFoundationApi::class) // Habilitar API experimental de Pager
@Composable
fun MachineControlScreen(paddingValues: PaddingValues) {

    // Estado para el Pager (2 páginas)
    val pagerState = rememberPagerState(pageCount = { 2 })

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues) // Padding del Scaffold
            .background(Color.White)
            .verticalScroll(rememberScrollState()) // Habilitar scroll general
            .padding(vertical = 24.dp), // Padding vertical general
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // --- TÍTULO PRINCIPAL "MIS EQUIPOS" ---
        Text(
            text = "Mis equipos",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = OsitoPolarAccentBlue,
            modifier = Modifier.padding(bottom = 24.dp),
            textAlign = TextAlign.Center
        )

        // --- SECCIÓN 1: VITRINA VERTICAL (Resumen superior) ---
        Box(modifier = Modifier.padding(horizontal = 16.dp)) {
            MachineSummaryCard()
        }

        Spacer(modifier = Modifier.height(32.dp))

        // --- SECCIÓN 2: SERIE / CÓDIGO / UBICACIÓN (CON BORDE Y CENTRADO) ---
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp), // Padding a los lados
            contentAlignment = Alignment.Center // Centra la Column interna
        ) {
            MachineDetailsSection() // Usa la versión con borde y contenido centrado
        }

        Spacer(modifier = Modifier.height(32.dp))

        // --- SECCIÓN 3: MONITOREO EN TIEMPO REAL (Título Centrado) ---
        Column(modifier = Modifier.padding(horizontal = 16.dp)) {
            SectionTitleControl("Monitoreo en tiempo real") // Ahora se centrará
            Spacer(modifier = Modifier.height(8.dp))
            MonitoringItem(icon = Icons.Default.FlashOn, label = "Consumo de energía", value = "150 W")
            MonitoringItem(icon = Icons.Default.Thermostat, label = "Temperatura", value = "-18 °C")
            MonitoringItem(icon = Icons.Default.Bolt, label = "Voltaje", value = "220 V")
        }

        Spacer(modifier = Modifier.height(32.dp))

        // --- SECCIÓN 4: PAGER HORIZONTAL (Centrado) ---
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(0.dp),
            pageSpacing = 0.dp
        ) { page ->
            // Box contenedor que centra el contenido de la página
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                // Columna que agrupa las dos tarjetas y ajusta su ancho
                Column(
                    modifier = Modifier.width(IntrinsicSize.Max), // Ajusta al contenido
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    when (page) {
                        0 -> {
                            InfoCard("Detalles técnicos", listOf("Modelo: VT-500", "Marca: ISA", "Refrigerante: R-134"))
                            InfoCard("Estado del sistema", listOf("Operativo"), icon = Icons.Default.CheckCircle, iconColor = OsitoPolarGreenButton)
                        }
                        1 -> {
                            InfoCard("Mantenimiento", listOf("Último: 15 Mar 2025", "Próximo: -"))
                            InfoCard("Notas", listOf("Equipo funcionando correctamente"))
                        }
                    }
                }
            }
        }
        // Indicador de Página
        Row(
            Modifier
                .height(20.dp)
                .fillMaxWidth()
                .padding(top = 8.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            repeat(pagerState.pageCount) { iteration ->
                val color = if (pagerState.currentPage == iteration) OsitoPolarAccentBlue else Color.LightGray.copy(alpha=0.5f)
                Box(
                    modifier = Modifier
                        .padding(horizontal = 4.dp)
                        .clip(CircleShape)
                        .background(color)
                        .size(8.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))
        Box(modifier = Modifier.padding(horizontal = 16.dp)) {
            FooterContent()
        }
    }
}

// =========================================================================
//         COMPONENTES AUXILIARES (MachineDetailsSection, DetailItem, SectionTitleControl CORREGIDOS)
// =========================================================================

@Composable
private fun SectionTitleControl(title: String) {
    Text(
        text = title,
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold,
        color = OsitoPolarAccentBlue,
        modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp),
        textAlign = TextAlign.Center // <<-- CORREGIDO: Centrado
    )
}

@Composable
private fun MachineSummaryCard() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, Color.LightGray.copy(alpha = 0.4f), RoundedCornerShape(10.dp))
            .padding(vertical = 16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box(
                modifier = Modifier
                    .height(100.dp).width(50.dp)
                    .background(Color.Gray.copy(alpha = 0.2f), RoundedCornerShape(4.dp))
            ) { /* Placeholder Imagen */ }
            Spacer(modifier = Modifier.height(8.dp))
            Text("Vitrina vertical para congelados", fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
        }
    }
}

// <<-- CORREGIDO: MachineDetailsSection CON BORDE Y CONTENIDO CENTRADO -->>
@Composable
private fun MachineDetailsSection() {
    Column(
        modifier = Modifier
            // Opcional: Si quieres limitar el ancho máximo de la tarjeta
            .widthIn(max = 320.dp)
            .fillMaxWidth() // Usa el ancho disponible hasta el máximo
            .border(1.dp, Color.LightGray.copy(alpha = 0.4f), RoundedCornerShape(10.dp)) // Borde añadido
            .padding(vertical = 16.dp, horizontal = 16.dp), // Padding interno añadido
        // <<-- CORRECCIÓN: Alineación Central del contenido -->>
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp) // Espacio entre ítems
    ) {
        DetailItem("Serie:", "8385332")
        DetailItem("Código:", "0000873")
        DetailItem("Ubicación física:", "Zona de congelados - Puerta 2")
    }
}

// <<-- CORREGIDO: DetailItem con texto centrado -->>
@Composable
private fun DetailItem(label: String, value: String) {
    Text(
        buildAnnotatedString {
            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold, color = OsitoPolarAccentBlue, fontSize = 16.sp)) {
                append(label)
            }
            append(" ")
            withStyle(style = SpanStyle(color = Color.Black.copy(alpha = 0.87f), fontWeight = FontWeight.Bold, fontSize = 16.sp)) {
                append(value)
            }
        },
        textAlign = TextAlign.Center // <<-- CORRECCIÓN: Centrar texto
        // Sin fillMaxWidth
    )
}

// <<-- MonitoringItem sin cambios -->>
@Composable
private fun MonitoringItem(icon: ImageVector, label: String, value: String){
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = icon,
                    contentDescription = label,
                    tint = Color.DarkGray.copy(alpha = 0.8f), // Icono gris oscuro
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(label, fontSize = 16.sp, color = Color.DarkGray) // Label gris oscuro
            }
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.LightGray.copy(alpha = 0.2f)) // Fondo gris claro
                    .padding(horizontal = 12.dp, vertical = 4.dp) // Padding interno
            ) {
                Text(
                    text = value,
                    color = Color.Black.copy(alpha = 0.9f), // Color de texto negro/oscuro
                    fontWeight = FontWeight.Medium,
                    fontSize = 14.sp
                )
            }
        }
        HorizontalDivider(color = Color.LightGray.copy(alpha = 0.3f), thickness = 0.5.dp)
    }
}

// <<-- InfoCard sin cambios -->>
@Composable
private fun InfoCard(title: String, details: List<String>, icon: ImageVector? = null, iconColor: Color = OsitoPolarAccentBlue){
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
            .border(1.dp, Color.LightGray.copy(alpha = 0.4f), RoundedCornerShape(10.dp))
            .padding(12.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            if (icon != null) {
                Icon(icon, contentDescription = title, tint = iconColor, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
            }
            Text(title, fontWeight = FontWeight.Bold, color = OsitoPolarAccentBlue, fontSize = 14.sp)
        }
        Spacer(modifier = Modifier.height(4.dp))
        details.forEach { detail ->
            Text(detail, fontSize = 12.sp, color = Color.DarkGray, modifier = Modifier.padding(bottom = 2.dp))
        }
    }
}