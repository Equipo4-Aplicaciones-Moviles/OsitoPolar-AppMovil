package com.example.ositopolarapp.features.authentication.presentation.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
// Imports de tu proyecto
import com.example.ositopolarapp.navigation.ui.composables.OsitoLabel
import com.example.ositopolarapp.navigation.ui.composables.OsitoTextField
import com.example.ositopolarapp.ui.theme.OsitoBluePrimary
import com.example.ositopolarapp.ui.theme.OsitoBackground

@Composable
fun ClientLoginScreen(
    onLoginSuccess: () -> Unit,
    onRegisterClicked: () -> Unit,
    onForgotPasswordClicked: () -> Unit
    // TODO: Aquí podrías inyectar un ClientLoginViewModel si tienes lógica específica
) {
    val context = LocalContext.current

    // Estados locales para el formulario
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }

    // Simulación del degradado del fondo (El mismo estilo que el resto del onboarding)
    val gradientBrush = remember {
        Brush.verticalGradient(
            colors = listOf(
                OsitoBackground.copy(alpha = 0.3f),
                Color.White
            )
        )
    }

    Scaffold { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
        ) {
            // Fondo con degradado
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(gradientBrush)
            )

            // Contenido Principal
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 30.dp, vertical = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Spacer(modifier = Modifier.height(40.dp))

                // Títulos
                Text(
                    text = "¡Bienvenido de vuelta!",
                    textAlign = TextAlign.Center,
                    style = TextStyle(
                        fontSize = 28.sp,
                        fontWeight = FontWeight.W900,
                        color = Color.Black,
                        letterSpacing = (-0.5).sp,
                    ),
                )
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "Inicia sesión en tu cuenta",
                    textAlign = TextAlign.Center,
                    style = TextStyle(
                        fontSize = 16.sp,
                        color = Color(0xFF667085),
                        fontWeight = FontWeight.W400,
                    ),
                )
                Spacer(modifier = Modifier.height(50.dp))

                // --- FORMULARIO ---

                // Usuario
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterStart) {
                    OsitoLabel("Nombre de usuario")
                }
                Spacer(modifier = Modifier.height(8.dp))

                OsitoTextField(
                    value = username,
                    onValueChange = { username = it },
                    hintText = "Ej. juanperez",
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(24.dp))

                // Contraseña
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterStart) {
                    OsitoLabel("Contraseña")
                }
                Spacer(modifier = Modifier.height(8.dp))

                OsitoTextField(
                    value = password,
                    onValueChange = { password = it },
                    hintText = "•••••••••",
                    isPassword = !passwordVisible,
                    keyboardType = KeyboardType.Password,
                    modifier = Modifier.fillMaxWidth(),
                    trailingIcon = {
                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Icon(
                                imageVector = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                                contentDescription = if (passwordVisible) "Ocultar contraseña" else "Mostrar contraseña",
                                tint = Color(0xFF475467)
                            )
                        }
                    }
                )

                // Olvidaste contraseña
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterEnd) {
                    TextButton(onClick = onForgotPasswordClicked) {
                        Text(
                            text = "¿Olvidaste tu contraseña?",
                            style = TextStyle(
                                color = OsitoBluePrimary,
                                fontWeight = FontWeight.W400,
                                fontSize = 14.sp
                            )
                        )
                    }
                }
                Spacer(modifier = Modifier.height(30.dp))

                // Botón Login
                Button(
                    onClick = {
                        // Lógica simple de validación antes de llamar al callback
                        if (username.isNotBlank() && password.isNotBlank()) {
                            isLoading = true
                            // Aquí simularíamos la llamada a la API
                            // En una implementación real, llamarías a viewModel.login(username, password)
                            Toast.makeText(context, "Iniciando sesión...", Toast.LENGTH_SHORT).show()
                            onLoginSuccess() // Navega al éxito
                            isLoading = false
                        } else {
                            Toast.makeText(context, "Por favor llena todos los campos", Toast.LENGTH_SHORT).show()
                        }
                    },
                    enabled = !isLoading,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = OsitoBluePrimary,
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(100.dp)
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                    } else {
                        Text("Login", style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Bold))
                    }
                }

                Spacer(modifier = Modifier.height(30.dp))

                // Enlace a Registro
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "¿No tienes cuenta? ",
                        style = TextStyle(color = Color(0xFF667085), fontSize = 14.sp)
                    )
                    Text(
                        text = "Regístrate",
                        modifier = Modifier.clickable(onClick = onRegisterClicked),
                        style = TextStyle(
                            color = OsitoBluePrimary,
                            fontWeight = FontWeight.W400,
                            fontSize = 14.sp,
                        ),
                    )
                }
                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }
}