package com.example.ositopolarapp

import android.app.Application
import com.example.ositopolarapp.core.di.AppContainer
import android.content.Intent
import com.example.ositopolarapp.features.authentication.domain.repository.AuthRepository

/**
 * Clase Application personalizada para inicializar dependencias únicas (Singletons).
 */
class OsitoPolarApplication : Application() {

    // Instancia pública y única del Contenedor de Dependencias
    val appContainer: AppContainer by lazy {
        AppContainer(applicationContext)
    }
}