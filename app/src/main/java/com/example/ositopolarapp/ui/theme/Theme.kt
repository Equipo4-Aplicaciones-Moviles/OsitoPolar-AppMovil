package com.example.ositopolarapp.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import com.example.ositopolarapp.core.data.PreferencesManager

// Paleta OSCURA (placeholder, puedes ajustarla luego)
private val DarkColorScheme = darkColorScheme(
    primary = OsitoBluePrimary, // Mantenemos el azul brillante
    secondary = OsitoBlueTitle,
    onPrimary = OsitoWhiteLabel,
    background = Color(0xFF1C1B1F), // Fondo oscuro estándar
    surface = Color(0xFF2C2B2F),    // Superficie oscura
    onBackground = Color(0xFFE6E1E5), // Texto claro
    onSurface = Color(0xFFE6E1E5)     // Texto claro
)

// Paleta CLARA (¡Esta es la que definiste!)
private val LightColorScheme = lightColorScheme(
    primary = OsitoBluePrimary,
    onPrimary = OsitoWhiteLabel,
    secondary = OsitoBlueTitle,
    onSecondary = OsitoWhiteLabel,
    background = OsitoBackground,
    onBackground = Color(0xFF1A1C1E), // Texto oscuro estándar
    surface = OsitoSurface,
    onSurface = Color(0xFF1A1C1E), // Texto oscuro estándar
    surfaceVariant = OsitoSurfaceVariant, // Borde de la tarjeta
    outline = OsitoTextField // Color para bordes de textfield
)

@Composable
fun OsitoPolarAppTheme(
    themePreference: String = PreferencesManager.THEME_SYSTEM,
    // Dynamic color (Android 12+) lo desactivamos por defecto
    // para que SIEMPRE se vean tus colores de marca.
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val systemInDarkTheme = isSystemInDarkTheme()

    val darkTheme = when (themePreference) {
        PreferencesManager.THEME_LIGHT -> false
        PreferencesManager.THEME_DARK -> true
        else -> systemInDarkTheme // THEME_SYSTEM or default
    }

    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography, // 'Typography' viene de tu archivo Type.kt
        content = content
    )
}