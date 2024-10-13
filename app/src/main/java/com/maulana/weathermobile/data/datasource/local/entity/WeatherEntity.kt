package com.maulana.weathermobile.data.datasource.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "weather")
data class WeatherEntity(
    @ColumnInfo(name = "location_id")
    val locationId: Int,
    @PrimaryKey
    @ColumnInfo(name = "location_name")
    val locationName: String,
    @ColumnInfo(name = "current_weather")
    val currentWeather: String,
    @ColumnInfo(name = "weather_code")
    val weatherCode: String,
    val temperature: Double,
    @ColumnInfo(name = "max_temperature")
    val maxTemperature: Double,
    @ColumnInfo(name = "min_temperature")
    val minTemperature: Double,
    val date: String,
    @ColumnInfo(name = "is_default")
    val isDefault: Boolean,
    val latitude: Double,
    val longitude: Double,
)