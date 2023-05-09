package com.plcoding.weatherapp.data.remote

import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {
    //Function to call the API
    @GET("v1/forecast?hourly=temperature_2m,relativehumidity_2m,windspeed_10m,pressure_msl")    //Get request specifying the endpoint
    suspend fun getWeatherData(
        //Parameters to send location to API
        @Query("latitude") lat: Double,
        @Query("longitude") long: Double
    ): WeatherDto   //Returning Weather Dto: Contains weatherdatadto
}