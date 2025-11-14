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
import com.example.ositopolarapp.features.authentication.domain.usecase.VerifyTwoFactorUseCase
// Define los estados posibles de la UI de Login
data class LoginUiState(
    val isLoading: Boolean = false,
    val loginSuccess: Boolean = false,
    val requires2FA: Boolean = false,
    val requiresTwoFactorSetup: Boolean = false,
    val error: String? = null,
    val isVerifying2FA: Boolean = false,
    val user: AuthenticatedUserEntity? = null // Para guardar datos si pide 2FA
)

class LoginViewModel(
    private val signInUseCase: SignInUseCase,
    private val verifyTwoFactorUseCase: VerifyTwoFactorUseCase
    // TODO: Aquí también irá el VerifyTwoFactorUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()
    private var pendingUser: AuthenticatedUserEntity? = null
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

                    when {
                        // First login: needs 2FA setup
                        user.requiresTwoFactorSetup -> {
                            pendingUser = user
                            _uiState.update {
                                it.copy(
                                    isLoading = false,
                                    requiresTwoFactorSetup = true,
                                    user = user
                                )
                            }
                        }
                        // Subsequent login: needs 2FA verification
                        user.requires2FA -> {
                            pendingUser = user
                            _uiState.update {
                                it.copy(
                                    isLoading = false,
                                    requires2FA = true,
                                    user = user
                                )
                            }
                        }
                        // No 2FA: login successful
                        else -> {
                            _uiState.update {
                                it.copy(isLoading = false, loginSuccess = true, user = user)
                            }
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

    fun verify2FACode(code: String) {
        val userToVerify = _uiState.value.user // Usamos el usuario que ya está en el estado

        if (userToVerify == null || code.isBlank()) {
            _uiState.update { it.copy(requires2FA = false, error = "Error de sesión. Vuelve a iniciar sesión.") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isVerifying2FA = true, error = null) }

            verifyTwoFactorUseCase(userToVerify.username, code)
                .onSuccess { finalUser ->
                    // Éxito: Login completo!
                    _uiState.update {
                        it.copy(isVerifying2FA = false, requires2FA = false, loginSuccess = true, user = finalUser)
                    }
                    // El token ya se guardó en Room dentro del AuthRepositoryImpl
                }
                .onFailure { error ->
                    // Fracaso: Código incorrecto
                    _uiState.update {
                        it.copy(isVerifying2FA = false, error = error.message ?: "Código 2FA incorrecto.")
                    }
                }
        }
    }

    // 5. Función para que la UI pueda cerrar el diálogo de 2FA
    fun dismiss2FADialog() {
        // CORRECCIÓN: Quitamos 'pendingUser = null' y limpiamos el 'user' y el 'error'.
        _uiState.update {
            it.copy(
                requires2FA = false,
                requiresTwoFactorSetup = false,
                isVerifying2FA = false,
                user = null, // Limpiamos el usuario temporal
                error = null // Limpiamos el error
            )
        }
    }
}