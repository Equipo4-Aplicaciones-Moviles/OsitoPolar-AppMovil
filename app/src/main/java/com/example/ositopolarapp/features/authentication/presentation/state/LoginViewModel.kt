package com.example.ositopolarapp.features.authentication.presentation.state

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import com.example.ositopolarapp.features.authentication.domain.usecase.SignInUseCase
import com.example.ositopolarapp.features.authentication.domain.usecase.VerifyTwoFactorUseCase

data class LoginUiState(
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val error: String? = null,
    val requires2FA: Boolean = false,
    val tempUsername: String? = null
)

class LoginViewModel(
    private val signInUseCase: SignInUseCase,
    private val verifyTwoFactorUseCase: VerifyTwoFactorUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun signIn(username: String, pass: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            // signInUseCase ahora devuelve un Result<SignInResponse>
            signInUseCase(username, pass)
                .onSuccess { response ->
                    // CORRECCIÓN: Usamos la respuesta del DTO (SignInResponse)
                    // Si tu backend indica 2FA en el login, úsalo aquí. Si no, asumimos éxito directo.
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            isSuccess = true, // Navegar al dashboard
                            requires2FA = false // O response.requires2FA si lo agregaste al DTO
                        )
                    }
                }
                .onFailure { error ->
                    _uiState.update { it.copy(isLoading = false, error = error.message) }
                }
        }
    }

    // Función extra si necesitas 2FA
    fun verify2FA(code: String) {
        val username = _uiState.value.tempUsername ?: return
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            verifyTwoFactorUseCase(username, code)
                .onSuccess {
                    _uiState.update { it.copy(isLoading = false, isSuccess = true) }
                }
                .onFailure { error ->
                    _uiState.update { it.copy(isLoading = false, error = error.message) }
                }
        }
    }
}