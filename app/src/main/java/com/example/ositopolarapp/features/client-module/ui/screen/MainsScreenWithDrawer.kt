package com.example.ositopolarapp.features.`client-module`.ui.screen

// IMPORTS DE NAVEGACIÓN
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

// IMPORTS DEL TEMA
import com.example.ositopolarapp.ui.theme.OsitoPolarAccentBlue

// IMPORTS GENERALES
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
// Importación del componente de la otra carpeta
import com.example.ositopolarapp.features.`client-module`.ui.composables.DrawerContent
import kotlinx.coroutines.launch

// Rutas de navegación (incluye todas las pantallas)
object MainDestinations {
    const val HOME_ROUTE = "home"
    const val MY_MACHINES_ROUTE = "my_machines"
    const val MACHINE_CONTROL_ROUTE = "machine_control"
    const val RENT_ROUTE = "rent"
    const val CONTACT_ROUTE = "contact"
    const val NOTIFICATIONS_ROUTE = "notifications"
    const val ACCOUNT_ROUTE = "account"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreenWithDrawer() {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val navController = rememberNavController()

    // Configuración RTL del Drawer para apertura a la derecha
    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        ModalNavigationDrawer(
            drawerState = drawerState,
            drawerContent = {
                // Contenido del Drawer en LTR
                CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
                    DrawerContent(
                        onItemSelected = { route ->
                            scope.launch { drawerState.close() }
                            navController.navigate(route) {
                                popUpTo(MainDestinations.HOME_ROUTE) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            },
            content = {
                // Contenido principal en LTR
                CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {

                    Scaffold(
                        topBar = {
                            TopAppBar(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .shadow(2.dp), // Sombra inferior
                                title = {
                                    Text(
                                        text = "OsitoPolar",
                                        color = OsitoPolarAccentBlue,
                                        fontSize = 24.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                },
                                actions = {
                                    IconButton(onClick = {
                                        scope.launch {
                                            if (drawerState.isClosed) drawerState.open() else drawerState.close()
                                        }
                                    }) {
                                        Icon(
                                            Icons.Default.Menu,
                                            contentDescription = "Menú de navegación",
                                            tint = OsitoPolarAccentBlue
                                        )
                                    }
                                },
                                colors = TopAppBarDefaults.topAppBarColors(
                                    containerColor = Color.White // Fondo blanco
                                )
                            )
                        }
                    ) { paddingValues ->

                        // NavHost - Gestiona todas las pantallas
                        NavHost(
                            navController = navController,
                            startDestination = MainDestinations.HOME_ROUTE,
                            modifier = Modifier.fillMaxSize()
                        ) {
                            composable(MainDestinations.HOME_ROUTE) {
                                HomeScreen(paddingValues)
                            }
                            composable(MainDestinations.MY_MACHINES_ROUTE) {
                                // Pasamos el NavController a MyMachinesScreen
                                MyMachinesScreen(paddingValues = paddingValues, navController = navController)
                            }
                            composable(MainDestinations.MACHINE_CONTROL_ROUTE) {
                                MachineControlScreen(paddingValues = paddingValues)
                            }
                            composable(MainDestinations.RENT_ROUTE) {
                                RentScreen(paddingValues)
                            }
                            composable(MainDestinations.CONTACT_ROUTE) {
                                ContactScreen(paddingValues)
                            }
                            composable(MainDestinations.NOTIFICATIONS_ROUTE) {
                                NotificationsScreen(paddingValues)
                            }
                            composable(MainDestinations.ACCOUNT_ROUTE) {
                                // Llama a la pantalla de Mi Cuenta
                                AccountScreen(paddingValues = paddingValues)
                            }
                        }
                    }
                }
            }
        )
    }
}