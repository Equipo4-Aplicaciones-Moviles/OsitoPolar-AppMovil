package com.example.ositopolarapp.ui.composables

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * El Composable para la barra de navegación superior (TopBar).
 * Es 'public' (por defecto) para poder usarlo desde cualquier pantalla.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OsitoPolarTopBar(
    modifier: Modifier = Modifier,
    onMenuClicked: () -> Unit = {}
) {
    TopAppBar(
        modifier = modifier,
        title = {
            Text(
                text = "OsitoPolar",
                style = MaterialTheme.typography.headlineLarge, // Estilo del logo
                color = MaterialTheme.colorScheme.primary // Color azul del logo
            )
        },
        actions = {
            IconButton(onClick = onMenuClicked) {
                Icon(
                    imageVector = Icons.Default.Menu,
                    tint = MaterialTheme.colorScheme.primary,
                    contentDescription = "Menú"
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            // Fondo blanco/claro para el TopBar
            containerColor = MaterialTheme.colorScheme.surface
        )
    )
}

/**
 * El Composable para el pie de página (Footer).
 * Es 'public' (por defecto) para poder usarlo desde cualquier pantalla.
 */
@Composable
fun OsitoPolarFooter(modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        // Color de fondo claro, similar al de la imagen
        color = MaterialTheme.colorScheme.surface
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp), // Padding vertical para el footer
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Texto de Copyright
            Text(
                text = "© 2025 OsitoPolar. All rights reserved.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f) // Color grisáceo
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Fila para los enlaces
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                FooterLink(text = "Terms and Conditions")
                FooterLink(text = "Privacy Policy")
                FooterLink(text = "Cookie Policy")
            }
        }
    }
}

/**
 * Un Composable de ayuda para los enlaces del footer.
 * Este puede ser 'private' porque SOLO se usa dentro de OsitoPolarFooter.
 */
@Composable
private fun FooterLink(text: String) {
    TextButton(
        onClick = { /* TODO: Acción de navegación */ },
        contentPadding = PaddingValues(horizontal = 8.dp) // Menos padding entre enlaces
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelSmall, // Texto pequeño
            color = MaterialTheme.colorScheme.primary // Color azul de enlace
        )
    }
}