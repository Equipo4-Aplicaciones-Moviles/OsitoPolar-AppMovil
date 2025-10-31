package com.example.ositopolarapp.features.authentication.ui.screen

import android.icu.text.CaseMap
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.AlignmentLine
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.ositopolarapp.ui.theme.OsitoPolarAppTheme

@Composable
fun ClientRegisterScreen(
    // Devolvemos los 3 datos cuando el usuario se registra
    onSignUpClicked: (String, String, String) -> Unit,
    // Acción para volver a la pantalla de Login
    onLoginClicked: () -> Unit
) {
    // Estados 'falsos' locales para que la UI funcione
    var fullName by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var rememberMe by remember { mutableStateOf(false) }

    Surface(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .fillMaxSize()
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
                            text = "Register",
                            style = MaterialTheme.typography.headlineLarge,
                            color = MaterialTheme.colorScheme.secondary,
                            modifier = Modifier.padding(bottom = 32.dp)
                        )

                        // Campo de Full Name
                        OutlinedTextField(
                            value = fullName,
                            onValueChange = { fullName = it },
                            label = { Text("Full name") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,

                            colors = TextFieldDefaults.colors(
                                // Fondo del campo de texto (tu color DCE3EE)
                                unfocusedContainerColor = MaterialTheme.colorScheme.outlineVariant,
                                focusedContainerColor = MaterialTheme.colorScheme.outlineVariant,

                                // --- LA CORRECCIÓN ---
                                // Color del "indicador" (borde)
                                focusedIndicatorColor = MaterialTheme.colorScheme.primary, // Azul al hacer clic
                                unfocusedIndicatorColor = MaterialTheme.colorScheme.surfaceVariant, // Borde F1 (sutil)

                                // Color del Label ("Contraseña")
                                focusedLabelColor = MaterialTheme.colorScheme.primary, // Azul al hacer clic
                                unfocusedLabelColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f), // Gris sutil

                                // Color del texto que escribe el usuario
                                focusedTextColor = MaterialTheme.colorScheme.onSurface,
                                unfocusedTextColor = MaterialTheme.colorScheme.onSurface
                            )

                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Campo de Username
                        OutlinedTextField(
                            value = username,
                            onValueChange = { username = it },
                            label = { Text("Username") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,

                            colors = TextFieldDefaults.colors(
                                // Fondo del campo de texto (tu color DCE3EE)
                                unfocusedContainerColor = MaterialTheme.colorScheme.outlineVariant,
                                focusedContainerColor = MaterialTheme.colorScheme.outlineVariant,

                                // --- LA CORRECCIÓN ---
                                // Color del "indicador" (borde)
                                focusedIndicatorColor = MaterialTheme.colorScheme.primary, // Azul al hacer clic
                                unfocusedIndicatorColor = MaterialTheme.colorScheme.surfaceVariant, // Borde F1 (sutil)

                                // Color del Label ("Contraseña")
                                focusedLabelColor = MaterialTheme.colorScheme.primary, // Azul al hacer clic
                                unfocusedLabelColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f), // Gris sutil

                                // Color del texto que escribe el usuario
                                focusedTextColor = MaterialTheme.colorScheme.onSurface,
                                unfocusedTextColor = MaterialTheme.colorScheme.onSurface
                            )
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Campo de Contraseña


                        OutlinedTextField(
                            value = password,
                            onValueChange = { password = it },
                            label = { Text("Contraseña") }, // Cambié "Password" a español
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            visualTransformation = PasswordVisualTransformation(),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),

                            // --- AQUÍ ESTÁ LA MAGIA ---

                            // 1. Misma forma que los botones
                            shape = RoundedCornerShape(12.dp),

                            // 2. Definición de colores
                            colors = TextFieldDefaults.colors(
                                // Fondo del campo de texto (tu color DCE3EE)
                                unfocusedContainerColor = MaterialTheme.colorScheme.outlineVariant,
                                focusedContainerColor = MaterialTheme.colorScheme.outlineVariant,

                                // --- LA CORRECCIÓN ---
                                // Color del "indicador" (borde)
                                focusedIndicatorColor = MaterialTheme.colorScheme.primary, // Azul al hacer clic
                                unfocusedIndicatorColor = MaterialTheme.colorScheme.surfaceVariant, // Borde F1 (sutil)

                                // Color del Label ("Contraseña")
                                focusedLabelColor = MaterialTheme.colorScheme.primary, // Azul al hacer clic
                                unfocusedLabelColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f), // Gris sutil

                                // Color del texto que escribe el usuario
                                focusedTextColor = MaterialTheme.colorScheme.onSurface,
                                unfocusedTextColor = MaterialTheme.colorScheme.onSurface
                            )
                        )


                        Spacer(modifier = Modifier.height(16.dp))

                        // Fila para el Checkbox "Remember me"
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Checkbox(
                                checked = rememberMe,
                                onCheckedChange = { rememberMe = it }
                            )
                            Text(
                                text = "Remember me",
                                modifier = Modifier.padding(start = 2.dp),
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }

                        Spacer(modifier = Modifier.height(32.dp))

                        // Botón de Sign Up
                        Button(
                            onClick = { onSignUpClicked(fullName, username, password) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text(text = "Sign Up")
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Texto para ir a Login
                        TextButton(onClick = onLoginClicked) {
                            Text("Already have an account? Login")
                        }

                    }







            }

        }

    }
}

@Preview
@Composable
fun RegisterClientPreview() {
    OsitoPolarAppTheme {
        ClientRegisterScreen(
            // 1. Pasa una lambda vacía que acepta dos strings
            onSignUpClicked = { fullname, username, password ->
                // En un preview, esto se deja vacío o se puede
                // imprimir a la consola para depurar:
                // Log.d("Preview", "User: $username, Pass: $password")
            },
            // 2. Pasa una lambda vacía simple
            onLoginClicked = {
                // Vacío para el preview
            }
        )
    }
    // 3. Quita el ': Unit' de aquí, no va en una llamada de función
}