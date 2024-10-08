package com.maulana.weathermobile.data.mapper

import com.maulana.weathermobile.data.datasource.remote.response.CloudsResponse
import com.maulana.weathermobile.data.datasource.remote.response.CoordResponse
import com.maulana.weathermobile.data.datasource.remote.response.currentweather.CurrentWeatherResponse
import com.maulana.weathermobile.data.datasource.remote.response.MainResponse
import com.maulana.weathermobile.data.datasource.remote.response.currentweather.SysResponse
import com.maulana.weathermobile.data.datasource.remote.response.WeatherResponse
import com.maulana.weathermobile.data.datasource.remote.response.WindResponse
import com.maulana.weathermobile.domain.model.Clouds
import com.maulana.weathermobile.domain.model.Coord
import com.maulana.weathermobile.domain.model.CurrentWeather
import com.maulana.weathermobile.domain.model.Main
import com.maulana.weathermobile.domain.model.Sys
import com.maulana.weathermobile.domain.model.Weather
import com.maulana.weathermobile.domain.model.Wind

fun CurrentWeatherResponse.toCurrentWeather(): CurrentWeather = CurrentWeather(
    base,
    clouds?.toClouds(),
    cod,
    coord?.toCoord(),
    dt,
    id,
    main?.toMain(),
    name,
    sys?.toSys(),
    timezone,
    visibility,
    weather?.map { it?.toWeather() },
    wind?.toWind()
)

fun CloudsResponse.toClouds(): Clouds = Clouds(all)

fun CoordResponse.toCoord(): Coord = Coord(lat, lon)

fun MainResponse.toMain(): Main =
    Main(feelsLike, grndLevel, humidity, pressure, seaLevel, temp, tempMax, tempMin)

fun SysResponse.toSys(): Sys = Sys(country, id, sunrise, sunset, type)

fun WeatherResponse.toWeather(): Weather = Weather(description, icon, id, main)

fun WindResponse.toWind(): Wind = Wind(deg, gust, speed)