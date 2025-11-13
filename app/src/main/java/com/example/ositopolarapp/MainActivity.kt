package com.example.ositopolarapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
// 1. Importa tu nuevo navegador
import com.example.ositopolarapp.navigation.AppNavigation
import com.example.ositopolarapp.ui.theme.OsitoPolarAppTheme

import com.example.ositopolarapp.core.di.AppContainer
import com.example.ositopolarapp.core.di.AuthViewModelFactory

import com.example.ositopolarapp.features.authentication.presentation.screens.LoginScreen
import com.example.ositopolarapp.features.authentication.presentation.screens.RegistrationScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            OsitoPolarAppTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    // 2. Llama a tu navegador principal aquí
                    AppNavigation()
                }
            }
        }
    }
}

// 3. Ya no necesitamos Greeting ni GreetingPreview en este archivo
