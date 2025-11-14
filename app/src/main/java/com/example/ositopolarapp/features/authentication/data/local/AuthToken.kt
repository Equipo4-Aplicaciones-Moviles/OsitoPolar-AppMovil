package com.example.ositopolarapp.features.authentication.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Entidad de Room para guardar el Token de Sesión y datos del usuario.
 * Solo necesitamos una entrada, por eso la @PrimaryKey es estática.
 */
@Entity(tableName = "auth_tokens")
data class AuthToken(
    // 1. Identificador estático. Siempre será 1 ya que solo guardamos el token activo.
    @PrimaryKey(autoGenerate = false)
    val id: Int = 1,

    // 2. El token JWT que se recibe del servidor.
    @ColumnInfo(name = "token")
    val token: String,

    // 3. User information from authentication response
    @ColumnInfo(name = "user_id")
    val userId: Int,

    @ColumnInfo(name = "username")
    val username: String,

    @ColumnInfo(name = "user_type")
    val userType: String,

    @ColumnInfo(name = "profile_id")
    val profileId: Int,

    // 4. 2FA status
    @ColumnInfo(name = "requires_2fa")
    val requires2FA: Boolean = false,

    @ColumnInfo(name = "requires_two_factor_setup")
    val requiresTwoFactorSetup: Boolean = false,

    // 5. (Opcional, pero recomendado) Un timestamp para saber cuándo caduca.
    @ColumnInfo(name = "expiry_date")
    val expiryDate: Long? = null
)