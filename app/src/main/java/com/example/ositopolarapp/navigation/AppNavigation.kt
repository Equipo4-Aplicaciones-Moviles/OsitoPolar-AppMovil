package com.example.ositopolarapp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

// 1. Importa TODAS tus pantallas reales
import com.example.ositopolarapp.features.authentication.ui.screen.SelectProfileScreen
import com.example.ositopolarapp.features.authentication.ui.screen.ClientLoginScreen
import com.example.ositopolarapp.features.authentication.ui.screen.ProviderLoginScreen
import com.example.ositopolarapp.features.authentication.ui.screen.ClientRegisterScreen
import com.example.ositopolarapp.features.authentication.ui.screen.ProviderRegisterScreen

// 2. He borrado las pantallas temporales falsas
//    (ClientLoginScreen y ProviderLoginScreen)
//    que estaban aquí.

/**
 * Define todas las rutas de navegación de la aplicación.
 */
@Composable
fun AppNavigation() {
    // 1. Crea el controlador de navegación
    val navController = rememberNavController()

    // 2. Define el "host" que intercambiará las pantallas (Composables)
    NavHost(
        navController = navController,
        startDestination = AppRoutes.SelectProfile.route // Define la pantalla inicial
    ) {

        // Ruta 1: Pantalla de Selección de Perfil (Estaba correcta)
        composable(route = AppRoutes.SelectProfile.route) {
            SelectProfileScreen(
                onClientClicked = {
                    // Navega a la ruta del login de cliente
                    navController.navigate(AppRoutes.ClientLogin.route)
                },
                onProviderClicked = {
                    // Navega a la ruta del login de empresa
                    navController.navigate(AppRoutes.ProviderLogin.route)
                }
            )
        }

        // Ruta 2: Pantalla de Login de Cliente (MODIFICADA)
        // 3. Ahora llama a tu pantalla REAL con los parámetros correctos
        composable(route = AppRoutes.ClientLogin.route) {
            ClientLoginScreen(
                onLoginClicked = { username, password ->
                    // TODO: Aquí es donde llamarías a tu ViewModel para
                    // validar el login. El ViewModel se encargaría
                    // de navegar al dashboard si el login es exitoso.
                },
                onRegisterClicked = {
                    // Navega a la pantalla de registro de cliente
                    navController.navigate(AppRoutes.ClientRegister.route)
                }
            )
        }

        // Ruta 3: Pantalla de Login de Empresa (MODIFICADA)
        // 4. Llama a tu pantalla REAL con los parámetros correctos
        composable(route = AppRoutes.ProviderLogin.route) {
            ProviderLoginScreen(
                onLoginClicked = { bussinessName, password ->
                    // TODO: Llamar al ViewModel de login de empresa
                },
                onRegisterClicked = {
                    // Navega a la pantalla de registro de empresa
                    navController.navigate(AppRoutes.ProviderRegister.route)
                }
            )
        }

        // --- AÑADIMOS LAS NUEVAS RUTAS DE REGISTRO ---

        // Ruta 4: Pantalla de Registro de Cliente (NUEVA)
        composable(route = AppRoutes.ClientRegister.route) {
            ClientRegisterScreen(
                onSignUpClicked = { fullName, username, password ->
                    // TODO: Llamar al ViewModel para registrar al cliente
                },
                onLoginClicked = {
                    // Vuelve a la pantalla anterior (Login de Cliente)
                    navController.popBackStack()
                }
            )
        }

        // Ruta 5: Pantalla de Registro de Empresa (NUEVA)
        composable(route = AppRoutes.ProviderRegister.route) {
            ProviderRegisterScreen(
                onSignUpClicked = { bussinessName, username, password ->
                    // TODO: Llamar al ViewModel para registrar a la empresa
                },
                onLoginClicked = {
                    // Vuelve a la pantalla anterior (Login de Empresa)
                    navController.popBackStack()
                }
            )
        }
    }
}

/**
 * Clase sellada para definir las rutas de forma centralizada y segura.
 * Esto evita errores de escritura al navegar.
 */
sealed class AppRoutes(val route: String) {
    object SelectProfile : AppRoutes("select_profile")
    object ClientLogin : AppRoutes("client_login")
    object ProviderLogin : AppRoutes("provider_login")
    // AÑADIDAS:
    object ClientRegister : AppRoutes("client_register")
    object ProviderRegister : AppRoutes("provider_register")
    // object ClientDashboard : AppRoutes("client_dashboard")
    // object ProviderDashboard : AppRoutes("provider_dashboard")
}

