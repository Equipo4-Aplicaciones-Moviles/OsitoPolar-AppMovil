package com.example.ositopolarapp.navigation

import android.net.Uri
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.ositopolarapp.core.di.*
import com.example.ositopolarapp.features.authentication.presentation.screens.LoginScreen
import com.example.ositopolarapp.features.authentication.presentation.screens.RegistrationScreen
import com.example.ositopolarapp.features.authentication.presentation.state.*

import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Text
import androidx.compose.foundation.layout.fillMaxSize

@Composable
fun AppNavigation(
    appContainer: AppContainer,
    deepLinkUri: Uri?,
    onDeepLinkProcessed: () -> Unit
) {

    val navController = rememberNavController()

    val mainFactory = remember { MainViewModelFactory(appContainer) }
    val regFactory = remember { RegistrationViewModelFactory(appContainer) }
    val authFactory = remember { AuthViewModelFactory(appContainer) }

    val mainVM = viewModel<MainViewModel>(factory = mainFactory)
    val authState by mainVM.authState.collectAsState()

    val startDestination = when (authState) {
        AuthState.Loading -> ""
        AuthState.LoggedIn -> "dashboard"
        AuthState.LoggedOut -> "login"
    }

    if (authState == AuthState.Loading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    NavHost(navController = navController, startDestination = startDestination) {

        composable("login") {
            LoginScreen(
                viewModel = viewModel(factory = authFactory),
                onLoginSuccess = {
                    navController.navigate("dashboard") {
                        popUpTo("login") { inclusive = true }
                    }
                },
                onGoToRegister = { navController.navigate("register/1") }
            )
        }

        composable("register/{planId}") { entry ->
            val planId = entry.arguments?.getString("planId")?.toIntOrNull() ?: 1

            RegistrationScreen(
                viewModel = viewModel(factory = regFactory),
                planId = planId,
                userType = "Owner",
                deepLinkUri = deepLinkUri,
                onRegistrationSuccess = {
                    navController.navigate("login") {
                        popUpTo("login") { inclusive = true }
                    }
                },
                onDeepLinkProcessed = onDeepLinkProcessed
            )
        }

        composable("dashboard") {
            Text("¡Dashboard!", modifier = Modifier.fillMaxSize())
        }
    }
}
