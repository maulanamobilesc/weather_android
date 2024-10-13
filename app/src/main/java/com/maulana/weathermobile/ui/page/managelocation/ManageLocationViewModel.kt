package com.maulana.weathermobile.ui.page.managelocation

import android.content.SharedPreferences
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maulana.warehouse.domain.UIState
import com.maulana.weathermobile.BuildConfig
import com.maulana.weathermobile.data.mapper.toWeatherLocal
import com.maulana.weathermobile.domain.LocationIntent
import com.maulana.weathermobile.domain.model.CurrentWeather
import com.maulana.weathermobile.domain.model.LocationLocal
import com.maulana.weathermobile.domain.model.WeatherLocal
import com.maulana.weathermobile.domain.repository.LocationRepository
import com.maulana.weathermobile.domain.repository.WeatherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ManageLocationViewModel @Inject constructor(
    private val weatherRepository: WeatherRepository,
    private val locationRepository: LocationRepository,
    private val dispatcher: CoroutineDispatcher,
    sharedPreferences: SharedPreferences
) : ViewModel() {

    val query = mutableStateOf("")

    private val _savedLocationUiState = MutableStateFlow<UIState<List<LocationLocal>>>(UIState.Init)
    val savedLocationUiState: StateFlow<UIState<List<LocationLocal>>> = _savedLocationUiState

    private val _cityWeatherUiState = MutableStateFlow<UIState<CurrentWeather>>(UIState.Init)
    val cityWeatherUiState: StateFlow<UIState<CurrentWeather>> = _cityWeatherUiState

    private val _savedWeatherUiState = MutableStateFlow<UIState<List<WeatherLocal>>>(UIState.Init)
    val savedWeatherUiState: StateFlow<UIState<List<WeatherLocal>>> = _savedWeatherUiState

    private val _searchResultUiState = MutableStateFlow<UIState<List<LocationLocal>>>(UIState.Init)
    val searchResultUiState: StateFlow<UIState<List<LocationLocal>>> = _searchResultUiState


    fun processIntent(intent: LocationIntent) {
        when (intent) {
            is LocationIntent.LoadSavedWeather -> getSavedWeather()
            is LocationIntent.SearchLocation -> searchLocation()
            is LocationIntent.GetWeatherFromSelectedCity -> getCurrentWeather(intent.locationLocal)
            is LocationIntent.InsertCurrentWeather -> insertCurrentWeather(intent.weather)
        }
    }

    private fun getSavedWeather() {
        viewModelScope.launch(dispatcher) {
            weatherRepository.getAllSavedWeather().onStart {
                _savedWeatherUiState.value = UIState.Loading
            }.collectLatest { list ->
                _savedWeatherUiState.value = UIState.Success(list)
            }
        }
    }

    private fun searchLocation() {
        viewModelScope.launch(dispatcher) {
            locationRepository.searchLocation(query.value, BuildConfig.API_KEY).onStart {
                _searchResultUiState.value = UIState.Loading
            }.collectLatest { list ->
                _searchResultUiState.value = list
            }
        }
    }

    private fun getCurrentWeather(locationLocal: LocationLocal) {
        viewModelScope.launch(dispatcher) {
            weatherRepository.getCurrentWeather(
                apiKey = BuildConfig.API_KEY,
                latitude = locationLocal.latitude,
                longitude = locationLocal.longitude
            ).onStart {
                _cityWeatherUiState.value = UIState.Loading
            }.collect { state ->
                _cityWeatherUiState.value = state
                if (state is UIState.Success) {
                    processIntent(
                        LocationIntent.InsertCurrentWeather(
                            state.data.toWeatherLocal(
                                weatherRepository.getDataCount() + 1
                            )
                        )
                    )
                }
            }
        }
    }

    private fun insertCurrentWeather(weatherLocal: WeatherLocal) {
        viewModelScope.launch(dispatcher) {
            runCatching {
                weatherRepository.insertWeather(weatherLocal)
            }.onFailure {

            }.onSuccess {

            }
        }
    }

}