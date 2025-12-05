package com.example.ositopolarapp.features.rentals.presentation.viewmodel

import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ositopolarapp.features.payments.data.api.PaymentApiService
import com.example.ositopolarapp.features.payments.data.dto.CompleteRentalRequest
import com.example.ositopolarapp.features.rentals.domain.model.RentalEquipment
import com.example.ositopolarapp.features.rentals.domain.usecase.CreateRentalRequestUseCase
import com.example.ositopolarapp.features.rentals.domain.usecase.GetRentalEquipmentUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * State for Rental Catalog Screen
 */
data class RentalCatalogUiState(
    val isLoading: Boolean = false,
    val equipment: List<RentalEquipment> = emptyList(),
    val error: String? = null,
    val checkoutUrl: String? = null,
    val selectedEquipment: RentalEquipment? = null,
    val rentalComplete: Boolean = false,
    val rentalCompletionError: String? = null
)

/**
 * ViewModel for Rental Catalog Screen
 *
 * Handles equipment listing, rental creation with Stripe checkout,
 * and completion of rental after payment success.
 */
class RentalCatalogViewModel(
    private val getRentalEquipmentUseCase: GetRentalEquipmentUseCase,
    private val createRentalRequestUseCase: CreateRentalRequestUseCase,
    private val paymentApiService: PaymentApiService,
    private val sharedPreferences: SharedPreferences
) : ViewModel() {

    companion object {
        private const val TAG = "RentalCatalogViewModel"
        private const val PREF_RENTAL_EQUIPMENT_ID = "rental_equipment_id"
        private const val PREF_RENTAL_MONTHS = "rental_months"
        private const val PREF_RENTAL_USER_ID = "rental_user_id"
    }

    private val _uiState = MutableStateFlow(RentalCatalogUiState())
    val uiState: StateFlow<RentalCatalogUiState> = _uiState.asStateFlow()

    init {
        loadRentalEquipment()
    }

    fun loadRentalEquipment() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            getRentalEquipmentUseCase()
                .onSuccess { equipmentList ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            equipment = equipmentList,
                            error = null
                        )
                    }
                }
                .onFailure { exception ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = exception.message ?: "Error al cargar equipos"
                        )
                    }
                }
        }
    }

    fun selectEquipment(equipment: RentalEquipment) {
        _uiState.update { it.copy(selectedEquipment = equipment) }
    }

    /**
     * Creates a rental request and gets a Stripe checkout URL.
     * Saves rental data to SharedPreferences before opening Stripe,
     * so it can be recovered when the user returns from payment.
     *
     * @param equipmentId The ID of the equipment to rent
     * @param months Number of months to rent
     * @param userId The current user's ID (needed for completing the rental)
     */
    fun createRentalRequest(equipmentId: Int, months: Int, userId: Int = 0) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            // Save rental data BEFORE opening Stripe (in case of process death)
            saveRentalData(equipmentId, months, userId)
            Log.d(TAG, "Saved rental data: equipmentId=$equipmentId, months=$months, userId=$userId")

            // Use deep links directly - Stripe supports custom URL schemes
            val successUrl = "ositopolar://rental/success"
            val cancelUrl = "ositopolar://rental/cancel"

            createRentalRequestUseCase(
                equipmentId = equipmentId,
                months = months,
                successUrl = successUrl,
                cancelUrl = cancelUrl
            )
                .onSuccess { response ->
                    Log.d(TAG, "Rental request created, checkout URL: ${response.checkoutUrl}")
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            checkoutUrl = response.checkoutUrl,
                            error = null
                        )
                    }
                }
                .onFailure { exception ->
                    Log.e(TAG, "Failed to create rental request", exception)
                    // Clear saved data on failure
                    clearRentalData()
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = exception.message ?: "Error al crear solicitud de renta"
                        )
                    }
                }
        }
    }

    /**
     * Saves rental data to SharedPreferences for recovery after payment
     */
    private fun saveRentalData(equipmentId: Int, months: Int, userId: Int) {
        sharedPreferences.edit()
            .putInt(PREF_RENTAL_EQUIPMENT_ID, equipmentId)
            .putInt(PREF_RENTAL_MONTHS, months)
            .putInt(PREF_RENTAL_USER_ID, userId)
            .apply()
    }

    /**
     * Retrieves saved rental data from SharedPreferences
     */
    private fun getSavedRentalData(): Triple<Int, Int, Int>? {
        val equipmentId = sharedPreferences.getInt(PREF_RENTAL_EQUIPMENT_ID, -1)
        val months = sharedPreferences.getInt(PREF_RENTAL_MONTHS, -1)
        val userId = sharedPreferences.getInt(PREF_RENTAL_USER_ID, -1)

        return if (equipmentId > 0 && months > 0 && userId > 0) {
            Triple(equipmentId, months, userId)
        } else {
            null
        }
    }

    /**
     * Clears saved rental data from SharedPreferences
     */
    private fun clearRentalData() {
        sharedPreferences.edit()
            .remove(PREF_RENTAL_EQUIPMENT_ID)
            .remove(PREF_RENTAL_MONTHS)
            .remove(PREF_RENTAL_USER_ID)
            .apply()
    }

    /**
     * Completes the rental after a successful Stripe payment.
     * Retrieves saved rental data and calls the backend to finalize the rental.
     *
     * @param sessionId The Stripe session ID from the success deep link
     */
    fun completeRental(sessionId: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, rentalCompletionError = null) }

            val savedData = getSavedRentalData()
            if (savedData == null) {
                Log.e(TAG, "No saved rental data found!")
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        rentalCompletionError = "Error: No se encontraron los datos de la renta. Por favor intenta de nuevo."
                    )
                }
                return@launch
            }

            val (equipmentId, months, userId) = savedData
            Log.d(TAG, "Completing rental: sessionId=$sessionId, equipmentId=$equipmentId, months=$months, userId=$userId")

            try {
                val request = CompleteRentalRequest(
                    userId = userId,
                    equipmentId = equipmentId,
                    sessionId = sessionId,
                    rentalMonths = months
                )

                val response = paymentApiService.completeRental(request)

                if (response.isSuccessful) {
                    Log.i(TAG, "Rental completed successfully!")
                    // Clear saved data after successful completion
                    clearRentalData()
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            rentalComplete = true,
                            rentalCompletionError = null
                        )
                    }
                } else {
                    val errorBody = response.errorBody()?.string()
                    Log.e(TAG, "Failed to complete rental: ${response.code()} - $errorBody")
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            rentalCompletionError = "Error al completar la renta: ${response.message()}"
                        )
                    }
                }
            } catch (e: Exception) {
                Log.e(TAG, "Exception completing rental", e)
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        rentalCompletionError = "Error de conexión: ${e.message}"
                    )
                }
            }
        }
    }

    /**
     * Resets the rental completion state
     */
    fun resetRentalCompletionState() {
        _uiState.update {
            it.copy(
                rentalComplete = false,
                rentalCompletionError = null
            )
        }
    }

    fun clearCheckoutUrl() {
        _uiState.update { it.copy(checkoutUrl = null) }
    }

    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }
}
