package com.example.ositopolarapp.features.authentication.presentation.screens

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ositopolarapp.features.authentication.data.dto.CompleteRegistrationRequest
import com.example.ositopolarapp.features.authentication.presentation.state.RegistrationViewModel

/**
 * NOTA: Esta pantalla asume que recibe el 'planId' y 'userType'
 * desde la pantalla anterior (por ejemplo, una pantalla de selección de planes).
 */
@Composable
fun RegistrationScreen(
    viewModel: RegistrationViewModel = viewModel(), // ¡OJO! Ver nota al final
    planId: Int,
    userType: String,
    onRegistrationSuccess: () -> Unit // Para navegar al login
) {
    // --- ViewModel y Estado de la UI ---

    // TODO: Necesitarás un ViewModelFactory para inyectar los UseCases
    val viewModel: RegistrationViewModel = viewModel()

    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    // --- Estado para todos los campos del formulario ---
    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var companyName by remember { mutableStateOf("") } // Opcional
    var taxId by remember { mutableStateOf("") }       // Opcional
    var street by remember { mutableStateOf("") }
    var number by remember { mutableStateOf("") }
    var city by remember { mutableStateOf("") }
    var postalCode by remember { mutableStateOf("") }
    var country by remember { mutableStateOf("") }

    // --- Manejo de Efectos (Reacciones al Estado) ---

    // 1. Reacciona cuando la 'checkoutUrl' aparece
    LaunchedEffect(uiState.checkoutUrl) {
        uiState.checkoutUrl?.let { url ->
            // Abre la URL de pago en un navegador (Chrome Custom Tab es mejor)
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            try {
                context.startActivity(intent)
            } catch (e: Exception) {
                // Manejar error (ej. no hay navegador)
                Toast.makeText(context, "No se puede abrir el navegador", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // 2. Reacciona cuando el registro se completa con éxito
    LaunchedEffect(uiState.registrationComplete) {
        if (uiState.registrationComplete) {
            Toast.makeText(context, "¡Registro Exitoso!", Toast.LENGTH_LONG).show()
            onRegistrationSuccess() // Navega a la pantalla de Login
        }
    }

    // 3. Reacciona si hay un error
    LaunchedEffect(uiState.error) {
        uiState.error?.let { errorMsg ->
            Toast.makeText(context, errorMsg, Toast.LENGTH_LONG).show()
        }
    }


    // --- UI (El Formulario) ---
    Box(modifier = Modifier.fillMaxSize()) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()) // Permite scroll
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Crear Cuenta ($userType)", style = MaterialTheme.typography.headlineMedium)
            Spacer(Modifier.height(16.dp))

            // --- Campos del Formulario ---
            OutlinedTextField(
                value = username,
                onValueChange = { username = it },
                label = { Text("Nombre de Usuario") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(8.dp))
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                modifier = Modifier.fillMaxWidth()
            )
            // ... (Campos de firstName, lastName) ...
            Spacer(Modifier.height(8.dp))
            OutlinedTextField(
                value = firstName,
                onValueChange = { firstName = it },
                label = { Text("Nombre") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(8.dp))
            OutlinedTextField(
                value = lastName,
                onValueChange = { lastName = it },
                label = { Text("Apellido") },
                modifier = Modifier.fillMaxWidth()
            )

            // --- Campos de Dirección ---
            Spacer(Modifier.height(16.dp))
            Text("Dirección", style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(8.dp))

            OutlinedTextField(
                value = street,
                onValueChange = { street = it },
                label = { Text("Calle") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(8.dp))
            OutlinedTextField(
                value = number,
                onValueChange = { number = it },
                label = { Text("Número") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(8.dp))
            OutlinedTextField(
                value = city,
                onValueChange = { city = it },
                label = { Text("Ciudad") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(8.dp))
            OutlinedTextField(
                value = postalCode,
                onValueChange = { postalCode = it },
                label = { Text("Código Postal") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(8.dp))
            OutlinedTextField(
                value = country,
                onValueChange = { country = it },
                label = { Text("País") },
                modifier = Modifier.fillMaxWidth()
            )

            // --- Campos Opcionales de Proveedor ---
            if (userType == "Provider") {
                Spacer(Modifier.height(16.dp))
                Text("Información de la Compañía", style = MaterialTheme.typography.titleMedium)
                Spacer(Modifier.height(8.dp))

                OutlinedTextField(
                    value = companyName,
                    onValueChange = { companyName = it },
                    label = { Text("Nombre de la Compañía (Requerido)") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    value = taxId,
                    onValueChange = { taxId = it },
                    label = { Text("Tax ID (Opcional)") },
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Spacer(Modifier.height(24.dp))

            // --- Botón de Envío ---
            Button(
                onClick = {
                    // 1. Validar campos (básico)
                    if (username.isBlank() || email.isBlank() /* ...etc... */) {
                        Toast.makeText(context, "Completa todos los campos", Toast.LENGTH_SHORT).show()
                        return@Button
                    }

                    // 2. Crear el objeto de datos del formulario
                    val formData = CompleteRegistrationRequest(
                        sessionId = "", // Vacío por ahora, la API no lo usa en el Paso 1
                        username = username,
                        email = email,
                        firstName = firstName,
                        lastName = lastName,
                        street = street,
                        number = number,
                        city = city,
                        postalCode = postalCode,
                        country = country,
                        companyName = if (userType == "Provider") companyName else null,
                        taxId = if (userType == "Provider") taxId else null
                    )

                    // 3. Llamar al ViewModel (Paso 1)
                    viewModel.createCheckout(
                        planId = planId,
                        userType = userType,
                        formData = formData
                    )
                },
                enabled = !uiState.isLoading, // Deshabilita el botón si está cargando
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text("Ir a Pagar")
            }
        }

        // --- Overlay de Carga ---
        if (uiState.isLoading) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxSize()
                    .matchParentSize()
                // .background(Color.Black.copy(alpha = 0.5f)) // Fondo oscuro opcional
            ) {
                CircularProgressIndicator()
            }
        }
    }
}