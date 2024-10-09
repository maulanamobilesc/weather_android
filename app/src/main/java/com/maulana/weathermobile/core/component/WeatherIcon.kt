package com.maulana.weathermobile.core.component

import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

enum class WeatherIconSize {
    SMALL, BIG
}

@Composable
fun WeatherIcon(weatherCode: String?, size: WeatherIconSize = WeatherIconSize.BIG) {
    AsyncImage(
        model = getWeatherIconUrl(weatherCode.orEmpty()),
        contentDescription = "main weather icon",
        contentScale = ContentScale.FillWidth,
        modifier = Modifier
            .width(if (size == WeatherIconSize.BIG) 200.dp else 50.dp)
    )
}

fun getWeatherIconUrl(iconCode: String): String {
    return "https://openweathermap.org/img/wn/$iconCode@2x.png"
}
