package com.example.ositopolarapp.features.authentication.presentation.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.ositopolarapp.ui.composables.OsitoPolarFooter
import com.example.ositopolarapp.ui.composables.OsitoPolarTopBar
import com.example.ositopolarapp.ui.theme.OsitoPolarAppTheme

@Composable
fun ClientLoginScreen(
    onLoginClicked: (String, String) -> Unit, // Devuelve usuario y pass
    onRegisterClicked: () -> Unit
) {
    // Estados 'falsos' solo para que la UI funcione y podamos escribir
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    Scaffold(
        topBar = {
            // 1. Llamamos al TopBar importado
            OsitoPolarTopBar(onMenuClicked = { /* TODO: Abrir menú lateral */ })
        },
        bottomBar = {
            // 2. Llamamos al Footer importado
            OsitoPolarFooter()
        }
    ) { innerPadding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(32.dp)
                .padding(vertical = 64.dp)// Padding general de la pantalla
                .background(color = MaterialTheme.colorScheme.surfaceVariant)
        ) {

            Card(
                modifier = Modifier
                    //.align(Alignment.Center) // Centramos la tarjeta
                    .fillMaxWidth()
                    .align(Alignment.Center)
                    // Damos un padding horizontal para que no toque los bordes
                    .padding(horizontal = 16.dp)
                    .padding(vertical = 16.dp),
                colors = CardDefaults.cardColors(
                    // Usamos el color 'surface' (F2 EBEFF5)
                    containerColor = MaterialTheme.colorScheme.surface,
                    contentColor = MaterialTheme.colorScheme.surface
                ),
                // Añadimos el borde (F1 CFD8E8)
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.surfaceVariant)
            )
            {

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {

                    Text(
                        text = "Sign In",
                        style = MaterialTheme.typography.headlineLarge,
                        color = MaterialTheme.colorScheme.secondary,
                        modifier = Modifier.padding(bottom = 32.dp)
                    )

                    // Campo de Usuario
                    OutlinedTextField(
                        value = username,
                        onValueChange = { username = it },
                        label = { Text("Username") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        colors = TextFieldDefaults.colors(
                            unfocusedContainerColor = MaterialTheme.colorScheme.outlineVariant,
                            focusedContainerColor = MaterialTheme.colorScheme.outlineVariant,
                            focusedIndicatorColor = MaterialTheme.colorScheme.primary,
                            unfocusedIndicatorColor = MaterialTheme.colorScheme.surfaceVariant,
                            focusedLabelColor = MaterialTheme.colorScheme.primary,
                            unfocusedLabelColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                            focusedTextColor = MaterialTheme.colorScheme.onSurface,
                            unfocusedTextColor = MaterialTheme.colorScheme.onSurface
                        )
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Campo de Contraseña
                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = { Text("Password") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        visualTransformation = PasswordVisualTransformation(), // Oculta el texto
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        colors = TextFieldDefaults.colors(
                            unfocusedContainerColor = MaterialTheme.colorScheme.outlineVariant,
                            focusedContainerColor = MaterialTheme.colorScheme.outlineVariant,
                            focusedIndicatorColor = MaterialTheme.colorScheme.primary,
                            unfocusedIndicatorColor = MaterialTheme.colorScheme.surfaceVariant,
                            focusedLabelColor = MaterialTheme.colorScheme.primary,
                            unfocusedLabelColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                            focusedTextColor = MaterialTheme.colorScheme.onSurface,
                            unfocusedTextColor = MaterialTheme.colorScheme.onSurface
                        )
                    )

                    Spacer(modifier = Modifier.height(32.dp))

                    // Botón de Sign In
                    Button(
                        onClick = { onLoginClicked(username, password) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(text = "Sign In")
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Texto para registrarse
                    Text(
                        text = "Don't have an account?"
                    )

                    TextButton(onClick = onRegisterClicked) {
                        Text("Register")
                    }
                }


            }
        }
    }
}

@Preview
@Composable
fun SimpleComposablePreview() {
    OsitoPolarAppTheme {
        ClientLoginScreen(
            // 1. Pasa una lambda vacía que acepta dos strings
            onLoginClicked = { username, password ->
                // En un preview, esto se deja vacío o se puede
                // imprimir a la consola para depurar:
                // Log.d("Preview", "User: $username, Pass: $password")
            },
            // 2. Pasa una lambda vacía simple
            onRegisterClicked = {
                // Vacío para el preview
            }
        )
    }
    // 3. Quita el ': Unit' de aquí, no va en una llamada de función
}
