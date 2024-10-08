package com.maulana.weathermobile.data.mapper

import com.maulana.weathermobile.data.datasource.remote.response.forecastweather.ForecastResponse
import com.maulana.weathermobile.data.datasource.remote.response.forecastweather.RainResponse
import com.maulana.weathermobile.data.datasource.remote.response.forecastweather.SysPodResponse
import com.maulana.weathermobile.domain.model.Forecast
import com.maulana.weathermobile.domain.model.Rain
import com.maulana.weathermobile.domain.model.SysPod

fun ForecastResponse.toForecast(): Forecast = Forecast(
    clouds?.toClouds(),
    dt,
    dtTxt,
    main?.toMain(),
    pop,
    rain?.toRain(),
    sys?.toSysPod(),
    visibility,
    weather?.map { it?.toWeather() },
    wind?.toWind()
)

fun RainResponse.toRain(): Rain = Rain(h)

fun SysPodResponse.toSysPod(): SysPod = SysPod(pod)