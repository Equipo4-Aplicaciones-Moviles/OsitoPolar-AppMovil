package com.example.ositopolarapp.features.authentication.presentation.state

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ositopolarapp.features.authentication.domain.model.AuthenticatedUserEntity
import com.example.ositopolarapp.features.authentication.domain.usecase.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

enum class AuthState {
    Loading, LoggedIn, LoggedOut
}

class MainViewModel(
    private val checkAuthUseCase: CheckAuthUseCase,
    private val logoutUseCase: LogoutUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val enable2FAUseCase: Enable2FAUseCase,
    private val disable2FAUseCase: Disable2FAUseCase
) : ViewModel() {

    private val _authState = MutableStateFlow(AuthState.Loading)
    val authState: StateFlow<AuthState> = _authState.asStateFlow()

    private val _currentUser = MutableStateFlow<AuthenticatedUserEntity?>(null)
    val currentUser: StateFlow<AuthenticatedUserEntity?> = _currentUser.asStateFlow()

    init {
        checkAuthStatus()
    }

    private fun checkAuthStatus() {
        viewModelScope.launch {
            checkAuthUseCase()
                .onSuccess { isAuthenticated ->
                    if (isAuthenticated) {
                        _authState.value = AuthState.LoggedIn
                        loadCurrentUser()
                    } else {
                        _authState.value = AuthState.LoggedOut
                    }
                }
                .onFailure {
                    _authState.value = AuthState.LoggedOut
                }
        }
    }

    private fun loadCurrentUser() {
        viewModelScope.launch {
            getCurrentUserUseCase().onSuccess { user ->
                _currentUser.value = user
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            logoutUseCase()
            _authState.value = AuthState.LoggedOut
            _currentUser.value = null
        }
    }

    fun enable2FA(username: String) {
        viewModelScope.launch { enable2FAUseCase(username) }
    }

    fun disable2FA(username: String) {
        viewModelScope.launch { disable2FAUseCase(username) }
    }
}