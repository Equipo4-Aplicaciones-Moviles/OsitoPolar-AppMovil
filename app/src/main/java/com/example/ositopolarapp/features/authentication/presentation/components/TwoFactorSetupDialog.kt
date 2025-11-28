package com.example.ositopolarapp.features.authentication.presentation.components

import android.graphics.BitmapFactory
import android.util.Base64
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

@Composable
fun TwoFactorSetupDialog(
    qrCodeDataUrl: String?,
    isVerifying: Boolean, // Recibe el estado de carga
    onVerify: (String) -> Unit, // Recibe la función para verificar
    onDismiss: () -> Unit // Recibe la función para cerrar
) {
    var code by remember { mutableStateOf("") }

    // Decodificar Base64 a Imagen
    val qrBitmap = remember(qrCodeDataUrl) {
        try {
            if (!qrCodeDataUrl.isNullOrEmpty()) {
                // Eliminar el prefijo "data:image/png;base64," si existe
                val pureBase64 = qrCodeDataUrl.substringAfter(",")
                val decodedString = Base64.decode(pureBase64, Base64.DEFAULT)
                BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size).asImageBitmap()
            } else null
        } catch (e: Exception) { null }
    }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = MaterialTheme.shapes.medium,
            modifier = Modifier.fillMaxWidth().padding(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Configurar 2FA",
                    style = MaterialTheme.typography.headlineSmall
                )
                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Escanea este código QR con tu app de autenticación (Google Auth, Authy, etc).",
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.height(16.dp))

                // Mostrar QR
                if (qrBitmap != null) {
                    Image(
                        bitmap = qrBitmap,
                        contentDescription = "QR Code",
                        modifier = Modifier.size(200.dp)
                    )
                } else {
                    Box(modifier = Modifier.size(200.dp), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = code,
                    onValueChange = { if (it.length <= 6) code = it },
                    label = { Text("Código de 6 dígitos") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(24.dp))

                if (isVerifying) {
                    CircularProgressIndicator()
                } else {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        TextButton(onClick = onDismiss) {
                            Text("Cancelar")
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Button(
                            onClick = { onVerify(code) },
                            enabled = code.length == 6
                        ) {
                            Text("Verificar y Activar")
                        }
                    }
                }
            }
        }
    }
}