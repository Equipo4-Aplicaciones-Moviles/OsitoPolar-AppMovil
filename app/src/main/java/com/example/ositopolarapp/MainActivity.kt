package com.example.ositopolarapp

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.example.ositopolarapp.navigation.AppNavigation
import com.example.ositopolarapp.ui.theme.OsitoPolarAppTheme
import com.example.ositopolarapp.core.di.AppContainer
import com.example.ositopolarapp.core.data.PreferencesManager

class MainActivity : ComponentActivity() {

    // 🔹 Obtenemos el contenedor global desde OsitoPolarApplication (Patrón Singleton Seguro)
    private val appContainer: AppContainer
        get() = (application as OsitoPolarApplication).appContainer

    // Estado para manejar el deep link recibido
    private var deepLinkUri = mutableStateOf<Uri?>(null)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Manejar intent inicial (deep link)
        handleIntent(intent)

        enableEdgeToEdge()
        setContent {
            // Collect theme preference from DataStore
            val themePreference by appContainer.preferencesManager.themePreference.collectAsState(
                initial = PreferencesManager.THEME_SYSTEM
            )

            OsitoPolarAppTheme(themePreference = themePreference) {
                Surface(modifier = Modifier.fillMaxSize()) {

                    // 1. Obtiene la referencia mutable de la URI
                    val uriState = remember { deepLinkUri }

                    // 2. Define el callback de limpieza que se pasa a AppNavigation
                    val onDeepLinkProcessed: () -> Unit = {
                        uriState.value = null // Limpia el estado en MainActivity
                    }

                    AppNavigation(
                        appContainer = appContainer,
                        deepLinkUri = uriState.value,
                        onDeepLinkProcessed = onDeepLinkProcessed // <-- ¡Callback pasado!
                    )
                }
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        handleIntent(intent)
    }

    /**
     * Procesa un intent que podría contener un deep link.
     */
    private fun handleIntent(intent: Intent?) {
        if (intent?.action == Intent.ACTION_VIEW) {
            val uri = intent.data
            if (uri != null &&
                uri.scheme == "ositopolar" &&
                uri.host == "registration"
            ) {
                deepLinkUri.value = uri

                // Evita que el intent se procese de nuevo
                intent.data = null
            }
        }
    }
}