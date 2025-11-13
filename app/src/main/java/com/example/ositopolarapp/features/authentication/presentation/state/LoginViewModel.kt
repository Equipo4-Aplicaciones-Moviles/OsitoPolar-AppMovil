package com.example.ositopolarapp.features.authentication.presentation.state

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ositopolarapp.features.authentication.domain.model.AuthenticatedUserEntity
import com.example.ositopolarapp.features.authentication.domain.usecase.SignInUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

// Define los estados posibles de la UI de Login
data class LoginUiState(
    val isLoading: Boolean = false,
    val loginSuccess: Boolean = false,
    val requires2FA: Boolean = false,
    val error: String? = null,
    val user: AuthenticatedUserEntity? = null // Para guardar datos si pide 2FA
)

class LoginViewModel(
    private val signInUseCase: SignInUseCase
    // TODO: Aquí también irá el VerifyTwoFactorUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun signIn(username: String, password: String) {
        // Validación simple
        if (username.isBlank() || password.isBlank()) {
            _uiState.update { it.copy(error = "Usuario y contraseña requeridos") }
            return
        }

        viewModelScope.launch {
            // 1. Poner estado de "cargando"
            _uiState.update { it.copy(isLoading = true, error = null) }

            // 2. Llamar al UseCase
            signInUseCase(username, password)
                .onSuccess { user ->
                    // 3. Éxito

                    // TODO: Manejar el 2FA
                    if (user.requires2FA) {
                        _uiState.update {
                            it.copy(isLoading = false, requires2FA = true, user = user)
                        }
                    } else {
                        // ¡Login exitoso!
                        // TODO: Guardar el token en Room/DataStore
                        _uiState.update {
                            it.copy(isLoading = false, loginSuccess = true, user = user)
                        }
                    }
                }
                .onFailure { error ->
                    // 4. Fracaso
                    _uiState.update {
                        it.copy(isLoading = false, error = error.message ?: "Error desconocido")
                    }
                }
        }
    }
}