package com.example.ositopolarapp.features.authentication.presentation.state

import android.util.Log // <-- Importar la librería de Log de Android
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import android.content.SharedPreferences // Importar
import com.google.gson.Gson // Importar
import com.example.ositopolarapp.features.authentication.data.dto.CompleteRegistrationRequest
import com.example.ositopolarapp.features.authentication.domain.usecase.CreateRegistrationCheckoutUseCase
import com.example.ositopolarapp.features.authentication.domain.usecase.CompleteRegistrationUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

// Constante para la clave de SharedPreferences
private const val PREF_FORM_DATA = "reg_form_data_cache"

// Define los estados de la UI
data class RegistrationUiState(
    val isLoading: Boolean = false,
    val checkoutUrl: String? = null,
    val registrationComplete: Boolean = false,
    val error: String? = null,
    val formData: CompleteRegistrationRequest? = null,
    val generatedUsername: String? = null,
    val generatedPassword: String? = null
)

class RegistrationViewModel(
    private val createRegistrationCheckoutUseCase: CreateRegistrationCheckoutUseCase,
    private val completeRegistrationUseCase: CompleteRegistrationUseCase,
    private val prefs: SharedPreferences // 🔹 Recibe SharedPreferences
) : ViewModel() {

    private val TAG = "REG_VM"

    private val _uiState = MutableStateFlow(RegistrationUiState())
    val uiState: StateFlow<RegistrationUiState> = _uiState.asStateFlow()

    // 🔹 Inicialización: Intenta cargar el formulario si hubo un "Process Death"
    init {
        loadPendingFormData()
    }

    private fun loadPendingFormData() {
        val json = prefs.getString(PREF_FORM_DATA, null)
        if (json != null) {
            try {
                // Necesitas una dependencia de Gson: implementation("com.google.code.gson:gson:2.10.1")
                val pendingData = Gson().fromJson(json, CompleteRegistrationRequest::class.java)
                _uiState.update { it.copy(formData = pendingData) }
                Log.w(TAG, "Datos de formulario restaurados después de Process Death.")
            } catch (e: Exception) {
                Log.e(TAG, "Error al deserializar formulario: ${e.message}")
                prefs.edit().remove(PREF_FORM_DATA).apply() // Limpia datos corruptos
            }
        }
    }


    /**
     * PASO 1: Crea el Checkout y guarda los datos en caché antes de abrir el navegador.
     */
    fun createCheckout(planId: Int, userType: String, formData: CompleteRegistrationRequest) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            // 🛑 1. GUARDAR LOS DATOS EN SHARED PREFERENCES (Persistencia)
            val json = Gson().toJson(formData)
            prefs.edit().putString(PREF_FORM_DATA, json).apply()
            Log.i(TAG, "Paso 1: Datos de formulario guardados en SharedPreferences.")

            _uiState.update { it.copy(formData = formData) }

            createRegistrationCheckoutUseCase(planId, userType)
                .onSuccess { entity ->
                    _uiState.update {
                        it.copy(isLoading = false, checkoutUrl = entity.checkoutUrl)
                    }
                }
                .onFailure { error ->
                    _uiState.update {
                        it.copy(isLoading = false, error = error.message)
                    }
                }
        }
    }

    /**
     * PASO 2: Completa el registro usando el SessionID del Deep Link y los datos guardados.
     */
    fun completeRegistration(sessionIdFromUrl: String) {
        // 1. CARGA/VERIFICA datos, incluyendo los cargados por 'init'
        var formData = _uiState.value.formData

        if (formData == null) {
            // Intento final de recuperar por si el 'init' falló
            val json = prefs.getString(PREF_FORM_DATA, null)
            if (json != null) {
                try {
                    formData = Gson().fromJson(json, CompleteRegistrationRequest::class.java)
                } catch (e: Exception) {
                    Log.e(TAG, "Error al deserializar en Paso 2: ${e.message}")
                }
            }
        }

        // VALIDACIÓN FINAL
        if (formData == null) {
            _uiState.update { it.copy(error = "Error fatal: No se pudieron restaurar los datos del formulario.") }
            prefs.edit().remove(PREF_FORM_DATA).apply()
            return
        }

        // 2. CONSTRUCCIÓN DEL REQUEST FINAL
        val finalRequest = formData.copy(sessionId = sessionIdFromUrl)

        // 🚀 LOG DE VERIFICACIÓN (Añadido para depuración de JSON)
        Log.d(TAG, "Requesting final registration with data: $finalRequest")


        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            Log.i(TAG, "Paso 2: Ejecutando completeRegistration con SessionID: $sessionIdFromUrl")

            completeRegistrationUseCase(finalRequest)
                .onSuccess { credentials ->
                    // 🚀 LOG DE ÉXITO EXPLICITO
                    Log.i(TAG, "¡REGISTRO EXITOSO! Usuario: ${credentials.first}, Password: ${credentials.second}")

                    // 3. LIMPIAR LOS DATOS TEMPORALES DESPUÉS DEL ÉXITO
                    prefs.edit().remove(PREF_FORM_DATA).apply()

                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            registrationComplete = true,
                            formData = null,
                            generatedUsername = credentials.first,
                            generatedPassword = credentials.second
                        )
                    }
                }
                .onFailure { error ->
                    Log.e(TAG, "Error en Paso 2 (completeRegistration): ${error.message}")
                    _uiState.update {
                        it.copy(isLoading = false, error = error.message)
                    }
                }
        }
    }

    fun clearCheckoutUrl() {
        _uiState.update { it.copy(checkoutUrl = null) }
    }
}