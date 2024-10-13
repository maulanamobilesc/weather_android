package com.maulana.weathermobile.data.mapper

import com.maulana.weathermobile.data.datasource.local.entity.WeatherEntity
import com.maulana.weathermobile.data.datasource.remote.response.CloudsResponse
import com.maulana.weathermobile.data.datasource.remote.response.CoordResponse
import com.maulana.weathermobile.data.datasource.remote.response.MainResponse
import com.maulana.weathermobile.data.datasource.remote.response.WeatherResponse
import com.maulana.weathermobile.data.datasource.remote.response.WindResponse
import com.maulana.weathermobile.data.datasource.remote.response.currentweather.CurrentWeatherResponse
import com.maulana.weathermobile.data.datasource.remote.response.currentweather.Rain1HResponse
import com.maulana.weathermobile.data.datasource.remote.response.currentweather.SysResponse
import com.maulana.weathermobile.domain.model.Clouds
import com.maulana.weathermobile.domain.model.Coord
import com.maulana.weathermobile.domain.model.CurrentWeather
import com.maulana.weathermobile.domain.model.LocationLocal
import com.maulana.weathermobile.domain.model.Main
import com.maulana.weathermobile.domain.model.Rain
import com.maulana.weathermobile.domain.model.Sys
import com.maulana.weathermobile.domain.model.Weather
import com.maulana.weathermobile.domain.model.WeatherLocal
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
    rain?.toRain(),
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

fun Rain1HResponse.toRain(): Rain = Rain(h)

fun WeatherEntity.toWeatherLocal() = WeatherLocal(
    locationId,
    locationName,
    currentWeather,
    weatherCode,
    temperature,
    maxTemperature,
    minTemperature,
    date,
    isDefault,
    latitude, longitude
)

fun WeatherLocal.toWeatherEntity() = WeatherEntity(
    locationId,
    locationName,
    currentWeather,
    weatherCode,
    temperature,
    maxTemperature,
    minTemperature,
    date,
    isDefault,
    latitude, longitude
)

fun CurrentWeather.toLocationLocal(id: Int) = LocationLocal(
    id = id,
    country = sys?.country.orEmpty(),
    name = name.orEmpty(),
    isDefault = false,
    longitude = coord?.lon ?: 0.0,
    latitude = coord?.lat ?: 0.0,
    state = ""
)

fun CurrentWeather.toWeatherLocal(locationId: Int) = WeatherLocal(
    locationId = locationId,
    locationName = name.orEmpty(),
    currentWeather = weather?.first()?.description.orEmpty(),
    weatherCode = weather?.first()?.icon.orEmpty(),
    date = dt.toString(),
    temperature = main?.temp ?: 0.0,
    maxTemperature = main?.tempMax ?: 0.0,
    minTemperature = main?.tempMin ?: 0.0,
    isDefault = true,
    latitude = coord?.lat ?: 0.0,
    longitude = coord?.lon ?: 0.0
)

fun WeatherLocal.toCoord() = Coord(lat = latitude, lon = longitude)