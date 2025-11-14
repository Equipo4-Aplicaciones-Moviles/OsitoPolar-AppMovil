package com.example.ositopolarapp.features.authentication.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface AuthDao {

    /**
     * Inserta un nuevo token. Si ya existe (mismo id=1), lo reemplaza.
     * Esta es la función de "guardar sesión" o "actualizar token".
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertToken(token: AuthToken)

    /**
     * Recupera el token de sesión.
     * Usamos Flow para observar cambios en tiempo real si el token se borra o actualiza.
     */
    @Query("SELECT * FROM auth_tokens WHERE id = 1")
    fun getToken(): Flow<AuthToken?>

    /**
     * Recupera el token de sesión de forma síncrona (para operaciones puntuales).
     */
    @Query("SELECT * FROM auth_tokens WHERE id = 1")
    suspend fun getTokenOnce(): AuthToken?

    /**
     * Cierra la sesión eliminando el token.
     */
    @Query("DELETE FROM auth_tokens")
    suspend fun deleteToken()
}