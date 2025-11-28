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
    val generatedUsername: String? = null,
    val generatedPassword: String? = null
)

private const val PREF_FORM_DATA = "reg_form_data_cache"

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
        if (json != null) {
            try {
                val pendingData = Gson().fromJson(json, CompleteRegistrationRequest::class.java)
                _uiState.update { it.copy(formData = pendingData) }
            } catch (e: Exception) {
                prefs.edit().remove(PREF_FORM_DATA).apply()
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
                    _uiState.update { it.copy(isLoading = false, checkoutUrl = response.checkoutUrl) }
                }
                .onFailure { error ->
                    _uiState.update { it.copy(isLoading = false, error = error.message) }
                }
        }
    }

    fun completeRegistration(sessionIdFromUrl: String) {
        var formData = _uiState.value.formData
        if (formData == null) {
            val json = prefs.getString(PREF_FORM_DATA, null)
            if (json != null) {
                try {
                    formData = Gson().fromJson(json, CompleteRegistrationRequest::class.java)
                } catch (e: Exception) {
                    Log.e(TAG, "Error parsing json", e)
                }
            }
        }

        if (formData == null) {
            _uiState.update { it.copy(error = "No hay datos de registro guardados.") }
            return
        }

        // Creamos el request final con el ID de sesión
        val finalRequest = formData.copy(sessionId = sessionIdFromUrl)

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            completeRegistrationUseCase(finalRequest)
                .onSuccess { credentials ->
                    prefs.edit().remove(PREF_FORM_DATA).apply()
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
                    _uiState.update { it.copy(isLoading = false, error = error.message) }
                }
        }
    }

    fun clearCheckoutUrl() {
        _uiState.update { it.copy(checkoutUrl = null) }
    }
}