package com.example.ositopolarapp.features.authentication.presentation.state

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ositopolarapp.features.authentication.domain.usecase.CheckAuthUseCase
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
    private val checkAuthUseCase: CheckAuthUseCase
) : ViewModel() {

    private val _authState = MutableStateFlow<AuthState>(AuthState.Loading)
    val authState: StateFlow<AuthState> = _authState.asStateFlow()

    init {
        checkAuthenticationStatus()
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
}