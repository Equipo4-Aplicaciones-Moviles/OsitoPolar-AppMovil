package com.example.ositopolarapp.features.subscriptions.presentation.state

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ositopolarapp.features.subscriptions.domain.model.Plan
import com.example.ositopolarapp.features.subscriptions.domain.usecase.GetAllPlansUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * UI state for Plans screen.
 */
data class PlansUiState(
    val isLoading: Boolean = false,
    val ownerPlans: List<Plan> = emptyList(),
    val providerPlans: List<Plan> = emptyList(),
    val error: String? = null,
    val selectedPlan: Plan? = null,
    val selectedUserType: String = "Owner" // "Owner" or "Provider"
)

/**
 * ViewModel for Plans/Subscription selection screen.
 * Handles fetching plans and user selection.
 */
class PlansViewModel(
    private val getAllPlansUseCase: GetAllPlansUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(PlansUiState())
    val uiState: StateFlow<PlansUiState> = _uiState.asStateFlow()

    init {
        loadPlans()
    }

    /**
     * Loads all plans for both Owners and Providers.
     */
    fun loadPlans() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            // Load Owner plans
            val ownerPlansResult = getAllPlansUseCase("Owner")

            // Load Provider plans
            val providerPlansResult = getAllPlansUseCase("Provider")

            val ownerPlans = ownerPlansResult.getOrElse { emptyList() }
            val providerPlans = providerPlansResult.getOrElse { emptyList() }

            if (ownerPlansResult.isFailure && providerPlansResult.isFailure) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = "Failed to load plans. Please try again."
                    )
                }
            } else {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        ownerPlans = ownerPlans,
                        providerPlans = providerPlans,
                        error = null
                    )
                }
            }
        }
    }

    /**
     * Selects a plan for registration.
     */
    fun selectPlan(plan: Plan) {
        _uiState.update {
            it.copy(selectedPlan = plan)
        }
    }

    /**
     * Clears the selected plan.
     */
    fun clearSelectedPlan() {
        _uiState.update {
            it.copy(selectedPlan = null)
        }
    }

    /**
     * Switches between Owner and Provider tabs.
     */
    fun switchUserType(userType: String) {
        _uiState.update {
            it.copy(selectedUserType = userType)
        }
    }

    /**
     * Clears the error message.
     */
    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }
}
