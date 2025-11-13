package com.example.ositopolarapp.features.authentication.presentation.state

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ositopolarapp.features.authentication.data.dto.CompleteRegistrationRequest
import com.example.ositopolarapp.features.authentication.domain.usecase.CreateRegistrationCheckoutUseCase
import com.example.ositopolarapp.features.authentication.domain.usecase.CompleteRegistrationUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

// Define los estados de la UI
data class RegistrationUiState(
    val isLoading: Boolean = false,
    val checkoutUrl: String? = null,
    val registrationComplete: Boolean = false,
    val error: String? = null,

    // Aquí guardamos el formulario, igual que en Flutter
    val formData: CompleteRegistrationRequest? = null
)

class RegistrationViewModel(
    private val createRegistrationCheckoutUseCase: CreateRegistrationCheckoutUseCase,
    private val completeRegistrationUseCase: CompleteRegistrationUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(RegistrationUiState())
    val uiState: StateFlow<RegistrationUiState> = _uiState.asStateFlow()

    /**
     * PASO 1: Es llamado por la UI al presionar "Registrar"
     */
    fun createCheckout(planId: Int, userType: String, formData: CompleteRegistrationRequest) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            // ¡Guardamos el formulario en el estado!
            _uiState.update { it.copy(formData = formData) }

            createRegistrationCheckoutUseCase(planId, userType)
                .onSuccess { entity ->
                    // ¡Éxito! Enviamos la URL a la UI
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
     * PASO 2: Es llamado por la Activity/Screen cuando vuelve del Deep Link
     */
    fun completeRegistration(sessionIdFromUrl: String) {
        val formData = _uiState.value.formData

        if (formData == null) {
            _uiState.update { it.copy(error = "Error: No se encontraron datos del formulario.") }
            return
        }

        // Combinamos el ID de sesión con los datos guardados
        val finalRequest = formData.copy(sessionId = sessionIdFromUrl)

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            completeRegistrationUseCase(finalRequest)
                .onSuccess {
                    _uiState.update {
                        it.copy(isLoading = false, registrationComplete = true, formData = null) // Limpiamos
                    }
                }
                .onFailure { error ->
                    _uiState.update {
                        it.copy(isLoading = false, error = error.message)
                    }
                }
        }
    }
}