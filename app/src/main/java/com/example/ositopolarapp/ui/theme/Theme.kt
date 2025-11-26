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
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

// Esquema de colores oscuros (puedes ajustarlos si quieres modo oscuro)
private val DarkColorScheme = darkColorScheme(
    primary = OsitoBluePrimary,
    secondary = PurpleGrey80,
    tertiary = Pink80
)

// Esquema de colores claros (usando tus colores)
private val LightColorScheme = lightColorScheme(
    primary = OsitoBluePrimary,
    secondary = PurpleGrey40,
    tertiary = Pink40,
    background = OsitoBackground,
    surface = OsitoSurface,
    onPrimary = OsitoWhiteLabel,
    onSecondary = OsitoWhite,
    onTertiary = OsitoWhite,
    onBackground = OsitoBlack,
    onSurface = OsitoBlack,
)

@Composable
fun OsitoPolarAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color está disponible en Android 12+
    dynamicColor: Boolean = false, // Lo pongo en false para forzar tus colores azules
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography, // Asegúrate de tener Typography.kt o borra esta línea si da error
        content = content
    )
}