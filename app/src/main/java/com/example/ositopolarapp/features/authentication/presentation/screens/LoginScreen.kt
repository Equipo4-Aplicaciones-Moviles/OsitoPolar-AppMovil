package com.example.ositopolarapp.features.authentication.presentation.screens

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ositopolarapp.core.di.AuthViewModelFactory
import com.example.ositopolarapp.features.authentication.presentation.state.LoginViewModel
import com.example.ositopolarapp.features.authentication.presentation.components.TwoFactorVerificationDialog
import com.example.ositopolarapp.features.authentication.presentation.components.TwoFactorSetupDialog

@Composable
fun LoginScreen(
    viewModel: LoginViewModel, // Inyectado por la Factory
    onLoginSuccess: () -> Unit,
    onGoToRegister: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    // Campos de texto
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    // --- Efectos ---
    // Observa el estado de éxito
    LaunchedEffect(uiState.loginSuccess) {
        if (uiState.loginSuccess) {
            Toast.makeText(context, "¡Bienvenido ${uiState.user?.username}!", Toast.LENGTH_SHORT).show()
            onLoginSuccess() // Navega al dashboard
        }
    }

    // Observa el estado de 2FA
    LaunchedEffect(uiState.requires2FA) {
        if (uiState.requires2FA) {
            // TODO: Abrir un diálogo/bottom-sheet para pedir el código 2FA
            Toast.makeText(context, "Se requiere 2FA", Toast.LENGTH_SHORT).show()
        }
    }

    // Observa los errores
    LaunchedEffect(uiState.error) {
        uiState.error?.let {
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
        }
    }

    // Show 2FA Setup Dialog (first login)
    if (uiState.requiresTwoFactorSetup) {
        TwoFactorSetupDialog(
            viewModel = viewModel,
            uiState = uiState,
            qrCodeDataUrl = uiState.user?.qrCodeDataUrl,
            manualKey = uiState.user?.manualEntryKey
        )
    }

    // Show 2FA Verification Dialog (subsequent logins)
    if (uiState.requires2FA && !uiState.requiresTwoFactorSetup) {
        TwoFactorVerificationDialog(viewModel = viewModel, uiState = uiState)
    }

    // --- UI ---
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("Iniciar Sesión", style = MaterialTheme.typography.headlineLarge)
            Spacer(modifier = Modifier.height(32.dp))

            OutlinedTextField(
                value = username,
                onValueChange = { username = it },
                label = { Text("Usuario") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Contraseña") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
            )
            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = { viewModel.signIn(username.trim(), password.trim()) },
                enabled = !uiState.isLoading,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                if (uiState.isLoading) {
                    CircularProgressIndicator(color = MaterialTheme.colorScheme.onPrimary)
                } else {
                    Text("Entrar")
                }
            }

            TextButton(
                onClick = onGoToRegister,
                enabled = !uiState.isLoading
            ) {
                Text("¿No tienes cuenta? Regístrate")
            }
        }
    }
}