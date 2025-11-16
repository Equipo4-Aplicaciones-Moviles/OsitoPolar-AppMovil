package com.example.ositopolarapp.features.authentication.presentation.screens

import android.content.Intent
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable // Importar para guardar estado en rotación
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.ositopolarapp.features.authentication.data.dto.CompleteRegistrationRequest
import com.example.ositopolarapp.features.authentication.presentation.state.RegistrationViewModel

@Composable
fun RegistrationScreen(
    viewModel: RegistrationViewModel,
    planId: Int,
    userType: String,
    onRegistrationSuccess: (username: String, password: String) -> Unit
) {
    // --- ViewModel y Estado de la UI ---
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    // 🛑 1. ESTADO LOCAL DEL FORMULARIO (Usando rememberSaveable para persistencia)
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

    // 2. Reacciona al checkoutUrl (Abrir Navegador)
    LaunchedEffect(uiState.checkoutUrl) {
        val url = uiState.checkoutUrl
        if (url != null) {
            try {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                context.startActivity(intent)
            } finally {
                viewModel.clearCheckoutUrl()
            }
        }
    }

    // 3. Reacciona al Éxito del Registro (only from registration-complete route)
    LaunchedEffect(uiState.registrationComplete) {
        if (uiState.registrationComplete &&
            uiState.generatedUsername != null &&
            uiState.generatedPassword != null) {
            Log.i("RegistrationScreen", "Registration completed successfully! Username: ${uiState.generatedUsername}")
            Toast.makeText(context, "Registro completado", Toast.LENGTH_LONG).show()
            onRegistrationSuccess(uiState.generatedUsername!!, uiState.generatedPassword!!)
        } else if (uiState.registrationComplete) {
            Log.e("RegistrationScreen", "Registration marked complete but credentials are null! Username: ${uiState.generatedUsername}, Password present: ${uiState.generatedPassword != null}")
        }
    }

    LaunchedEffect(uiState.error) {
        uiState.error?.let {
            Log.e("RegistrationScreen", "Error occurred: $it")
            Toast.makeText(context, "ERROR: $it", Toast.LENGTH_LONG).show()
        }
    }


    // --- UI (El Formulario COMPLETO) ---
    Box(modifier = Modifier.fillMaxSize()) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Crear Cuenta ($userType)", style = MaterialTheme.typography.headlineMedium)
            Spacer(Modifier.height(16.dp))

            // 🛑 CAMPOS DE INFORMACIÓN PERSONAL
            OutlinedTextField(value = username, onValueChange = { username = it }, label = { Text("Nombre de Usuario") }, modifier = Modifier.fillMaxWidth())
            Spacer(Modifier.height(8.dp))
            OutlinedTextField(value = email, onValueChange = { email = it }, label = { Text("Email") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email), modifier = Modifier.fillMaxWidth())
            Spacer(Modifier.height(8.dp))
            OutlinedTextField(value = firstName, onValueChange = { firstName = it }, label = { Text("Nombre") }, modifier = Modifier.fillMaxWidth())
            Spacer(Modifier.height(8.dp))
            OutlinedTextField(value = lastName, onValueChange = { lastName = it }, label = { Text("Apellido") }, modifier = Modifier.fillMaxWidth())

            // 🛑 CAMPOS DE DIRECCIÓN
            Spacer(Modifier.height(16.dp))
            Text("Dirección", style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(8.dp))

            OutlinedTextField(value = street, onValueChange = { street = it }, label = { Text("Calle") }, modifier = Modifier.fillMaxWidth())
            Spacer(Modifier.height(8.dp))
            OutlinedTextField(value = number, onValueChange = { number = it }, label = { Text("Número") }, modifier = Modifier.fillMaxWidth())
            Spacer(Modifier.height(8.dp))
            OutlinedTextField(value = city, onValueChange = { city = it }, label = { Text("Ciudad") }, modifier = Modifier.fillMaxWidth())
            Spacer(Modifier.height(8.dp))
            OutlinedTextField(value = postalCode, onValueChange = { postalCode = it }, label = { Text("Código Postal") }, modifier = Modifier.fillMaxWidth())
            Spacer(Modifier.height(8.dp))
            OutlinedTextField(value = country, onValueChange = { country = it }, label = { Text("País") }, modifier = Modifier.fillMaxWidth())

            // 🛑 CAMPOS DE PROVEEDOR (Conditional)
            if (userType == "Provider") {
                Spacer(Modifier.height(16.dp))
                Text("Información de la Compañía", style = MaterialTheme.typography.titleMedium)
                Spacer(Modifier.height(8.dp))

                OutlinedTextField(value = companyName, onValueChange = { companyName = it }, label = { Text("Nombre de la Compañía (Requerido)") }, modifier = Modifier.fillMaxWidth())
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(value = taxId, onValueChange = { taxId = it }, label = { Text("Tax ID (Opcional)") }, modifier = Modifier.fillMaxWidth())
            }

            Spacer(Modifier.height(24.dp))

            // --- Botón de Envío ---
            Button(
                onClick = {
                    if (username.isBlank() || email.isBlank() || firstName.isBlank() || lastName.isBlank()) {
                        Toast.makeText(context, "Completa los campos de nombre y email.", Toast.LENGTH_SHORT).show()
                        return@Button
                    }

                    // 🛑 CONSTRUCCIÓN FINAL DEL REQUEST
                    val formData = CompleteRegistrationRequest(
                        sessionId = "", // Vacío para el Paso 1
                        username = username, email = email, firstName = firstName, lastName = lastName,
                        street = street, number = number, city = city, postalCode = postalCode, country = country,
                        companyName = if (userType == "Provider") companyName else null,
                        taxId = if (userType == "Provider") taxId else null
                    )

                    viewModel.createCheckout(planId = planId, userType = userType, formData = formData)
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
                modifier = Modifier.fillMaxSize().matchParentSize()
            ) {
                CircularProgressIndicator()
            }
        }
    }
}