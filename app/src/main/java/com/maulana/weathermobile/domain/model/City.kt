package com.maulana.weathermobile.domain.model


import com.maulana.weathermobile.data.datasource.remote.response.CoordResponse
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

data class City(
    val coord: CoordResponse?,
    val country: String?,
    val id: Int?,
    val name: String?,
    val population: Int?,
    val sunrise: Int?,
    val sunset: Int?,
    val timezone: Int?
)