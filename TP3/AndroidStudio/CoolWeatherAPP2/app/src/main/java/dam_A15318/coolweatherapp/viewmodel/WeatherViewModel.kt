package dam_A15318.coolweatherapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dam_A15318.coolweatherapp.data.WeatherApiClient
import dam_A15318.coolweatherapp.ui.WeatherUIState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class WeatherViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(WeatherUIState())
    val uiState: StateFlow<WeatherUIState> = _uiState.asStateFlow()

    init {
        fetchWeather()
    }

    fun updateLatitude(lat: Float) {
        _uiState.update { it.copy(latitude = lat) }
    }

    fun updateLongitude(lon: Float) {
        _uiState.update { it.copy(longitude = lon) }
    }

    fun fetchWeather() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            val lat = _uiState.value.latitude
            val lon = _uiState.value.longitude
            val data = WeatherApiClient.getWeather(lat, lon)
            if (data != null) {
                _uiState.update {
                    it.copy(
                        latitude = data.latitude,
                        longitude = data.longitude,
                        temperature = data.current_weather.temperature,
                        windspeed = data.current_weather.windspeed,
                        winddirection = data.current_weather.winddirection,
                        weathercode = data.current_weather.weathercode,
                        seaLevelPressure = data.hourly.pressure_msl
                            .getOrElse(12) { 0.0 }.toFloat(),
                        time = data.current_weather.time,
                        isLoading = false
                    )
                }
            } else {
                _uiState.update {
                    it.copy(isLoading = false, errorMessage = "Erro ao obter dados")
                }
            }
        }
    }
}