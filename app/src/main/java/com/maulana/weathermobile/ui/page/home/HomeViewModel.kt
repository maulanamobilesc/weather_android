package com.maulana.weathermobile.ui.page.home

import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maulana.warehouse.domain.UIState
import com.maulana.weathermobile.BuildConfig
import com.maulana.weathermobile.domain.model.Coord
import com.maulana.weathermobile.domain.model.CurrentWeather
import com.maulana.weathermobile.domain.model.Forecast
import com.maulana.weathermobile.domain.repository.LocationRepository
import com.maulana.weathermobile.domain.repository.WeatherRepository
import com.maulana.weathermobile.util.hasLocationPermission
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val weatherRepository: WeatherRepository,
    private val locationRepository: LocationRepository,
    private val dispatcher: CoroutineDispatcher, private val sharedPreferences: SharedPreferences
) : ViewModel() {

    private val _currentWeatherUiState = MutableStateFlow<UIState<CurrentWeather>>(UIState.Init)
    val currentWeatherUiState: StateFlow<UIState<CurrentWeather>> = _currentWeatherUiState

    private val _forecastUiState = MutableStateFlow<UIState<List<Forecast>>>(UIState.Init)
    val forecastUiState: StateFlow<UIState<List<Forecast>>> = _forecastUiState

    fun getCurrentLocation(context: Context) {
        if (!context.hasLocationPermission()) {
            _currentWeatherUiState.value = UIState.Error("Missing location permission")
        } else {
            viewModelScope.launch(dispatcher) {
                runCatching {
                    locationRepository.getCurrentLocation(context).onStart {
                        _currentWeatherUiState.value = UIState.Loading
                    }
                }.onFailure {
                    _currentWeatherUiState.value = UIState.Error(it.message.orEmpty())
                }.onSuccess { result ->
                    result.collectLatest { state ->
                        if (state is UIState.Success) {
                            getCurrentWeather(state.data)
                            getForecast(state.data)
                        }
                    }
                }
            }
        }
    }

    private fun getCurrentWeather(coordinate: Coord) {
        viewModelScope.launch(dispatcher) {
            weatherRepository.getCurrentWeather(
                apiKey = BuildConfig.API_KEY,
                latitude = coordinate.lat ?: 0.0,
                longitude = coordinate.lon ?: 0.0
            ).onStart {
                _currentWeatherUiState.value = UIState.Loading
            }.collect { state ->
                _currentWeatherUiState.value = state
            }
        }
    }

    private fun getForecast(coordinate: Coord) {
        viewModelScope.launch(dispatcher) {
            weatherRepository.getForecast(
                apiKey = BuildConfig.API_KEY, latitude = coordinate.lat ?: 0.0,
                longitude = coordinate.lon ?: 0.0
            ).onStart {
                _forecastUiState.value = UIState.Loading
            }.collect { state ->
                _forecastUiState.value = state
            }
        }
    }


}