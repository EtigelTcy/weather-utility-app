package au.edu.jcu.assessment.utilityapp.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import au.edu.jcu.assessment.utilityapp.data.model.City
import au.edu.jcu.assessment.utilityapp.data.model.WeatherInfo
import au.edu.jcu.assessment.utilityapp.data.repository.WeatherRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * Possible states for the weather UI.
 * Using a sealed class makes it easy to handle each case in the Composable.
 */
sealed class WeatherUiState {
    object Loading : WeatherUiState()
    data class Success(val weather: WeatherInfo) : WeatherUiState()
    data class Error(val message: String) : WeatherUiState()
}

/** Simple enum for the temperature unit setting. */
enum class TemperatureUnit { CELSIUS, FAHRENHEIT }

/**
 * ViewModel for the Weather Utility App.
 *
 * Holds the weather data state and user settings (city, temperature unit).
 * Survives configuration changes like screen rotation, so the user
 * doesn't lose their data when they flip their phone.
 */
class WeatherViewModel(private val repository: WeatherRepository) : ViewModel() {

    // List of cities the user can pick from in settings
    val availableCities = listOf(
        City("Townsville", -19.26, 146.80),
        City("Sydney", -33.87, 151.21),
        City("Melbourne", -37.81, 144.96),
        City("Brisbane", -27.47, 153.03),
        City("Perth", -31.95, 115.86)
    )

    // Currently selected city — Townsville by default (JCU's home!)
    private val _selectedCity = MutableStateFlow(availableCities[0])
    val selectedCity: StateFlow<City> = _selectedCity.asStateFlow()

    // Temperature unit preference — Celsius by default
    private val _temperatureUnit = MutableStateFlow(TemperatureUnit.CELSIUS)
    val temperatureUnit: StateFlow<TemperatureUnit> = _temperatureUnit.asStateFlow()

    // The main weather data state that the UI observes
    private val _uiState = MutableStateFlow<WeatherUiState>(WeatherUiState.Loading)
    val uiState: StateFlow<WeatherUiState> = _uiState.asStateFlow()

    // Load weather as soon as the ViewModel is created
    init {
        loadWeather()
    }

    /**
     * Fetches weather data for the currently selected city.
     * Updates the UI state to Loading -> Success or Error.
     */
    fun loadWeather() {
        viewModelScope.launch {
            _uiState.value = WeatherUiState.Loading
            try {
                val city = _selectedCity.value
                val weather = repository.getWeather(city.latitude, city.longitude)
                _uiState.value = WeatherUiState.Success(weather)
            } catch (e: Exception) {
                _uiState.value = WeatherUiState.Error(
                    "Could not load weather. Check your internet connection."
                )
            }
        }
    }

    /** Called when the user picks a different city in settings. */
    fun selectCity(city: City) {
        _selectedCity.value = city
        loadWeather()  // automatically refresh for the new city
    }

    /** Called when the user toggles the temperature unit. */
    fun setTemperatureUnit(unit: TemperatureUnit) {
        _temperatureUnit.value = unit
    }

    /**
     * Converts a Celsius temperature to a display string
     * based on the user's unit preference.
     */
    fun displayTemperature(celsius: Double): String {
        return when (_temperatureUnit.value) {
            TemperatureUnit.CELSIUS -> "${celsius.toInt()}°C"
            TemperatureUnit.FAHRENHEIT -> "${(celsius * 9.0 / 5.0 + 32.0).toInt()}°F"
        }
    }

    /**
     * Factory for creating this ViewModel with the repository dependency.
     * We need this because ViewModels normally can't take constructor parameters.
     */
    companion object {
        fun provideFactory(repository: WeatherRepository): ViewModelProvider.Factory {
            return object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return WeatherViewModel(repository) as T
                }
            }
        }
    }
}
