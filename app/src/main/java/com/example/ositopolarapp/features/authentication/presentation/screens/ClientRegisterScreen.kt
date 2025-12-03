package com.example.ositopolarapp.features.authentication.presentation.screens

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ositopolarapp.features.authentication.data.dto.CompleteRegistrationRequest
import com.example.ositopolarapp.features.authentication.presentation.state.RegistrationViewModel
import com.example.ositopolarapp.navigation.ui.composables.OsitoLabel
import com.example.ositopolarapp.navigation.ui.composables.OsitoTextField
import com.example.ositopolarapp.ui.theme.OsitoBluePrimary
import com.example.ositopolarapp.ui.theme.OsitoBackground

sealed class RegistrationStep(val number: Int, val title: String) {
    data object PersonalInfo : RegistrationStep(1, "Info Personal")
    data object Address : RegistrationStep(2, "Dirección")
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun ClientRegisterScreen(
    viewModel: RegistrationViewModel, // Inyectamos el ViewModel real
    planId: Int,                      // Recibido de la pantalla anterior
    userType: String,                 // Recibido de la pantalla anterior
    onSignInClicked: () -> Unit
) {
    // 1. Observar el estado del ViewModel
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    var currentStep by remember { mutableStateOf<RegistrationStep>(RegistrationStep.PersonalInfo) }

    // --- VARIABLES DEL FORMULARIO ---
    val name = remember { mutableStateOf("") }
    val lastName = remember { mutableStateOf("") }
    val email = remember { mutableStateOf("") }
    val username = remember { mutableStateOf("") }

    val street = remember { mutableStateOf("") }
    val number = remember { mutableStateOf("") }
    val zipCode = remember { mutableStateOf("") }
    val city = remember { mutableStateOf("") }
    val country = remember { mutableStateOf("") }

    // Validaciones
    val isStep1Valid = remember { derivedStateOf { name.value.isNotEmpty() && lastName.value.isNotEmpty() && email.value.contains('@') && username.value.isNotEmpty() } }
    val isStep2Valid = remember { derivedStateOf { street.value.isNotEmpty() && number.value.isNotEmpty() && city.value.isNotEmpty() && country.value.isNotEmpty() } }

    // 2. EFECTO: ABRIR NAVEGADOR PARA PAGAR (STRIPE)
    LaunchedEffect(uiState.checkoutUrl) {
        uiState.checkoutUrl?.let { url ->
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            context.startActivity(intent)
            viewModel.clearCheckoutUrl() // Limpiar para no reabrir al volver
        }
    }

    // 3. EFECTO: MANEJO DE ERRORES
    LaunchedEffect(uiState.error) {
        uiState.error?.let {
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
        }
    }

    val gradientBrush = remember {
        Brush.verticalGradient(colors = listOf(OsitoBackground.copy(alpha = 0.3f), Color.White))
    }

    // --- LÓGICA DE BOTONES ---
    val onNextPressed: () -> Unit = {
        if (currentStep == RegistrationStep.PersonalInfo && isStep1Valid.value) {
            currentStep = RegistrationStep.Address
        }
    }

    val onPayPressed: () -> Unit = {
        if (currentStep == RegistrationStep.Address && isStep2Valid.value) {
            // Empaquetar los datos en el DTO que acabamos de crear
            val formData = CompleteRegistrationRequest(
                firstName = name.value,
                lastName = lastName.value,
                email = email.value,
                username = username.value,
                street = street.value,
                number = number.value,
                zipCode = zipCode.value,
                city = city.value,
                country = country.value,
                planId = planId,
                userType = userType
            )
            // Llamar al ViewModel para iniciar el pago
            viewModel.createCheckout(planId, userType, formData)
        }
    }

    Scaffold { paddingValues ->
        Box(modifier = Modifier.fillMaxSize().background(Color.White)) {
            Box(modifier = Modifier.fillMaxSize().background(gradientBrush))

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 30.dp, vertical = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    "Crea una cuenta",
                    textAlign = TextAlign.Center,
                    style = TextStyle(fontSize = 28.sp, fontWeight = FontWeight.W900, color = Color.Black),
                )

                // Link Login
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Ya tienes una cuenta ", style = TextStyle(fontSize = 16.sp, color = Color(0xFF667085)))
                    Text(
                        "Login",
                        modifier = Modifier.clickable(onClick = onSignInClicked),
                        style = TextStyle(fontSize = 16.sp, color = OsitoBluePrimary)
                    )
                }

                Spacer(modifier = Modifier.height(40.dp))

                // Stepper Visual
                _buildStepper(currentStep)

                Spacer(modifier = Modifier.height(40.dp))

                // Formularios
                AnimatedContent(targetState = currentStep, label = "FormStepAnimation") { step ->
                    when (step) {
                        RegistrationStep.PersonalInfo -> _buildStep1Form(name, lastName, email, username)
                        RegistrationStep.Address -> _buildStep2Form(street, number, zipCode, city, country)
                    }
                }

                Spacer(modifier = Modifier.height(40.dp))

                // BOTÓN PRINCIPAL
                Button(
                    onClick = when (currentStep) {
                        RegistrationStep.PersonalInfo -> onNextPressed
                        RegistrationStep.Address -> onPayPressed // Ahora llama a la lógica de pago
                    },
                    enabled = !uiState.isLoading && when (currentStep) {
                        RegistrationStep.PersonalInfo -> isStep1Valid.value
                        RegistrationStep.Address -> isStep2Valid.value
                    },
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = OsitoBluePrimary),
                    shape = RoundedCornerShape(100.dp)
                ) {
                    if (uiState.isLoading) {
                        CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                    } else {
                        Text(
                            if (currentStep == RegistrationStep.PersonalInfo) "Siguiente" else "Ir a Pagar",
                            style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Bold),
                        )
                    }
                }

                Spacer(modifier = Modifier.height(30.dp))
            }
        }
    }
}

// ----------------------------------------------------------------------------
// COMPONENTES UI (Copia estos tal cual si ya los tenías, los incluyo por si acaso)
// ----------------------------------------------------------------------------

@Composable
fun _buildStepper(currentStep: RegistrationStep) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        _buildStepItem(RegistrationStep.PersonalInfo, currentStep.number)
        Spacer(modifier = Modifier.weight(1f).height(2.dp).background(OsitoBluePrimary).padding(top = 40.dp))
        _buildStepItem(RegistrationStep.Address, currentStep.number)
    }
}

@Composable
fun _buildStepItem(step: RegistrationStep, currentStepNumber: Int) {
    val isActive = currentStepNumber >= step.number
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            step.title,
            textAlign = TextAlign.Center,
            style = TextStyle(fontSize = 12.sp, color = OsitoBluePrimary, fontWeight = FontWeight.W500),
            modifier = Modifier.width(80.dp)
        )
        Spacer(modifier = Modifier.height(10.dp))
        Box(
            modifier = Modifier
                .size(32.dp)
                .background(Color.White, CircleShape)
                .border(2.dp, OsitoBluePrimary, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(
                step.number.toString(),
                style = TextStyle(color = OsitoBluePrimary, fontWeight = if (isActive) FontWeight.W900 else FontWeight.Bold, fontSize = 14.sp)
            )
        }
    }
}

@Composable
fun _buildStep1Form(name: MutableState<String>, lastName: MutableState<String>, email: MutableState<String>, username: MutableState<String>) {
    Column(modifier = Modifier.fillMaxWidth()) {
        OsitoLabel("Nombres")
        OsitoTextField(value = name.value, onValueChange = { name.value = it }, hintText = "Ej. Oliver", modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(20.dp))
        OsitoLabel("Apellido")
        OsitoTextField(value = lastName.value, onValueChange = { lastName.value = it }, hintText = "Ej. Smith", modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(20.dp))
        OsitoLabel("Email")
        OsitoTextField(value = email.value, onValueChange = { email.value = it }, hintText = "ejemplo@correo.com", keyboardType = KeyboardType.Email, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(20.dp))
        OsitoLabel("Nombre de Usuario")
        OsitoTextField(value = username.value, onValueChange = { username.value = it }, hintText = "Ej. oliver_smith", modifier = Modifier.fillMaxWidth())
    }
}

@Composable
fun _buildStep2Form(street: MutableState<String>, number: MutableState<String>, zipCode: MutableState<String>, city: MutableState<String>, country: MutableState<String>) {
    Column(modifier = Modifier.fillMaxWidth()) {
        OsitoLabel("Calle")
        OsitoTextField(value = street.value, onValueChange = { street.value = it }, hintText = "Av. Principal", modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(20.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(20.dp)) {
            Column(modifier = Modifier.weight(1f)) {
                OsitoLabel("Número")
                OsitoTextField(value = number.value, onValueChange = { number.value = it }, hintText = "123", keyboardType = KeyboardType.Number, modifier = Modifier.fillMaxWidth())
            }
            Column(modifier = Modifier.weight(1f)) {
                OsitoLabel("C. Postal")
                OsitoTextField(value = zipCode.value, onValueChange = { zipCode.value = it }, hintText = "15001", keyboardType = KeyboardType.Number, modifier = Modifier.fillMaxWidth())
            }
        }
        Spacer(modifier = Modifier.height(20.dp))
        OsitoLabel("Ciudad")
        OsitoTextField(value = city.value, onValueChange = { city.value = it }, hintText = "Lima", modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(20.dp))
        OsitoLabel("País")
        OsitoTextField(value = country.value, onValueChange = { country.value = it }, hintText = "Perú", modifier = Modifier.fillMaxWidth())
    }
}