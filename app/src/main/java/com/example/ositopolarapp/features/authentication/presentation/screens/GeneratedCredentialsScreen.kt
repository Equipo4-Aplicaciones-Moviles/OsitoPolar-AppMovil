package com.example.ositopolarapp.features.authentication.presentation.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ositopolarapp.features.authentication.presentation.state.RegistrationViewModel
import com.example.ositopolarapp.ui.theme.OsitoBluePrimary

@Composable
fun GeneratedCredentialsScreen(
    viewModel: RegistrationViewModel,
    sessionId: String?, // Recibimos el ID que viene de Stripe
    onLoginClicked: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    // 1. Al iniciar la pantalla, si tenemos SessionID, completamos el registro
    LaunchedEffect(sessionId) {
        if (sessionId != null && !uiState.registrationComplete) {
            viewModel.completeRegistration(sessionId)
        }
    }

    Scaffold { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(24.dp),
            contentAlignment = Alignment.Center
        ) {
            when {
                // CASO A: CARGANDO (Video min 1:55)
                uiState.isLoading -> {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        CircularProgressIndicator(
                            color = OsitoBluePrimary,
                            modifier = Modifier.size(60.dp)
                        )
                        Spacer(modifier = Modifier.height(24.dp))
                        Text(
                            text = "Completando registro...",
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Por favor espera mientras procesamos tu pago",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.Gray,
                            textAlign = TextAlign.Center
                        )
                    }
                }

                // CASO B: ERROR
                uiState.error != null -> {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Default.Warning,
                            contentDescription = "Error",
                            tint = Color.Red,
                            modifier = Modifier.size(80.dp)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Hubo un problema",
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = uiState.error ?: "Error desconocido",
                            textAlign = TextAlign.Center,
                            color = Color.Red
                        )
                        Spacer(modifier = Modifier.height(24.dp))
                        Button(onClick = {
                            if (sessionId != null) viewModel.completeRegistration(sessionId)
                        }) {
                            Text("Reintentar")
                        }
                    }
                }

                // CASO C: ÉXITO (Video min 2:00)
                uiState.registrationComplete -> {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = "Éxito",
                            tint = OsitoBluePrimary,
                            modifier = Modifier.size(80.dp)
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = "¡Registro Exitoso!",
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )

                        Text(
                            text = "Tu cuenta ha sido creada.",
                            style = MaterialTheme.typography.bodyLarge,
                            color = Color.Gray
                        )

                        Spacer(modifier = Modifier.height(32.dp))

                        // TARJETA DE CREDENCIALES
                        Card(
                            colors = CardDefaults.cardColors(containerColor = Color(0xFFF2F4F7)),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(
                                    "IMPORTANTE: Estas credenciales se muestran solo una vez. Guárdalas.",
                                    color = Color(0xFFB42318),
                                    fontSize = 12.sp,
                                    lineHeight = 14.sp,
                                    modifier = Modifier.padding(bottom = 16.dp)
                                )

                                CredentialRow(
                                    label = "Usuario",
                                    value = uiState.generatedUsername ?: "---",
                                    icon = Icons.Default.Person,
                                    context = context
                                )

                                Spacer(modifier = Modifier.height(12.dp))

                                CredentialRow(
                                    label = "Contraseña",
                                    value = uiState.generatedPassword ?: "---",
                                    icon = Icons.Default.Lock,
                                    context = context
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(40.dp))

                        Button(
                            onClick = onLoginClicked,
                            modifier = Modifier.fillMaxWidth().height(50.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = OsitoBluePrimary)
                        ) {
                            Text("Ir a Iniciar Sesión")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CredentialRow(label: String, value: String, icon: androidx.compose.ui.graphics.vector.ImageVector, context: Context) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White, RoundedCornerShape(8.dp))
            .padding(12.dp)
    ) {
        Icon(imageVector = icon, contentDescription = null, tint = OsitoBluePrimary)
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(text = label, fontSize = 12.sp, color = Color.Gray)
            Text(text = value, fontWeight = FontWeight.Bold, fontSize = 16.sp)
        }
        IconButton(onClick = {
            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText(label, value)
            clipboard.setPrimaryClip(clip)
            Toast.makeText(context, "Copiado", Toast.LENGTH_SHORT).show()
        }) {
            Icon(imageVector = Icons.Default.ContentCopy, contentDescription = "Copiar", tint = Color.Gray)
        }
    }
}