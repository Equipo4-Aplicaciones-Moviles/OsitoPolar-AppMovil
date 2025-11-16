package com.example.ositopolarapp.features.analytics.presentation.state

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ositopolarapp.features.analytics.data.dto.*
import com.example.ositopolarapp.features.analytics.domain.repository.AnalyticsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * UI State for Analytics
 */
data class AnalyticsUiState(
    val isLoading: Boolean = false,
    val error: String? = null,

    // Readings data (temperature and energy)
    val readings: List<ReadingDto> = emptyList(),
    val readingsTotal: Int = 0,

    // Summaries data (daily averages)
    val summaries: List<DailySummaryDto> = emptyList(),
    val summariesTotal: Int = 0,

    // Overview data (multi-equipment)
    val overviewEquipments: List<EquipmentOverviewDto> = emptyList(),
    val overviewSummary: OverviewSummaryDto? = null,

    // Advanced analytics
    val healthScore: HealthScoreResponse? = null,
    val anomalies: AnomaliesResponse? = null,
    val costAnalysis: CostAnalysisResponse? = null,
    val maintenanceForecast: MaintenanceForecastResponse? = null
)

/**
 * ViewModel for Equipment Analytics
 * Handles fetching and managing analytics data from the backend
 */
class AnalyticsViewModel(
    private val repository: AnalyticsRepository
) : ViewModel() {

    companion object {
        private const val TAG = "AnalyticsViewModel"
    }

    private val _uiState = MutableStateFlow(AnalyticsUiState())
    val uiState: StateFlow<AnalyticsUiState> = _uiState.asStateFlow()

    /**
     * Load equipment readings (temperature, energy, or all)
     * @param equipmentId Equipment ID
     * @param type "all", "temperature", or "energy"
     * @param hours Hours to look back (default: 24)
     * @param limit Max number of readings (default: 100)
     */
    fun loadReadings(
        equipmentId: Int,
        type: String = "all",
        hours: Int = 24,
        limit: Int = 100
    ) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            repository.getEquipmentReadings(equipmentId, type, hours, limit)
                .onSuccess { response ->
                    Log.d(TAG, "Loaded ${response.data.size} readings for equipment $equipmentId")
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            readings = response.data,
                            readingsTotal = response.total
                        )
                    }
                }
                .onFailure { error ->
                    Log.e(TAG, "Failed to load readings: ${error.message}", error)
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = error.message ?: "Failed to load readings"
                        )
                    }
                }
        }
    }

    /**
     * Load equipment summaries (daily averages)
     * @param equipmentId Equipment ID
     * @param type "daily-averages" (default)
     * @param days Days to look back (default: 7)
     */
    fun loadSummaries(
        equipmentId: Int,
        type: String = "daily-averages",
        days: Int = 7
    ) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            repository.getEquipmentSummaries(equipmentId, type, days)
                .onSuccess { response ->
                    Log.d(TAG, "Loaded ${response.data.size} summaries for equipment $equipmentId")
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            summaries = response.data,
                            summariesTotal = response.total
                        )
                    }
                }
                .onFailure { error ->
                    Log.e(TAG, "Failed to load summaries: ${error.message}", error)
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = error.message ?: "Failed to load summaries"
                        )
                    }
                }
        }
    }

    /**
     * Load readings and summaries together
     * Useful for initial screen load
     */
    fun loadAnalytics(equipmentId: Int) {
        loadReadings(equipmentId, type = "all", hours = 24)
        loadSummaries(equipmentId, type = "daily-averages", days = 7)
    }

    /**
     * Load overview for multiple equipment (dashboard view)
     * @param equipmentIds List of equipment IDs
     * @param type "current" (default)
     */
    fun loadOverview(equipmentIds: List<Int>, type: String = "current") {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            repository.getEquipmentOverview(equipmentIds, type)
                .onSuccess { response ->
                    Log.d(TAG, "Loaded overview for ${response.equipments.size} equipment")
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            overviewEquipments = response.equipments,
                            overviewSummary = response.summary
                        )
                    }
                }
                .onFailure { error ->
                    Log.e(TAG, "Failed to load overview: ${error.message}", error)
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = error.message ?: "Failed to load overview"
                        )
                    }
                }
        }
    }

    /**
     * Refresh all analytics data
     */
    fun refresh(equipmentId: Int) {
        loadAnalytics(equipmentId)
    }

    /**
     * Clear error message
     */
    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }

    /**
     * Helper function to get temperature readings only
     */
    fun getTemperatureReadings(): List<ReadingDto> {
        return _uiState.value.readings.filter { it.type == "temperature" }
    }

    /**
     * Helper function to get energy readings only
     */
    fun getEnergyReadings(): List<ReadingDto> {
        return _uiState.value.readings.filter { it.type == "energy" }
    }
}
