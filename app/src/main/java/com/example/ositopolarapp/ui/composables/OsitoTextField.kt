package com.example.ositopolarapp.navigation.ui.composables

import androidx.compose.foundation.layout.padding // <--- ESTA ES LA IMPORTACIÓN QUE FALTABA
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.example.ositopolarapp.ui.theme.OsitoTextField // Tu color definido en theme/Color.kt

@Composable
fun OsitoTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    hintText: String,
    keyboardType: KeyboardType = KeyboardType.Text,
    isPassword: Boolean = false,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    isError: Boolean = false
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        placeholder = { Text(hintText, color = Color.Black.copy(alpha = 0.38f)) },
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        isError = isError,
        singleLine = true,
        colors = TextFieldDefaults.colors(
            focusedContainerColor = OsitoTextField,
            unfocusedContainerColor = OsitoTextField,
            disabledContainerColor = OsitoTextField,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            errorIndicatorColor = Color.Transparent,
            focusedTextColor = Color(0xFF1D2939),        // Texto oscuro cuando está enfocado
            unfocusedTextColor = Color(0xFF344054),      // Texto oscuro cuando NO está enfocado
            disabledTextColor = Color(0xFF667085),       // Texto gris cuando está deshabilitado
        ),
        shape = RoundedCornerShape(100.dp),
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None
    )
}

@Composable
fun OsitoLabel(text: String, modifier: Modifier = Modifier) {
    Text(
        text = text,
        // Aquí es donde daba el error si no importabas 'padding'
        modifier = modifier.padding(horizontal = 10.dp),
        style = MaterialTheme.typography.bodyMedium.copy(
            color = Color(0xFF344054),
            fontWeight = FontWeight.W400
        )
    )
}