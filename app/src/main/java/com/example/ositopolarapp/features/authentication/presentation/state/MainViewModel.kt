package com.example.ositopolarapp.features.authentication.presentation.state

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ositopolarapp.features.authentication.domain.model.AuthenticatedUserEntity
import com.example.ositopolarapp.features.authentication.domain.usecase.CheckAuthUseCase
import com.example.ositopolarapp.features.authentication.domain.usecase.Disable2FAUseCase
import com.example.ositopolarapp.features.authentication.domain.usecase.Enable2FAUseCase
import com.example.ositopolarapp.features.authentication.domain.usecase.GetCurrentUserUseCase
import com.example.ositopolarapp.features.authentication.domain.usecase.LogoutUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * Define el estado inicial de la aplicación: ¿Está el usuario logueado o no?
 */
sealed interface AuthState {
    object Loading : AuthState // Estado inicial, comprobando Room
    object LoggedIn : AuthState
    object LoggedOut : AuthState
}

class MainViewModel(
    private val checkAuthUseCase: CheckAuthUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val logoutUseCase: LogoutUseCase,
    private val enable2FAUseCase: Enable2FAUseCase,
    private val disable2FAUseCase: Disable2FAUseCase
) : ViewModel() {

    private val _authState = MutableStateFlow<AuthState>(AuthState.Loading)
    val authState: StateFlow<AuthState> = _authState.asStateFlow()

    private val _currentUser = MutableStateFlow<AuthenticatedUserEntity?>(null)
    val currentUser: StateFlow<AuthenticatedUserEntity?> = _currentUser.asStateFlow()

    init {
        checkAuthenticationStatus()
        observeCurrentUser()
    }

    private fun checkAuthenticationStatus() {
        viewModelScope.launch {
            // El Flow de Room nos dirá si existe un token
            checkAuthUseCase().collect { token ->
                // NOTA: Collect es automático, si el token se borra (signOut), esto se reejecutará.
                _authState.value = if (token.isNullOrEmpty()) {
                    AuthState.LoggedOut
                } else {
                    AuthState.LoggedIn
                }
            }
        }
    }

    private fun observeCurrentUser() {
        viewModelScope.launch {
            getCurrentUserUseCase().collect { user ->
                _currentUser.value = user
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            logoutUseCase()
            // No need to manually update authState - it will automatically
            // update via the Flow from Room when the token is deleted
        }
    }

    suspend fun enable2FA(username: String): Result<Unit> {
        return enable2FAUseCase(username)
    }

    suspend fun disable2FA(username: String): Result<Unit> {
        return disable2FAUseCase(username)
    }
}