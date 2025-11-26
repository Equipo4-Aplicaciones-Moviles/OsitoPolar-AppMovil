package com.example.ositopolarapp.features.authentication.presentation.screens

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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
    onRegistrationSuccess: (username: String, password: String) -> Unit,
    onSignInClicked: () -> Unit
) {
    var currentStep by remember { mutableStateOf<RegistrationStep>(RegistrationStep.PersonalInfo) }
    var isLoading by remember { mutableStateOf(false) }

    // --- CONTROLADORES Y ESTADO ---
    val name = remember { mutableStateOf("") }
    val lastName = remember { mutableStateOf("") }
    val email = remember { mutableStateOf("") }
    // Eliminado: companyName

    val street = remember { mutableStateOf("") }
    val number = remember { mutableStateOf("") }
    val zipCode = remember { mutableStateOf("") }
    val city = remember { mutableStateOf("") }
    val country = remember { mutableStateOf("") }

    val isStep1Valid = remember { derivedStateOf { name.value.isNotEmpty() && lastName.value.isNotEmpty() && email.value.contains('@') } }
    val isStep2Valid = remember { derivedStateOf { street.value.isNotEmpty() && number.value.isNotEmpty() && city.value.isNotEmpty() && country.value.isNotEmpty() } }

    val gradientBrush = remember {
        Brush.verticalGradient(
            colors = listOf(OsitoBackground.copy(alpha = 0.3f), Color.White)
        )
    }

    // --- LÓGICA DE ACCIÓN ---
    val onNextPressed: () -> Unit = {
        if (currentStep == RegistrationStep.PersonalInfo && isStep1Valid.value) {
            currentStep = RegistrationStep.Address
        }
    }

    val onSubmitPressed: () -> Unit = {
        if (currentStep == RegistrationStep.Address && isStep2Valid.value) {
            isLoading = true
            // Simulación de API
            val tempPassword = "Client-${System.currentTimeMillis().toString().substring(8)}"
            val tempUsername = email.value.ifEmpty { name.value }

            onRegistrationSuccess(tempUsername, tempPassword)
            isLoading = false
        }
    }

    Scaffold { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(gradientBrush)
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 30.dp, vertical = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                Text(
                    "Crea una cuenta",
                    textAlign = TextAlign.Center,
                    style = TextStyle(
                        fontSize = 28.sp,
                        fontWeight = FontWeight.W900,
                        color = Color.Black,
                        letterSpacing = (-0.5).sp,
                    ),
                )
                Spacer(modifier = Modifier.height(10.dp))

                // Login Link
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "Ya tienes una cuenta ",
                        style = TextStyle(fontSize = 16.sp, color = Color(0xFF667085)),
                    )
                    Text(
                        "Login",
                        modifier = Modifier.clickable(onClick = onSignInClicked),
                        style = TextStyle(
                            fontSize = 16.sp,
                            color = OsitoBluePrimary,
                        ),
                    )
                }

                Spacer(modifier = Modifier.height(40.dp))

                // Stepper Visual
                _buildStepper(currentStep)

                Spacer(modifier = Modifier.height(40.dp))

                // Formularios
                AnimatedContent(targetState = currentStep, label = "FormStepAnimation") { step ->
                    when (step) {
                        // ✅ CORRECCIÓN: Ya no pasamos companyName
                        RegistrationStep.PersonalInfo -> _buildStep1Form(name, lastName, email)
                        RegistrationStep.Address -> _buildStep2Form(street, number, zipCode, city, country)
                    }
                }

                Spacer(modifier = Modifier.height(40.dp))

                // Botón de Acción
                Button(
                    onClick = when (currentStep) {
                        RegistrationStep.PersonalInfo -> onNextPressed
                        RegistrationStep.Address -> onSubmitPressed
                    },
                    enabled = !isLoading && when (currentStep) {
                        RegistrationStep.PersonalInfo -> isStep1Valid.value
                        RegistrationStep.Address -> isStep2Valid.value
                    },
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = OsitoBluePrimary,
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(100.dp)
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(color = Color.White, strokeWidth = 2.dp, modifier = Modifier.size(24.dp))
                    } else {
                        Text(
                            if (currentStep == RegistrationStep.PersonalInfo) "Siguiente" else "Registrar",
                            style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Bold),
                        )
                    }
                }

                Spacer(modifier = Modifier.height(30.dp))

                // Footer Link
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "¿No tienes cuenta? ",
                        style = TextStyle(color = Color(0xFF667085), fontSize = 14.sp)
                    )
                    Text(
                        "Regístrate",
                        style = TextStyle(color = OsitoBluePrimary, fontWeight = FontWeight.W400, fontSize = 14.sp),
                        modifier = Modifier.clickable(onClick = onSignInClicked)
                    )
                }
                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }
}

// --- WIDGETS AUXILIARES ---

@Composable
fun _buildStepper(currentStep: RegistrationStep) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        _buildStepItem(RegistrationStep.PersonalInfo, currentStep.number)

        Spacer(modifier = Modifier
            .weight(1f)
            .height(2.dp)
            .background(OsitoBluePrimary)
            .padding(top = 40.dp))

        _buildStepItem(RegistrationStep.Address, currentStep.number)
    }
}

@Composable
fun _buildStepItem(step: RegistrationStep, currentStepNumber: Int) {
    val isActive = currentStepNumber >= step.number
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            step.title,
            textAlign = TextAlign.Center,
            style = TextStyle(
                fontSize = 12.sp,
                color = OsitoBluePrimary,
                fontWeight = FontWeight.W500,
            ),
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
                style = TextStyle(
                    color = OsitoBluePrimary,
                    fontWeight = if (isActive) FontWeight.W900 else FontWeight.Bold,
                    fontSize = 14.sp,
                ),
            )
        }
    }
}

// --- FORMULARIOS ---

// ✅ CORRECCIÓN: Eliminado el parámetro y campo de companyName
@Composable
fun _buildStep1Form(name: MutableState<String>, lastName: MutableState<String>, email: MutableState<String>) {
    Column(modifier = Modifier.fillMaxWidth()) {
        OsitoLabel("Nombres")
        Spacer(modifier = Modifier.height(8.dp))
        OsitoTextField(
            value = name.value,
            onValueChange = { name.value = it },
            hintText = "Ej. Oliver",
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(20.dp))

        OsitoLabel("Apellido")
        Spacer(modifier = Modifier.height(8.dp))
        OsitoTextField(
            value = lastName.value,
            onValueChange = { lastName.value = it },
            hintText = "Ej. Smith",
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(20.dp))

        OsitoLabel("Email")
        Spacer(modifier = Modifier.height(8.dp))
        OsitoTextField(
            value = email.value,
            onValueChange = { email.value = it },
            hintText = "ejemplo@correo.com",
            keyboardType = KeyboardType.Email,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
fun _buildStep2Form(street: MutableState<String>, number: MutableState<String>, zipCode: MutableState<String>, city: MutableState<String>, country: MutableState<String>) {
    Column(modifier = Modifier.fillMaxWidth()) {
        OsitoLabel("Calle")
        Spacer(modifier = Modifier.height(8.dp))
        OsitoTextField(
            value = street.value,
            onValueChange = { street.value = it },
            hintText = "Av. Principal",
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(20.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(20.dp)) {
            Column(modifier = Modifier.weight(1f)) {
                OsitoLabel("Número")
                Spacer(modifier = Modifier.height(8.dp))
                OsitoTextField(
                    value = number.value,
                    onValueChange = { number.value = it },
                    hintText = "123",
                    keyboardType = KeyboardType.Number,
                    modifier = Modifier.fillMaxWidth()
                )
            }
            Column(modifier = Modifier.weight(1f)) {
                OsitoLabel("C. Postal")
                Spacer(modifier = Modifier.height(8.dp))
                OsitoTextField(
                    value = zipCode.value,
                    onValueChange = { zipCode.value = it },
                    hintText = "15001",
                    keyboardType = KeyboardType.Number,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
        Spacer(modifier = Modifier.height(20.dp))

        OsitoLabel("Ciudad")
        Spacer(modifier = Modifier.height(8.dp))
        OsitoTextField(
            value = city.value,
            onValueChange = { city.value = it },
            hintText = "Lima",
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(20.dp))

        OsitoLabel("País")
        Spacer(modifier = Modifier.height(8.dp))
        OsitoTextField(
            value = country.value,
            onValueChange = { country.value = it },
            hintText = "Perú",
            modifier = Modifier.fillMaxWidth()
        )
    }
}