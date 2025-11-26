package com.example.ositopolarapp

import android.app.Application
import com.example.ositopolarapp.core.di.AppContainer
import com.example.ositopolarapp.core.di.DefaultAppContainer
// Asegúrate de que DefaultAppContainer exista.
// Si tu contenedor se llama AppDataContainer, cambia el nombre aquí.

class OsitoPolarApplication : Application() {

    // Esta es la variable 'container' que MainActivity no encontraba
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        // Inicializamos el contenedor
        container = DefaultAppContainer(this)
    }
}