package com.example.ositopolarapp.core.data.network

import android.content.Context
import android.content.SharedPreferences

class PreferencesManager(context: Context) {
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("os_polar_prefs", Context.MODE_PRIVATE)

    // --- AUTH ---
    fun saveToken(token: String) {
        sharedPreferences.edit().putString("auth_token", token).apply()
    }

    fun getToken(): String? {
        return sharedPreferences.getString("auth_token", null)
    }

    fun clearData() {
        sharedPreferences.edit().clear().apply()
    }

    // --- THEME / SETTINGS (Lo que faltaba) ---
    fun saveThemePreference(isDark: Boolean) {
        sharedPreferences.edit().putBoolean("dark_mode", isDark).apply()
    }

    fun getThemePreference(): Boolean {
        return sharedPreferences.getBoolean("dark_mode", false) // false por defecto
    }

    // --- NOTIFICATIONS ---
    fun saveNotificationPreference(enabled: Boolean) {
        sharedPreferences.edit().putBoolean("notifications_enabled", enabled).apply()
    }

    fun getNotificationPreference(): Boolean {
        return sharedPreferences.getBoolean("notifications_enabled", true)
    }
}