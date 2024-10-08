package com.maulana.weathermobile.domain.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

data class Forecast(
    val clouds: Clouds?,
    val dt: Int?,
    val dtTxt: String?,
    val main: Main?,
    val pop: Double?,
    val rain: Rain?,
    val sys: SysPod?,
    val visibility: Int?,
    val weather: List<Weather?>?,
    val wind: Wind?
)