package com.example.ositopolarapp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import android.net.Uri
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.ositopolarapp.features.authentication.presentation.state.LoginViewModel
import com.example.ositopolarapp.features.authentication.presentation.state.RegistrationViewModel

// 1. Importa TODAS tus pantallas reales

// 2. He borrado las pantallas temporales falsas
//    (ClientLoginScreen y ProviderLoginScreen)
//    que estaban aquí.

/**
 * Define todas las rutas de navegación de la aplicación.
 */

import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel

import com.example.ositopolarapp.core.di.AppContainer
import com.example.ositopolarapp.core.di.AuthViewModelFactory

import com.example.ositopolarapp.features.authentication.presentation.screens.ClientLoginScreen
import com.example.ositopolarapp.features.authentication.presentation.screens.LoginScreen
import com.example.ositopolarapp.features.authentication.presentation.screens.RegistrationScreen

@Composable
fun AppNavigation(appContainer: AppContainer,deepLinkUri: Uri?) {


    // 1. El controlador que maneja las rutas de navegación
    val navController = rememberNavController()

    // 2. ¡Nuestra DI! Creamos el contenedor y la fábrica
    // 'remember' es clave para que no se re-creen en cada recomposición
    //val appContainer = remember { AppContainer() }
    val authViewModelFactory = remember { AuthViewModelFactory(appContainer) }

    // 3. El NavHost que define todas las rutas (pantallas)
    // Empezamos en la ruta "login"
    NavHost(navController = navController, startDestination = "login") {

        /**
         * Ruta para la Pantalla de Login
         */
        composable(route = "login") {
            // Verás error aquí hasta que creemos LoginScreen.kt
            LoginScreen(
                // ¡Así le pasamos la factory!
                viewModel = viewModel(factory = authViewModelFactory),

                // Definimos qué hacer en cada acción
                onLoginSuccess = {
                    // Cuando el login sea exitoso, ir al "dashboard"
                    navController.navigate("dashboard") {
                        // Borra "login" de la pila para que no pueda volver
                        popUpTo("login") { inclusive = true }
                    }
                },
                onGoToRegister = {
                    // Ir a la pantalla de registro
                    // Pasamos un ID de plan de ejemplo
                    navController.navigate("register/1")
                }
            )
        }

        /**
         * Ruta para la Pantalla de Registro
         */
        composable(route = "register/{planId}") { backStackEntry ->
            val planId = backStackEntry.arguments?.getString("planId")?.toIntOrNull() ?: 1

            RegistrationScreen(
                // ¡Le pasamos la misma factory!
                // AHORA: Especificamos que debe crear un RegistrationViewModel.
                viewModel = viewModel(
                    modelClass = RegistrationViewModel::class.java, // <-- ¡Solución aquí!
                    factory = authViewModelFactory
                ),
                planId = planId,
                userType = "Owner",
                deepLinkUri = deepLinkUri,// O pasarlo como argumento
                onRegistrationSuccess = {
                    // Cuando el registro termine, ir al login
                    navController.navigate("login") {
                        popUpTo("login") { inclusive = true }
                    }
                }
            )
        }

        /**
         * Ruta para el Dashboard (aún no existe)
         */
        composable(route = "dashboard") {
            // Aquí iría tu DashboardScreen()
            // Por ahora, puedes poner un Text("¡Logueado!")
        }
    }
}
