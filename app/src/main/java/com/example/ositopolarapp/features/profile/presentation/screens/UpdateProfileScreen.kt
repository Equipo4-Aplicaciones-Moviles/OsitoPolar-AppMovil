package com.example.ositopolarapp.features.profile.presentation.screens

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.ositopolarapp.features.profile.data.api.ProfileApiService
import com.example.ositopolarapp.features.profile.data.dto.ProfileDto
import com.example.ositopolarapp.features.profile.data.dto.UpdateProfileRequest
import kotlinx.coroutines.launch
import retrofit2.Response

/**
 * Update Profile Screen
 *
 * Allows users to edit their profile information
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpdateProfileScreen(
    profileApiService: ProfileApiService,
    profileId: Int,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var isLoading by remember { mutableStateOf(true) }
    var isSaving by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf<String?>(null) }

    // Profile fields
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var street by remember { mutableStateOf("") }
    var number by remember { mutableStateOf("") }
    var city by remember { mutableStateOf("") }
    var postalCode by remember { mutableStateOf("") }
    var country by remember { mutableStateOf("") }

    // Load profile data
    LaunchedEffect(profileId) {
        scope.launch {
            try {
                isLoading = true
                val response = profileApiService.getProfileById(profileId)
                if (response.isSuccessful && response.body() != null) {
                    val profile = response.body()!!
                    firstName = profile.firstName
                    lastName = profile.lastName
                    email = profile.email
                    street = profile.street
                    number = profile.number
                    city = profile.city
                    postalCode = profile.postalCode
                    country = profile.country
                } else {
                    error = "Error al cargar el perfil"
                }
            } catch (e: Exception) {
                error = "Error: ${e.message}"
            } finally {
                isLoading = false
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Editar Perfil",
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Volver"
                        )
                    }
                }
            )
        },
        modifier = modifier
    ) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Error message
                    if (error != null) {
                        Card(
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.errorContainer
                            )
                        ) {
                            Text(
                                text = error!!,
                                modifier = Modifier.padding(16.dp),
                                color = MaterialTheme.colorScheme.onErrorContainer
                            )
                        }
                    }

                    // Personal Information Section
                    Text(
                        text = "Información Personal",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )

                    OutlinedTextField(
                        value = firstName,
                        onValueChange = { firstName = it },
                        label = { Text("Nombre") },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = !isSaving
                    )

                    OutlinedTextField(
                        value = lastName,
                        onValueChange = { lastName = it },
                        label = { Text("Apellido") },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = !isSaving
                    )

                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text("Email") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                        modifier = Modifier.fillMaxWidth(),
                        enabled = !isSaving
                    )

                    Divider()

                    // Address Section
                    Text(
                        text = "Dirección",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )

                    OutlinedTextField(
                        value = street,
                        onValueChange = { street = it },
                        label = { Text("Calle") },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = !isSaving
                    )

                    OutlinedTextField(
                        value = number,
                        onValueChange = { number = it },
                        label = { Text("Número") },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = !isSaving
                    )

                    OutlinedTextField(
                        value = city,
                        onValueChange = { city = it },
                        label = { Text("Ciudad") },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = !isSaving
                    )

                    OutlinedTextField(
                        value = postalCode,
                        onValueChange = { postalCode = it },
                        label = { Text("Código Postal") },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = !isSaving
                    )

                    OutlinedTextField(
                        value = country,
                        onValueChange = { country = it },
                        label = { Text("País") },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = !isSaving
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Save Button
                    Button(
                        onClick = {
                            if (firstName.isBlank() || lastName.isBlank() || email.isBlank()) {
                                Toast.makeText(
                                    context,
                                    "Por favor completa todos los campos requeridos",
                                    Toast.LENGTH_SHORT
                                ).show()
                                return@Button
                            }

                            scope.launch {
                                try {
                                    isSaving = true
                                    error = null

                                    val request = UpdateProfileRequest(
                                        firstName = firstName,
                                        lastName = lastName,
                                        email = email,
                                        street = street,
                                        number = number,
                                        city = city,
                                        postalCode = postalCode,
                                        country = country
                                    )

                                    val response = profileApiService.updateProfile(profileId, request)
                                    if (response.isSuccessful) {
                                        Toast.makeText(
                                            context,
                                            "Perfil actualizado correctamente",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        onNavigateBack()
                                    } else {
                                        error = "Error al actualizar: ${response.message()}"
                                    }
                                } catch (e: Exception) {
                                    error = "Error: ${e.message}"
                                } finally {
                                    isSaving = false
                                }
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        enabled = !isSaving
                    ) {
                        if (isSaving) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(24.dp),
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                        } else {
                            Text("Guardar Cambios")
                        }
                    }
                }
            }
        }
    }
}
