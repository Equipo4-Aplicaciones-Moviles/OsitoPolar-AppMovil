package com.example.ositopolarapp.core.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * PreferencesManager
 *
 * Manages app preferences using DataStore.
 * Currently handles:
 * - Theme preference (Light/Dark/System)
 */
class PreferencesManager(private val context: Context) {

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

    companion object {
        private val THEME_KEY = stringPreferencesKey("theme_preference")
        const val THEME_LIGHT = "light"
        const val THEME_DARK = "dark"
        const val THEME_SYSTEM = "system"
    }

    /**
     * Get theme preference as Flow
     * Default: THEME_SYSTEM
     */
    val themePreference: Flow<String> = context.dataStore.data.map { preferences ->
        preferences[THEME_KEY] ?: THEME_SYSTEM
    }

    /**
     * Save theme preference
     * @param theme One of: THEME_LIGHT, THEME_DARK, THEME_SYSTEM
     */
    suspend fun saveThemePreference(theme: String) {
        context.dataStore.edit { preferences ->
            preferences[THEME_KEY] = theme
        }
    }
}
