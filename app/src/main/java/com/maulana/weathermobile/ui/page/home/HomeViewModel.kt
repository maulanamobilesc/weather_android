package com.maulana.weathermobile.ui.page.home

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maulana.warehouse.domain.UIState
import com.maulana.weathermobile.BuildConfig
import com.maulana.weathermobile.data.mapper.toCoord
import com.maulana.weathermobile.data.mapper.toWeatherLocal
import com.maulana.weathermobile.domain.WeatherIntent
import com.maulana.weathermobile.domain.model.Coord
import com.maulana.weathermobile.domain.model.CurrentWeather
import com.maulana.weathermobile.domain.model.Forecast
import com.maulana.weathermobile.domain.model.WeatherLocal
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
    private val dispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _currentWeatherUiState = MutableStateFlow<UIState<CurrentWeather>>(UIState.Init)
    val currentWeatherUiState: StateFlow<UIState<CurrentWeather>> = _currentWeatherUiState

    private val _forecastUiState = MutableStateFlow<UIState<List<Forecast>>>(UIState.Init)
    val forecastUiState: StateFlow<UIState<List<Forecast>>> = _forecastUiState

    private val _savedWeatherUiState = MutableStateFlow<UIState<List<WeatherLocal>>>(UIState.Init)
    val savedWeatherUiState: StateFlow<UIState<List<WeatherLocal>>> = _savedWeatherUiState

    private var _locationId = 0
    private var _activeLocation: WeatherLocal? = null

    private var _savedLocationList: List<WeatherLocal> = listOf()

    fun processIntent(intent: WeatherIntent) {
        when (intent) {
            is WeatherIntent.FetchCurrentWeather -> getCurrentWeather(
                intent.coordinate,
                intent.withInsert
            )

            is WeatherIntent.FetchForecast -> getForecast(intent.coordinate)
            is WeatherIntent.GetSavedWeather -> getSavedWeather(intent.context)
            is WeatherIntent.GetCurrentLocation -> getCurrentLocation(intent.context)
            is WeatherIntent.InsertCurrentWeather -> insertCurrentWeather(intent.weather)
            is WeatherIntent.FetchWeatherAndForecast -> getWeatherData(
                intent.coordinate,
                intent.withInsert
            )

            is WeatherIntent.SetActiveLocation -> setActiveLocation(intent.locationIndex)
        }
    }

    private fun setActiveLocation(locationIndex: Int) {
        if (_savedLocationList.isNotEmpty()) {
            _activeLocation = _savedLocationList[locationIndex]
            _locationId = locationIndex
            processIntent(WeatherIntent.FetchWeatherAndForecast(_activeLocation!!.toCoord()))
        }
    }

    private fun getSavedWeather(context: Context) {
        viewModelScope.launch(dispatcher) {
            weatherRepository.getAllSavedWeather().onStart {
                _savedWeatherUiState.value = UIState.Loading
            }.collectLatest { list ->
                if (list.isEmpty()) {
                    processIntent(WeatherIntent.GetCurrentLocation(context))
                } else {
                    _savedWeatherUiState.value = UIState.Success(list)
                    _savedLocationList = list
                    processIntent(WeatherIntent.SetActiveLocation(0))
                }
            }
        }
    }

    private fun getWeatherData(coordinate: Coord, withInsert: Boolean) {
        processIntent(WeatherIntent.FetchCurrentWeather(coordinate, withInsert))
        processIntent(WeatherIntent.FetchForecast(coordinate))
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

    private fun getCurrentLocation(context: Context) {
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
                            processIntent(WeatherIntent.FetchWeatherAndForecast(state.data, true))
                        }
                    }
                }
            }
        }
    }

    private fun getCurrentWeather(coordinate: Coord, withInsert: Boolean = false) {
        viewModelScope.launch(dispatcher) {
            weatherRepository.getCurrentWeather(
                apiKey = BuildConfig.API_KEY,
                latitude = coordinate.lat ?: 0.0,
                longitude = coordinate.lon ?: 0.0
            ).onStart {
                _currentWeatherUiState.value = UIState.Loading
            }.collect { state ->
                _currentWeatherUiState.value = state
                if (state is UIState.Success && withInsert) {
                    processIntent(
                        WeatherIntent.InsertCurrentWeather(
                            state.data.toWeatherLocal(
                                _locationId
                            )
                        )
                    )
                }
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