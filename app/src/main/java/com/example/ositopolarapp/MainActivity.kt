package com.example.ositopolarapp

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.ositopolarapp.navigation.AppNavigation
import com.example.ositopolarapp.ui.theme.OsitoPolarAppTheme // Importamos el tema que acabamos de definir

class MainActivity : ComponentActivity() {

    // Estado para manejar el deep link si llega uno
    private var deepLinkUri by mutableStateOf<Uri?>(null)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Manejar Deep Link inicial si la app se abre desde un link
        handleIntent(intent)

        // Obtener el contenedor de dependencias desde la clase Application
        val appContainer = (application as OsitoPolarApplication).container

        setContent {
            // ✅ CORRECCIÓN: Llamamos al tema correcto "OsitoPolarAppTheme"
            OsitoPolarAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // ✅ CORRECCIÓN: AppNavigation se llama dentro de un contexto Composable
                    AppNavigation(
                        appContainer = appContainer,
                        deepLinkUri = deepLinkUri,
                        onDeepLinkProcessed = { deepLinkUri = null }
                    )
                }
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent) // Corregido para aceptar Intent nullable o non-null según versión
        handleIntent(intent)
    }

    private fun handleIntent(intent: Intent?) {
        if (intent?.action == Intent.ACTION_VIEW) {
            deepLinkUri = intent.data
        }
    }
}