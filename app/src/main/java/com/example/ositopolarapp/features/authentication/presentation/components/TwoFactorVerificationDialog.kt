package com.example.ositopolarapp.features.authentication.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.ositopolarapp.features.authentication.presentation.state.LoginViewModel
import com.example.ositopolarapp.features.authentication.presentation.state.LoginUiState

@Composable
fun TwoFactorVerificationDialog(
    viewModel: LoginViewModel,
    uiState: LoginUiState
) {
    // 1. Estado local para el código de 6 dígitos
    var code by remember { mutableStateOf("") }

    AlertDialog(
        // Cuando el usuario presiona afuera o Atrás
        onDismissRequest = {
            if (!uiState.isVerifying2FA) {
                viewModel.dismiss2FADialog() // Llama a la función que acabamos de corregir
            }
        },

        // --- Título y Contenido ---
        title = { Text("Verificación en 2 Pasos") },
        text = {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {

                Icon(
                    imageVector = Icons.Filled.Lock,
                    contentDescription = "2FA Lock",
                    modifier = Modifier.size(48.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
                Spacer(Modifier.height(16.dp))

                Text(
                    text = "Ingresa el código de 6 dígitos de tu aplicación de autenticación (Google Authenticator, etc.).",
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(Modifier.height(16.dp))

                OutlinedTextField(
                    value = code,
                    onValueChange = {
                        // Limita a 6 caracteres y solo números
                        if (it.length <= 6 && it.all { char -> char.isDigit() }) {
                            code = it
                        }
                    },
                    label = { Text("Código 2FA") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                    singleLine = true,
                    isError = uiState.error != null,
                    modifier = Modifier.fillMaxWidth(),
                    trailingIcon = {
                        if (uiState.isVerifying2FA) CircularProgressIndicator(Modifier.size(24.dp))
                    }
                )

                // Muestra el error de código incorrecto
                if (uiState.error != null) {
                    Text(
                        text = uiState.error!!,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }
        },

        // --- Botones de Acción ---
        confirmButton = {
            Button(
                onClick = { viewModel.verify2FACode(code) },
                enabled = code.length == 6 && !uiState.isVerifying2FA
            ) {
                Text("Verificar")
            }
        },
        dismissButton = {
            TextButton(
                onClick = { viewModel.dismiss2FADialog() },
                enabled = !uiState.isVerifying2FA
            ) {
                Text("Cancelar")
            }
        }
    )
}