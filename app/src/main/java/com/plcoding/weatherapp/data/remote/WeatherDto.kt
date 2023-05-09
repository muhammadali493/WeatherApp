package com.plcoding.weatherapp.data.remote

import com.squareup.moshi.Json

data class WeatherDto(
    //Info we need in app: hourly field
    @field: Json(name = "hourly")   //Specifying the specifice field name
    val weatherData: WeatherDataDto //weatherData => must be same name as JSON field from API(hourly here)
)
