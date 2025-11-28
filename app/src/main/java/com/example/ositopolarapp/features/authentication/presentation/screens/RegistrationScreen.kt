package com.example.ositopolarapp.features.authentication.presentation.screens

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
// --- IMPORTANTE: Este es el DTO que daba problemas ---
import com.example.ositopolarapp.features.authentication.data.dto.CompleteRegistrationRequest
import com.example.ositopolarapp.features.authentication.presentation.state.RegistrationViewModel

@Composable
fun RegistrationScreen(
    viewModel: RegistrationViewModel,
    planId: Int,
    userType: String,
    onRegistrationSuccess: (String, String) -> Unit // (Opcional, ahora usamos Stripe)
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    // --- VARIABLES DE ESTADO DEL FORMULARIO ---
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }

    var street by remember { mutableStateOf("") }
    var number by remember { mutableStateOf("") }
    var city by remember { mutableStateOf("") }
    var postalCode by remember { mutableStateOf("") } // En la UI le llamamos postalCode
    var country by remember { mutableStateOf("") }

    var companyName by remember { mutableStateOf("") }
    var taxId by remember { mutableStateOf("") }

    // --- EFECTO: ABRIR NAVEGADOR PARA PAGAR (STRIPE) ---
    LaunchedEffect(uiState.checkoutUrl) {
        uiState.checkoutUrl?.let { url ->
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            context.startActivity(intent)
            viewModel.clearCheckoutUrl()
        }
    }

    // --- EFECTO: MOSTRAR ERRORES ---
    LaunchedEffect(uiState.error) {
        uiState.error?.let {
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
        }
    }

    Scaffold { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Registro ($userType)",
                style = MaterialTheme.typography.headlineMedium
            )
            Spacer(modifier = Modifier.height(16.dp))

            // --- CAMPOS DE TEXTO ---
            OutlinedTextField(
                value = firstName,
                onValueChange = { firstName = it },
                label = { Text("Nombre") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = lastName,
                onValueChange = { lastName = it },
                label = { Text("Apellido") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = street,
                onValueChange = { street = it },
                label = { Text("Calle") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))

            Row(modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value = number,
                    onValueChange = { number = it },
                    label = { Text("Número") },
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(8.dp))
                OutlinedTextField(
                    value = postalCode,
                    onValueChange = { postalCode = it },
                    label = { Text("C. Postal") },
                    modifier = Modifier.weight(1f)
                )
            }
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = city,
                onValueChange = { city = it },
                label = { Text("Ciudad") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = country,
                onValueChange = { country = it },
                label = { Text("País") },
                modifier = Modifier.fillMaxWidth()
            )

            // Campos extra para Proveedores
            if (userType == "Provider") {
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = companyName,
                    onValueChange = { companyName = it },
                    label = { Text("Nombre Empresa") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = taxId,
                    onValueChange = { taxId = it },
                    label = { Text("RUC / Tax ID") },
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // --- BOTÓN DE ACCIÓN ---
            Button(
                onClick = {
                    if (firstName.isBlank() || email.isBlank()) {
                        Toast.makeText(context, "Completa los campos obligatorios", Toast.LENGTH_SHORT).show()
                        return@Button
                    }

                    // --- AQUÍ ESTÁ LA CORRECCIÓN ---
                    val formData = CompleteRegistrationRequest(
                        firstName = firstName,
                        lastName = lastName,
                        email = email,
                        street = street,
                        number = number,
                        city = city,

                        // MAPEO CORRECTO: La variable de UI 'postalCode' va al campo 'zipCode' del DTO
                        zipCode = postalCode,

                        // AGREGADO: Pasamos el país
                        country = country,

                        // Campos opcionales
                        companyName = if (userType == "Provider") companyName else null,
                        taxId = if (userType == "Provider") taxId else null,

                        // IDs necesarios para el flujo
                        planId = planId,
                        userType = userType
                    )

                    // Enviamos al ViewModel
                    viewModel.createCheckout(planId, userType, formData)
                },
                enabled = !uiState.isLoading,
                modifier = Modifier.fillMaxWidth().height(50.dp)
            ) {
                if (uiState.isLoading) {
                    CircularProgressIndicator(color = MaterialTheme.colorScheme.onPrimary)
                } else {
                    Text("Ir a Pagar")
                }
            }
        }
    }
}