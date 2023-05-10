package com.plcoding.weatherapp.domain.weather

data class WeatherInfo(
    ///Contains weather data per day
    val weatherDataPerDay: Map<Int, List<WeatherData>>, //<CurrentDayIndex, DataListOfDay> 0 = today
    //API returns the 7 day's dat
    val currentWeatherData: WeatherData?    //Represents weather data for today(current hour)

)
