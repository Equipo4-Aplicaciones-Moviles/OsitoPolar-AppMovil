package com.example.ositopolarapp.features.authentication.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

/**
 * Define la base de datos de Room para el módulo de Autenticación.
 */
@Database(
    entities = [AuthToken::class], // Lista de todas las tablas
    version = 1,                  // La versión actual de la DB
    exportSchema = false          // No exportamos el esquema a un archivo
)
abstract class AuthDatabase : RoomDatabase() {

    // Define las DAOs para acceder a las tablas
    abstract fun authDao(): AuthDao
}