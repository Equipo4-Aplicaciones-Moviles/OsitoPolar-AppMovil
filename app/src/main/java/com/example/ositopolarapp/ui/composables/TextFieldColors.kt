package com.example.ositopolarapp.ui.composables

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

/**
 * Colores estándar para OutlinedTextField con mejor visibilidad de texto
 */
@Composable
fun outlinedTextFieldColors(): TextFieldColors {
    return OutlinedTextFieldDefaults.colors(
        focusedTextColor = Color(0xFF1D2939),              // Texto oscuro cuando está enfocado
        unfocusedTextColor = Color(0xFF344054),            // Texto oscuro cuando NO está enfocado
        disabledTextColor = Color(0xFF667085),             // Texto gris cuando está deshabilitado
        focusedContainerColor = Color.Transparent,         // Sin fondo
        unfocusedContainerColor = Color.Transparent,       // Sin fondo
        disabledContainerColor = Color.Transparent,        // Sin fondo
        focusedBorderColor = MaterialTheme.colorScheme.primary,
        unfocusedBorderColor = Color(0xFFD0D5DD),
        disabledBorderColor = Color(0xFFE4E7EC),
        focusedLabelColor = MaterialTheme.colorScheme.primary,
        unfocusedLabelColor = Color(0xFF667085),
        disabledLabelColor = Color(0xFF98A2B3),
        cursorColor = MaterialTheme.colorScheme.primary,
        errorTextColor = Color(0xFFB42318),
        errorBorderColor = Color(0xFFF04438),
        errorLabelColor = Color(0xFFF04438),
        errorCursorColor = Color(0xFFF04438)
    )
}

