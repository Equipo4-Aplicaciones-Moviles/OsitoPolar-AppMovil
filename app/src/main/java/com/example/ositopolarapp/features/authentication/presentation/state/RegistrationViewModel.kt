package com.example.ositopolarapp.features.authentication.presentation.state

import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ositopolarapp.features.authentication.data.dto.CompleteRegistrationRequest
import com.example.ositopolarapp.features.authentication.domain.usecase.CompleteRegistrationUseCase
import com.example.ositopolarapp.features.authentication.domain.usecase.CreateRegistrationCheckoutUseCase
import com.google.gson.Gson
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

// UI State
data class RegistrationUiState(
    val isLoading: Boolean = false,
    val checkoutUrl: String? = null,
    val registrationComplete: Boolean = false,
    val error: String? = null,
    val formData: CompleteRegistrationRequest? = null,
    val backendSessionId: String? = null, // SessionId generado por el backend
    val generatedUsername: String? = null,
    val generatedPassword: String? = null
)

private const val PREF_FORM_DATA = "reg_form_data_cache"
private const val PREF_SESSION_ID = "reg_backend_session_id"

class RegistrationViewModel(
    private val createRegistrationCheckoutUseCase: CreateRegistrationCheckoutUseCase,
    private val completeRegistrationUseCase: CompleteRegistrationUseCase,
    private val prefs: SharedPreferences
) : ViewModel() {

    private val TAG = "REG_VM"
    private val _uiState = MutableStateFlow(RegistrationUiState())
    val uiState: StateFlow<RegistrationUiState> = _uiState.asStateFlow()

    init {
        loadPendingFormData()
    }

    private fun loadPendingFormData() {
        val json = prefs.getString(PREF_FORM_DATA, null)
        val savedSessionId = prefs.getString(PREF_SESSION_ID, null)

        if (json != null) {
            try {
                val pendingData = Gson().fromJson(json, CompleteRegistrationRequest::class.java)
                _uiState.update { it.copy(formData = pendingData, backendSessionId = savedSessionId) }
            } catch (e: Exception) {
                prefs.edit().remove(PREF_FORM_DATA).apply()
                prefs.edit().remove(PREF_SESSION_ID).apply()
            }
        }
    }

    fun createCheckout(planId: Int, userType: String, formData: CompleteRegistrationRequest) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            // Guardar en cache
            val json = Gson().toJson(formData)
            prefs.edit().putString(PREF_FORM_DATA, json).apply()
            _uiState.update { it.copy(formData = formData) }

            createRegistrationCheckoutUseCase(CreateRegistrationCheckoutUseCase.Params(planId, userType))
                .onSuccess { response ->
                    // Guardar el sessionId del backend en SharedPreferences
                    prefs.edit().putString(PREF_SESSION_ID, response.sessionId).apply()
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            checkoutUrl = response.checkoutUrl,
                            backendSessionId = response.sessionId
                        )
                    }
                }
                .onFailure { error ->
                    _uiState.update { it.copy(isLoading = false, error = error.message) }
                }
        }
    }

    fun completeRegistration(sessionIdFromUrl: String?) {
        Log.d(TAG, "completeRegistration iniciado con sessionId desde URL: $sessionIdFromUrl")

        var formData = _uiState.value.formData
        var backendSessionId = _uiState.value.backendSessionId

        Log.d(TAG, "FormData en memoria: ${formData != null}")
        Log.d(TAG, "BackendSessionId en memoria: $backendSessionId")

        if (formData == null || backendSessionId == null) {
            val json = prefs.getString(PREF_FORM_DATA, null)
            val savedSessionId = prefs.getString(PREF_SESSION_ID, null)
            Log.d(TAG, "Intentando cargar de SharedPreferences. FormData existe: ${json != null}, SessionId existe: ${savedSessionId != null}")

            if (json != null) {
                try {
                    formData = Gson().fromJson(json, CompleteRegistrationRequest::class.java)
                    Log.d(TAG, "FormData cargado exitosamente desde cache")
                } catch (e: Exception) {
                    Log.e(TAG, "Error parsing json", e)
                }
            }

            backendSessionId = savedSessionId
        }

        if (formData == null) {
            Log.e(TAG, "No hay datos de registro guardados")
            _uiState.update { it.copy(error = "No hay datos de registro guardados. Por favor, completa el formulario nuevamente.") }
            return
        }

        if (backendSessionId == null) {
            Log.e(TAG, "No hay sessionId del backend guardado")
            _uiState.update { it.copy(error = "Sesión de pago no encontrada. Por favor, intenta registrarte nuevamente.") }
            return
        }

        // Creamos el request final con el sessionId del BACKEND (no el de Stripe)
        val finalRequest = formData.copy(sessionId = backendSessionId)
        Log.d(TAG, "Request completo preparado: email=${finalRequest.email}, sessionId=${finalRequest.sessionId}")

        // Log del JSON completo para debugging
        val requestJson = Gson().toJson(finalRequest)
        Log.d(TAG, "JSON que se enviará al backend:")
        Log.d(TAG, requestJson)

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            Log.d(TAG, "Llamando a completeRegistrationUseCase...")

            completeRegistrationUseCase(finalRequest)
                .onSuccess { credentials ->
                    Log.d(TAG, "Registro completado exitosamente! Username: ${credentials.first}")
                    // Limpiar cache
                    prefs.edit().remove(PREF_FORM_DATA).apply()
                    prefs.edit().remove(PREF_SESSION_ID).apply()
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            registrationComplete = true,
                            generatedUsername = credentials.first,
                            generatedPassword = credentials.second
                        )
                    }
                }
                .onFailure { error ->
                    Log.e(TAG, "Error al completar registro: ${error.message}", error)
                    _uiState.update { it.copy(isLoading = false, error = error.message) }
                }
        }
    }

    fun clearCheckoutUrl() {
        _uiState.update { it.copy(checkoutUrl = null) }
    }
}