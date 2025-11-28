package com.example.ositopolarapp.features.authentication.presentation.screens

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ositopolarapp.features.authentication.presentation.components.TwoFactorVerificationDialog
import com.example.ositopolarapp.features.authentication.presentation.state.LoginViewModel
import com.example.ositopolarapp.ui.theme.OsitoBluePrimary

@Composable
fun LoginScreen(
    viewModel: LoginViewModel,
    onLoginSuccess: () -> Unit,
    onGoToRegister: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    // --- EFECTOS ---

    // 1. Manejo de Errores
    LaunchedEffect(uiState.error) {
        uiState.error?.let {
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
        }
    }

    // 2. Navegación al tener éxito
    LaunchedEffect(uiState.isSuccess) {
        if (uiState.isSuccess) {
            onLoginSuccess()
        }
    }

    Scaffold { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Iniciar Sesión",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = OsitoBluePrimary
                )

                Spacer(modifier = Modifier.height(32.dp))

                // CAMPO USUARIO
                OutlinedTextField(
                    value = username,
                    onValueChange = { username = it },
                    label = { Text("Usuario") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(16.dp))

                // CAMPO CONTRASEÑA
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Contraseña") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    trailingIcon = {
                        val image = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Icon(imageVector = image, contentDescription = null)
                        }
                    }
                )

                Spacer(modifier = Modifier.height(24.dp))

                // BOTÓN LOGIN
                Button(
                    onClick = { viewModel.signIn(username, password) },
                    enabled = !uiState.isLoading && username.isNotBlank() && password.isNotBlank(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = OsitoBluePrimary)
                ) {
                    if (uiState.isLoading) {
                        CircularProgressIndicator(color = MaterialTheme.colorScheme.onPrimary, modifier = Modifier.size(24.dp))
                    } else {
                        Text("Entrar", fontSize = 18.sp)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // LINK REGISTRO
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("¿No tienes cuenta? ")
                    Text(
                        text = "Regístrate aquí",
                        color = OsitoBluePrimary,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.clickable { onGoToRegister() }
                    )
                }
            }
        }

        // --- DIÁLOGOS ---

        // Diálogo de Verificación 2FA (Se muestra si el login pide 2FA)
        if (uiState.requires2FA) {
            TwoFactorVerificationDialog(
                isLoading = uiState.isLoading,
                error = uiState.error,
                onVerify = { code -> viewModel.verify2FA(code) },
                onDismiss = { /* Opcional: Cancelar login o limpiar estado */ }
            )
        }
    }
}