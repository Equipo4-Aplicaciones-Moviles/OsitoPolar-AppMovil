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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
// Eliminamos el import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ositopolarapp.features.authentication.data.dto.CompleteRegistrationRequest
import com.example.ositopolarapp.features.authentication.presentation.state.RegistrationViewModel

/**
 * Pantalla de registro.
 * Recibe el ViewModel inyectado por la AuthViewModelFactory.
 */
@Composable
fun RegistrationScreen(
    // 1. ELIMINAMOS la inicialización local y SOLO lo RECIBIMOS.
    viewModel: RegistrationViewModel,
    planId: Int,
    userType: String,
    deepLinkUri: Uri?, // Parámetro para manejar el regreso del pago
    onRegistrationSuccess: () -> Unit // Para navegar al login
) {
    // --- ViewModel y Estado de la UI ---
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    // --- Estado para todos los campos del formulario ---
    var username by rememberSaveable { mutableStateOf("") }
    var email by rememberSaveable { mutableStateOf("") }
    var firstName by rememberSaveable { mutableStateOf("") }
    var lastName by rememberSaveable { mutableStateOf("") }
    var companyName by rememberSaveable { mutableStateOf("") }
    var taxId by rememberSaveable { mutableStateOf("") }
    var street by rememberSaveable { mutableStateOf("") }
    var number by rememberSaveable { mutableStateOf("") }
    var city by rememberSaveable { mutableStateOf("") }
    var postalCode by rememberSaveable { mutableStateOf("") }
    var country by rememberSaveable { mutableStateOf("") }

    // --- Manejo de Efectos (Reacciones al Estado) ---

    // 1. Reacciona cuando la 'checkoutUrl' aparece (Abrir Navegador)
    LaunchedEffect(uiState.checkoutUrl) {
        uiState.checkoutUrl?.let { url ->
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url)).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            try {
                context.startActivity(intent)
                // Es importante limpiar la URL después de usarla para evitar reejecuciones
                // (Aunque la lógica de limpieza la manejamos mejor en el ViewModel o MainActivity,
                // la acción de abrir el navegador es la principal aquí).
                viewModel.clearCheckoutUrl()
            } catch (e: Exception) {
                Toast.makeText(context, "No se puede abrir el navegador: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // 2. Reacciona al Deep Link (Paso 2: Completar Registro)
    LaunchedEffect(deepLinkUri) {
        deepLinkUri?.let { uri ->
            // El Deep Link esperado es ositopolar://registration/success?session_id=...
            if (uri.path == "/success") {
                val sessionId = uri.getQueryParameter("session_id")

                if (sessionId != null) {
                    // Llama al ViewModel para completar el registro (Paso 2)
                    viewModel.completeRegistration(sessionId)
                } else {
                    Toast.makeText(context, "Error: Sesión de pago inválida.", Toast.LENGTH_LONG).show()
                }
            } else if (uri.path == "/cancel") {
                Toast.makeText(context, "Pago cancelado. Intenta de nuevo.", Toast.LENGTH_LONG).show()
            }
            // Después de procesar, la Activity debería limpiar el URI para que este efecto no se repita
            // (Esta limpieza ocurre en tu MainActivity, pero aquí manejamos el proceso).
        }
    }

    // 3. Reacciona cuando el registro se completa con éxito
    LaunchedEffect(uiState.registrationComplete) {
        if (uiState.registrationComplete) {
            Toast.makeText(context, "¡Registro Exitoso! Puedes iniciar sesión.", Toast.LENGTH_LONG).show()
            onRegistrationSuccess() // Navega a la pantalla de Login
        }
    }

    // 4. Reacciona si hay un error
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

            // --- Campos del Formulario (Usando rememberSaveable) ---
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
            // ... (Campos firstName, lastName, Dirección, Compañía...)
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
                    // 1. Validación básica de campos
                    if (username.isBlank() || email.isBlank() || firstName.isBlank() || lastName.isBlank()) {
                        Toast.makeText(context, "Completa los campos de nombre y email.", Toast.LENGTH_SHORT).show()
                        return@Button
                    }

                    // 2. Crear el objeto de datos del formulario
                    val formData = CompleteRegistrationRequest(
                        sessionId = "",
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

                    // 3. Llamar al ViewModel (Paso 1: Crear Checkout)
                    viewModel.createCheckout(
                        planId = planId,
                        userType = userType,
                        formData = formData
                    )
                },
                enabled = !uiState.isLoading,
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
            ) {
                CircularProgressIndicator()
            }
        }
    }
}