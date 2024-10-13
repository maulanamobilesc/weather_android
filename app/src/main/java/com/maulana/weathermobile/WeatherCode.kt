package com.maulana.weathermobile

enum class WeatherCode(code:String) {
    Unknown(""),
    ClearSkyDay("01d"),
    ClearSkyNight("01n"),
    FewCloudsDay("02d"),
    FewCloudsNight("02n"),
    ScatteredCloudsDay("03d"),
    ScatteredCloudsNight("03n"),
    BrokenCloudsDay("04d"),
    BrokenCloudsNight("04n"),
    ShowerRainDay("09d"),
    ShowerRainNight("09n"),
    RainDay("10d"),
    RainNight("10n"),
    ThunderstormDay("11d"),
    ThunderstormNight("11n"),
    SnowDay("13d"),
    SnowNight("13n"),
    MistDay("50d"),
    MistNight("50n")
}