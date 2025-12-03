package com.example.ositopolarapp.features.profile.presentation.screens

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
// IMPORTS CORRECTOS (Para no tener que escribir las clases aquí abajo)
import com.example.ositopolarapp.features.profile.data.api.ProfileApiService
import com.example.ositopolarapp.features.profile.data.dto.UpdateProfileRequest
import com.example.ositopolarapp.ui.composables.outlinedTextFieldColors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpdateProfileScreen(
    profileApiService: ProfileApiService,
    profileId: Int,
    onNavigateBack: () -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var isLoading by remember { mutableStateOf(true) }
    var isSaving by remember { mutableStateOf(false) }

    // Campos
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var street by remember { mutableStateOf("") }
    var number by remember { mutableStateOf("") }
    var city by remember { mutableStateOf("") }
    var postalCode by remember { mutableStateOf("") }
    var country by remember { mutableStateOf("") }

    // Cargar datos
    LaunchedEffect(profileId) {
        try {
            val response = profileApiService.getProfile(profileId)
            if (response.isSuccessful && response.body() != null) {
                val profile = response.body()!!
                firstName = profile.firstName ?: ""
                lastName = profile.lastName ?: ""
                email = profile.email
                street = profile.street ?: ""
                number = profile.number ?: ""
                city = profile.city ?: ""
                postalCode = profile.postalCode ?: ""
                country = profile.country ?: ""
            }
        } catch (e: Exception) {
            // Manejo de error silencioso o toast
        } finally {
            isLoading = false
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Editar Perfil") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding).fillMaxSize()) {
            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    OutlinedTextField(value = firstName, onValueChange = { firstName = it }, label = { Text("Nombre") }, modifier = Modifier.fillMaxWidth(), colors = outlinedTextFieldColors())
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(value = lastName, onValueChange = { lastName = it }, label = { Text("Apellido") }, modifier = Modifier.fillMaxWidth(), colors = outlinedTextFieldColors())
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(value = email, onValueChange = { email = it }, label = { Text("Email") }, modifier = Modifier.fillMaxWidth(), colors = outlinedTextFieldColors())

                    Spacer(modifier = Modifier.height(16.dp))
                    Text("Dirección", style = MaterialTheme.typography.titleMedium)

                    OutlinedTextField(value = street, onValueChange = { street = it }, label = { Text("Calle") }, modifier = Modifier.fillMaxWidth(), colors = outlinedTextFieldColors())
                    Spacer(modifier = Modifier.height(8.dp))

                    Row(modifier = Modifier.fillMaxWidth()) {
                        OutlinedTextField(value = number, onValueChange = { number = it }, label = { Text("Número") }, modifier = Modifier.weight(1f), colors = outlinedTextFieldColors())
                        Spacer(modifier = Modifier.width(8.dp))
                        OutlinedTextField(value = postalCode, onValueChange = { postalCode = it }, label = { Text("C. Postal") }, modifier = Modifier.weight(1f), colors = outlinedTextFieldColors())
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(value = city, onValueChange = { city = it }, label = { Text("Ciudad") }, modifier = Modifier.fillMaxWidth(), colors = outlinedTextFieldColors())
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(value = country, onValueChange = { country = it }, label = { Text("País") }, modifier = Modifier.fillMaxWidth(), colors = outlinedTextFieldColors())

                    Spacer(modifier = Modifier.height(24.dp))

                    Button(
                        onClick = {
                            scope.launch {
                                isSaving = true
                                try {
                                    val request = UpdateProfileRequest(
                                        firstName, lastName, email, street, number, city, postalCode, country
                                    )
                                    val response = profileApiService.updateProfile(profileId, request)
                                    if (response.isSuccessful) {
                                        Toast.makeText(context, "Guardado", Toast.LENGTH_SHORT).show()
                                        onNavigateBack()
                                    } else {
                                        Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show()
                                    }
                                } catch (e: Exception) {
                                    Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                                } finally {
                                    isSaving = false
                                }
                            }
                        },
                        enabled = !isSaving,
                        modifier = Modifier.fillMaxWidth().height(50.dp)
                    ) {
                        if (isSaving) CircularProgressIndicator(color = MaterialTheme.colorScheme.onPrimary) else Text("Guardar")
                    }
                }
            }
        }
    }
}
// ¡OJO! AQUÍ ABAJO NO DEBE HABER NINGUNA CLASE "data class..."