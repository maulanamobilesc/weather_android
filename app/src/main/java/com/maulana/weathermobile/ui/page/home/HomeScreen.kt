package com.maulana.weathermobile.ui.page.home

import android.Manifest
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.maulana.warehouse.core.component.LoadingComponent
import com.maulana.warehouse.core.component.PageErrorMessageHandler
import com.maulana.warehouse.core.component.Spacer
import com.maulana.warehouse.domain.UIState
import com.maulana.warehouse.util.GlobalDimension
import com.maulana.weathermobile.core.component.RationaleAlert
import com.maulana.weathermobile.core.component.WeatherIcon
import com.maulana.weathermobile.core.component.WeatherIconSize
import com.maulana.weathermobile.domain.model.CurrentWeather
import com.maulana.weathermobile.domain.model.Forecast
import com.maulana.weathermobile.util.convertToHourFormat
import com.maulana.weathermobile.util.hasLocationPermission
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun HomeScreen(
    navController: NavHostController,
    snackBarState: SnackbarHostState,
    homeViewModel: HomeViewModel = hiltViewModel()
) {
    homeViewModel.apply {
        val currentWeatherUIState by currentWeatherUiState.collectAsState()
        val forecastUiState by forecastUiState.collectAsState()

        val permissionState = rememberMultiplePermissionsState(
            permissions = listOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        )

        val context = LocalContext.current

        if (!context.hasLocationPermission()) {
            LaunchedEffect(true) {
                permissionState.launchMultiplePermissionRequest()
            }
        } else {
            LaunchedEffect(true) {
                getCurrentLocation(context)
            }
        }

        if (currentWeatherUIState is UIState.Error) {
            PageErrorMessageHandler((currentWeatherUIState as UIState.Error).message, snackBarState)
        } else {
            HomeContent(this, navController, currentWeatherUIState, forecastUiState)
        }

        when {
            permissionState.allPermissionsGranted -> {
                LaunchedEffect(Unit) {
                    getCurrentLocation(context)
                }
            }

            permissionState.shouldShowRationale -> {
                RationaleAlert(onDismiss = { }) {
                    permissionState.launchMultiplePermissionRequest()
                }
            }
        }
    }
}

@Composable
fun HomeContent(
    homeViewModel: HomeViewModel,
    navController: NavHostController,
    currentWeatherUIState: UIState<CurrentWeather>,
    forecastUiState: UIState<List<Forecast>>,
) {
    LazyColumn(Modifier.fillMaxSize()) {
        item {
            if (currentWeatherUIState is UIState.Loading) {
                LoadingComponent()
            } else if (currentWeatherUIState is UIState.Success) {
                MainContent(currentWeatherUIState.data)
            }
        }
        item {
            if (forecastUiState is UIState.Loading) {
                LoadingComponent()
            } else if (forecastUiState is UIState.Success) {
                ForecastContent(forecastUiState.data)
            }
        }
    }
}

@Composable
fun ForecastContent(forecasts: List<Forecast>) {
    Column(Modifier.fillMaxWidth()) {
        Text(text = getCurrentDate())
        LazyRow(modifier = Modifier.fillMaxWidth()) {
            itemsIndexed(items = forecasts) { index, item ->
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(GlobalDimension.smallPadding)
                ) {
                    Text(item.dtTxt.orEmpty().convertToHourFormat())
                    WeatherIcon(item.weather?.first()?.icon, WeatherIconSize.SMALL)
                    Text("${item.main?.tempMin}°C/${item.main?.tempMax}°C")
                    Text("${item.pop?.times(100)?.toInt()}% rain")
                }
            }
        }
    }
}

@Composable
fun MainContent(currentWeather: CurrentWeather) {
    Card(
        shape = RoundedCornerShape(30.dp),
        modifier = Modifier.padding(GlobalDimension.sectionPadding)
    ) {
        Column(
            Modifier
                .fillMaxWidth()
                .padding(GlobalDimension.smallPadding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(onClick = {}) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = "Add Icon")
                }
                Text(text = currentWeather.name.orEmpty())
                IconButton(onClick = { }) {
                    Icon(Icons.Default.MoreVert, contentDescription = "More")
                }
            }
            WeatherIcon(currentWeather.weather?.first()?.icon)
            Text(text = getCurrentDate())
            Text(
                "${(currentWeather.main?.temp ?: 0.0).toInt()}°C",
                fontSize = GlobalDimension.contentTitleFontSize
            )
            Text(currentWeather.weather?.first()?.description.orEmpty())
            Spacer(GlobalDimension.sectionPadding)
            HorizontalDivider(
                thickness = 2.dp,
                color = Color.Gray
            )
            Spacer(GlobalDimension.sectionPadding)
            Row(Modifier.fillMaxWidth()) {
                Column(
                    Modifier.fillMaxWidth(0.5f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("${currentWeather.wind?.speed ?: 0.0} km/h")
                    Text("Wind")
                }
                Column(
                    Modifier.fillMaxWidth(0.5f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                }
            }
            Spacer(GlobalDimension.sectionPadding)
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(GlobalDimension.smallPadding)) {
                Column(
                    Modifier.fillMaxWidth(0.5f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("${currentWeather.main?.pressure ?: 0.0} mbar")
                    Text("Pressure")
                }
                Column(
                    Modifier.fillMaxWidth(0.5f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("${currentWeather.main?.humidity ?: 0.0} %")
                    Text("Humidity")
                }
            }
        }
    }
}

fun getCurrentDate(): String {
    val currentDate = LocalDate.now()
    val formatter = DateTimeFormatter.ofPattern("EEEE, MMMM dd") // "EEEE" for full day name
    return currentDate.format(formatter)
}
