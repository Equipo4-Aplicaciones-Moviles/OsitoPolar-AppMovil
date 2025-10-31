package com.example.ositopolarapp.features.authentication.ui.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
// Importaciones de los módulos correctos
import com.example.ositopolarapp.features.authentication.ui.screen.MainDestinations
import com.example.ositopolarapp.ui.theme.OsitoPolarDesignBlue

@Composable
fun DrawerContent(
    onItemSelected: (String) -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth(0.75f) // Ancho del Drawer
            .background(OsitoPolarDesignBlue) // Fondo Azul
            .padding(vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly // Distribuye los ítems uniformemente
    ) {

        // Ítems de Navegación usando las rutas definidas en MainDestinations
        DrawerItem(text = "HOME", onClick = { onItemSelected(MainDestinations.HOME_ROUTE) })
        DrawerItem(text = "My Machines", onClick = { onItemSelected(MainDestinations.MY_MACHINES_ROUTE) })
        DrawerItem(text = "Rent", onClick = { onItemSelected(MainDestinations.RENT_ROUTE) })
        DrawerItem(text = "Contact", onClick = { onItemSelected(MainDestinations.CONTACT_ROUTE) })
        DrawerItem(text = "Notifications", onClick = { onItemSelected(MainDestinations.NOTIFICATIONS_ROUTE) })
        DrawerItem(text = "Mi cuenta", onClick = { onItemSelected(MainDestinations.ACCOUNT_ROUTE) }) // Navega a Mi Cuenta
    }
}

@Composable
fun DrawerItem(text: String, onClick: () -> Unit) {
    Text(
        text = text,
        color = Color.White,
        fontSize = 28.sp, // Tamaño ajustado
        fontWeight = FontWeight.Bold,
        textAlign = TextAlign.Center,
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 16.dp) // Espacio vertical entre ítems
    )
}