package com.example.ositopolarapp.features.profile.presentation.screens

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

/**
 * Two Factor Authentication Settings Screen
 *
 * Allows users to enable/disable 2FA and view setup instructions.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TwoFactorSettingsScreen(
    is2FAEnabled: Boolean,
    onEnable2FA: () -> Unit,
    onDisable2FA: () -> Unit,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var showEnableDialog by remember { mutableStateOf(false) }
    var showDisableDialog by remember { mutableStateOf(false) }
    var showQRCode by remember { mutableStateOf(false) }

    // Mock QR Code data (in production, this comes from backend)
    val mockQRCodeUrl = "https://chart.googleapis.com/chart?chs=200x200&cht=qr&chl=otpauth://totp/OsitoPolar:user@example.com?secret=JBSWY3DPEHPK3PXP&issuer=OsitoPolar"
    val mockManualKey = "JBSWY3DPEHPK3PXP"

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Autenticación de Dos Factores",
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Volver"
                        )
                    }
                }
            )
        },
        modifier = modifier
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Status Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = if (is2FAEnabled) {
                        MaterialTheme.colorScheme.primaryContainer
                    } else {
                        MaterialTheme.colorScheme.surfaceVariant
                    }
                )
            ) {
                Row(
                    modifier = Modifier.padding(20.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = null,
                        modifier = Modifier.size(48.dp),
                        tint = if (is2FAEnabled) {
                            MaterialTheme.colorScheme.primary
                        } else {
                            MaterialTheme.colorScheme.onSurfaceVariant
                        }
                    )
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Estado de 2FA",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = if (is2FAEnabled) "Activado" else "Desactivado",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = if (is2FAEnabled) {
                                "Tu cuenta está protegida con 2FA"
                            } else {
                                "Tu cuenta no está protegida con 2FA"
                            },
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            // What is 2FA?
            Text(
                text = "¿Qué es la autenticación de dos factores?",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )

            Card(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "La autenticación de dos factores (2FA) agrega una capa adicional de seguridad a tu cuenta.",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        text = "Además de tu contraseña, necesitarás un código de 6 dígitos generado por una aplicación de autenticación en tu teléfono.",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

            // How it works
            if (!is2FAEnabled) {
                Text(
                    text = "¿Cómo funciona?",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )

                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Step2FARow(
                            number = "1",
                            title = "Descarga una app de autenticación",
                            description = "Google Authenticator, Microsoft Authenticator, Authy, etc."
                        )
                        Divider()
                        Step2FARow(
                            number = "2",
                            title = "Escanea el código QR",
                            description = "O ingresa la clave manualmente en tu app"
                        )
                        Divider()
                        Step2FARow(
                            number = "3",
                            title = "Verifica el código",
                            description = "Ingresa el código de 6 dígitos generado"
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            // Action Button
            if (is2FAEnabled) {
                OutlinedButton(
                    onClick = { showDisableDialog = true },
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Text("Desactivar 2FA", style = MaterialTheme.typography.titleMedium)
                }
            } else {
                Button(
                    onClick = { showEnableDialog = true },
                    modifier = Modifier.fillMaxWidth().height(56.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.QrCode,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Activar 2FA", style = MaterialTheme.typography.titleMedium)
                }
            }

            // Security Note
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.tertiaryContainer
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text("💡", style = MaterialTheme.typography.headlineSmall)
                        Text(
                            text = "Recomendación de Seguridad",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Text(
                        text = "Activar 2FA es altamente recomendado para proteger tu cuenta de accesos no autorizados.",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }
    }

    // Enable 2FA Dialog
    if (showEnableDialog) {
        AlertDialog(
            onDismissRequest = { showEnableDialog = false },
            title = { Text("Activar 2FA") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text("Se generará un código QR que debes escanear con tu aplicación de autenticación.")

                    // QR Code Placeholder
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        shape = MaterialTheme.shapes.medium
                    ) {
                        Column(
                            modifier = Modifier.padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.QrCode,
                                contentDescription = "QR Code",
                                modifier = Modifier.size(120.dp),
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = "Código QR",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    Divider()

                    Text(
                        text = "Clave manual:",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        color = MaterialTheme.colorScheme.primaryContainer,
                        shape = MaterialTheme.shapes.small
                    ) {
                        Text(
                            text = mockManualKey,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(12.dp),
                            textAlign = TextAlign.Center
                        )
                    }

                    Text(
                        text = "En producción, esto mostrará el QR real del backend.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            },
            confirmButton = {
                Button(onClick = {
                    onEnable2FA()
                    showEnableDialog = false
                    Toast.makeText(context, "2FA activado exitosamente", Toast.LENGTH_SHORT).show()
                }) {
                    Text("Confirmar")
                }
            },
            dismissButton = {
                TextButton(onClick = { showEnableDialog = false }) {
                    Text("Cancelar")
                }
            }
        )
    }

    // Disable 2FA Dialog
    if (showDisableDialog) {
        AlertDialog(
            onDismissRequest = { showDisableDialog = false },
            title = { Text("Desactivar 2FA") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("⚠️ ¿Estás seguro?")
                    Text(
                        "Desactivar 2FA reducirá la seguridad de tu cuenta.",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        onDisable2FA()
                        showDisableDialog = false
                        Toast.makeText(context, "2FA desactivado", Toast.LENGTH_SHORT).show()
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Text("Desactivar")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDisableDialog = false }) {
                    Text("Cancelar")
                }
            }
        )
    }
}

@Composable
private fun Step2FARow(number: String, title: String, description: String) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.Top
    ) {
        Surface(
            shape = MaterialTheme.shapes.small,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(32.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Text(
                    text = number,
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
