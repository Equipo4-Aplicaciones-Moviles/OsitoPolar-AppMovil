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
import androidx.compose.ui.Modifier
// 1. Importa tu nuevo navegador
import com.example.ositopolarapp.navigation.AppNavigation
import com.example.ositopolarapp.ui.theme.OsitoPolarAppTheme

import com.example.ositopolarapp.core.di.AppContainer
import com.example.ositopolarapp.core.di.AuthViewModelFactory

import com.example.ositopolarapp.features.authentication.presentation.screens.LoginScreen
import com.example.ositopolarapp.features.authentication.presentation.screens.RegistrationScreen

class MainActivity : ComponentActivity() {

    val appContainer by lazy { AppContainer(applicationContext) }
    private var deepLinkUri = mutableStateOf<Uri?>(null)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 1. Manejar el Intent inicial (si la app estaba cerrada)
        handleIntent(intent)

        enableEdgeToEdge()
        setContent {
            OsitoPolarAppTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    // 2. Pasamos el estado de la URI a AppNavigation
                    AppNavigation(appContainer = appContainer,deepLinkUri = deepLinkUri.value)
                }
            }
        }
    }

    // 3. Manejar Intentes nuevos (si la app ya estaba abierta)
    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        handleIntent(intent)
    }

    private fun handleIntent(intent: Intent?) {
        if (intent?.action == Intent.ACTION_VIEW) {
            val uri = intent.data
            // Solo procesamos si la URI comienza con nuestro scheme/host
            if (uri != null && uri.scheme == "ositopolar" && uri.host == "registration") {
                deepLinkUri.value = uri // Almacena la URI en el estado
                // Opcional: limpiar la URI en el intent original
                intent.data = null
            }
        }
    }
}

// 3. Ya no necesitamos Greeting ni GreetingPreview en este archivo
