package com.maulana.weathermobile.ui.page.home

import android.Manifest
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.maulana.warehouse.core.component.LoadingComponent
import com.maulana.warehouse.core.component.PageErrorMessageHandler
import com.maulana.warehouse.core.component.Spacer
import com.maulana.warehouse.domain.UIState
import com.maulana.weathermobile.R
import com.maulana.weathermobile.core.component.RationaleAlert
import com.maulana.weathermobile.core.component.WeatherIcon
import com.maulana.weathermobile.core.component.WeatherIconSize
import com.maulana.weathermobile.domain.WeatherIntent
import com.maulana.weathermobile.domain.model.CurrentWeather
import com.maulana.weathermobile.domain.model.Forecast
import com.maulana.weathermobile.domain.model.WeatherLocal
import com.maulana.weathermobile.ui.destinations.ManageLocationDestination
import com.maulana.weathermobile.util.AppColor
import com.maulana.weathermobile.util.GlobalDimension
import com.maulana.weathermobile.util.convertToHourFormat
import com.maulana.weathermobile.util.hasLocationPermission
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalPermissionsApi::class, ExperimentalMaterialApi::class)
@Composable
fun HomeScreen(
    navController: NavHostController,
    snackBarState: SnackbarHostState,
    homeViewModel: HomeViewModel = hiltViewModel()
) {
    homeViewModel.apply {
        val currentWeatherUiState by currentWeatherUiState.collectAsState()
        val forecastUiState by forecastUiState.collectAsState()
        val savedWeatherUiState by savedWeatherUiState.collectAsState()

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
                processIntent(WeatherIntent.GetSavedWeather(context))
            }
        }

        val pullRefreshState = rememberPullRefreshState(
            savedWeatherUiState is UIState.Loading,
            { processIntent(WeatherIntent.GetSavedWeather(context)) })

        Box(
            Modifier
                .fillMaxSize()
                .pullRefresh(pullRefreshState)
        ) {
            when (savedWeatherUiState) {
                is UIState.Error -> PageErrorMessageHandler(
                    (savedWeatherUiState as UIState.Error).message,
                    snackBarState
                )

                is UIState.Success -> HomeContent(
                    this@apply,
                    navController,
                    currentWeatherUiState,
                    forecastUiState,
                    savedWeatherUiState,
                    snackBarState
                )

                else -> LoadingComponent()
            }

            PullRefreshIndicator(
                savedWeatherUiState is UIState.Loading,
                pullRefreshState,
                Modifier.align(Alignment.TopCenter)
            )
        }


        when {
            permissionState.allPermissionsGranted -> {
                LaunchedEffect(Unit) {
                    processIntent(WeatherIntent.GetSavedWeather(context))
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
    savedWeatherUiState: UIState<List<WeatherLocal>>,
    snackBarState: SnackbarHostState
) {

    if (savedWeatherUiState is UIState.Success) {
        val locationList = savedWeatherUiState.data
        val pagerState = rememberPagerState(pageCount = { locationList.size })

        LaunchedEffect(pagerState.currentPage) {
            homeViewModel.processIntent(WeatherIntent.SetActiveLocation(pagerState.currentPage))
            homeViewModel.processIntent(WeatherIntent.FetchWeatherAndForecast())
        }

        HorizontalPager(state = pagerState) {
            LazyColumn(Modifier.fillMaxSize()) {
                item {
                    when (currentWeatherUIState) {
                        is UIState.Error -> PageErrorMessageHandler(
                            currentWeatherUIState.message,
                            snackBarState
                        )

                        is UIState.Success -> MainContent(
                            currentWeatherUIState.data,
                            navController,
                            pagerState
                        )

                        else -> LoadingComponent()
                    }
                }
                item {
                    when (forecastUiState) {
                        is UIState.Error -> PageErrorMessageHandler(
                            forecastUiState.message,
                            snackBarState
                        )

                        is UIState.Success -> ForecastContent(forecastUiState.data)
                        else -> LoadingComponent()
                    }
                }
            }
        }
    }
}

@Composable
fun ForecastContent(forecasts: List<Forecast>) {
    Column(
        Modifier
            .fillMaxWidth()
            .background(color = Color(0xFF2C79C1))
    ) {
        Text(
            text = getCurrentDate(),
            color = AppColor.textColor,
            modifier = Modifier.padding(GlobalDimension.sectionPadding),
            fontWeight = FontWeight.Bold
        )
        LazyRow(modifier = Modifier.fillMaxWidth()) {
            items(items = forecasts) { item ->
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(GlobalDimension.smallPadding)
                ) {
                    Text(
                        item.dtTxt.orEmpty().convertToHourFormat(),
                        color = AppColor.textColor,
                        fontWeight = FontWeight.Bold
                    )
                    WeatherIcon(item.weather?.first()?.icon, WeatherIconSize.SMALL)
                    Text(
                        "${item.main?.tempMin?.toInt()}°c/${item.main?.tempMax?.toInt()}°c",
                        color = AppColor.textColor
                    )
                    Text("${item.pop?.times(100)?.toInt()}% rain", color = AppColor.textColor)
                }
            }
        }
    }
}

@Composable
fun MainContent(
    currentWeather: CurrentWeather,
    navController: NavHostController,
    pagerState: PagerState
) {
    Card(
        shape = RoundedCornerShape(30.dp),
        elevation = CardDefaults.cardElevation(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(GlobalDimension.sectionPadding)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFF62B8F6),
                            Color(0xFF2C79C1)
                        )
                    ), alpha = 1.0f
                ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(GlobalDimension.sectionPadding),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(onClick = {
                    navController.navigate(ManageLocationDestination.route)
                }) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add Icon",
                        tint = AppColor.textColor
                    )
                }
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(text = currentWeather.name.orEmpty(), color = AppColor.textColor)
                    Row(
                        Modifier
                            .wrapContentHeight()
                            .padding(top = 4.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.Bottom
                    ) {
                        repeat(pagerState.pageCount) { iteration ->
                            val color =
                                if (pagerState.currentPage == iteration) Color.DarkGray else Color.LightGray
                            Box(
                                modifier = Modifier
                                    .padding(2.dp)
                                    .clip(CircleShape)
                                    .background(color)
                                    .size(8.dp)
                            )
                        }
                    }
                }
                IconButton(onClick = { }) {
                    Icon(
                        Icons.Default.MoreVert,
                        contentDescription = "More",
                        tint = AppColor.textColor
                    )
                }
            }
            WeatherIcon(currentWeather.weather?.first()?.icon)
            Text(text = getCurrentDate(), color = AppColor.textColor)
            Text(
                "${(currentWeather.main?.temp ?: 0.0).toInt()}°c",
                fontSize = GlobalDimension.contentTitleFontSize,
                color = AppColor.textColor,
                fontWeight = FontWeight.Bold
            )
            Text(currentWeather.weather?.first()?.description.orEmpty(), color = AppColor.textColor)
            Spacer(GlobalDimension.sectionPadding)
            HorizontalDivider(
                thickness = 2.dp,
                color = Color.White,
                modifier = Modifier.padding(horizontal = GlobalDimension.sectionPadding)
            )
            Spacer(GlobalDimension.sectionPadding)
            Row(Modifier.fillMaxWidth()) {
                Column(
                    Modifier.fillMaxWidth(0.5f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            painter = painterResource(R.drawable.ic_carbon_location_current),
                            contentDescription = "Wind Icon",
                            tint = AppColor.textColor, modifier = Modifier
                                .size(32.dp)
                                .graphicsLayer(
                                    scaleX = -1f  // Flip horizontally, for vertical flip use scaleY = -1f
                                )
                        )
                        Spacer(4.dp)
                        Column {
                            Text(
                                "${currentWeather.wind?.speed ?: 0.0} km/h",
                                color = AppColor.textColor
                            )
                            Text("Wind", color = AppColor.textColor)
                        }
                    }
                }
                Column(
                    Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            painter = painterResource(R.drawable.ic_fluent_weather_rain_24_regular),
                            contentDescription = "Humidity Icon",
                            tint = AppColor.textColor, modifier = Modifier
                                .size(32.dp)
                        )
                        Spacer(4.dp)
                        Column {
                            Text(
                                "${currentWeather.rain?.h?.times(100)?.toInt() ?: "-"}%",
                                color = AppColor.textColor
                            )
                            Text("Chance of rain", color = AppColor.textColor)
                        }
                    }
                }
            }
            Spacer(GlobalDimension.sectionPadding)
            Row(
                Modifier
                    .fillMaxWidth()
            ) {
                Column(
                    Modifier.fillMaxWidth(0.5f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            painter = painterResource(R.drawable.ic_fluent_temperature_24_regular),
                            contentDescription = "Pressure Icon",
                            tint = AppColor.textColor, modifier = Modifier
                                .size(32.dp)
                        )
                        Spacer(4.dp)
                        Column {
                            Text(
                                "${currentWeather.main?.pressure ?: 0.0} mbar",
                                color = AppColor.textColor
                            )
                            Text("Pressure", color = AppColor.textColor)
                        }
                    }
                }
                Column(
                    Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            painter = painterResource(R.drawable.ic_ion_water_outline),
                            contentDescription = "Humidity Icon",
                            tint = AppColor.textColor, modifier = Modifier
                                .size(32.dp)
                        )
                        Spacer(4.dp)
                        Column {
                            Text(
                                "${currentWeather.main?.humidity ?: 0.0} %",
                                color = AppColor.textColor
                            )
                            Text("Humidity", color = AppColor.textColor)
                        }
                    }
                }
            }
            Spacer(GlobalDimension.sectionPadding)
        }
    }
}

fun getCurrentDate(): String {
    val currentDate = LocalDate.now()
    val formatter = DateTimeFormatter.ofPattern("EEEE | MMM dd") // "EEEE" for full day name
    return currentDate.format(formatter)
}
