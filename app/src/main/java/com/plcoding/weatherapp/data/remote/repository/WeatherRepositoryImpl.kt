package com.plcoding.weatherapp.data.remote.repository

import android.content.res.Resources
import com.plcoding.weatherapp.data.remote.WeatherApi
import com.plcoding.weatherapp.data.remote.mappers.toWeatherInfo
import com.plcoding.weatherapp.domain.repository.WeatherRespository
import com.plcoding.weatherapp.domain.util.Resource
import com.plcoding.weatherapp.domain.weather.WeatherInfo
import javax.inject.Inject

//WeatherRepositoryImplementation implementing interface WeatherRepository
class WeatherRepositoryImpl @Inject constructor(
    //To get access to the API
    private val api: WeatherApi
): WeatherRespository {
    override suspend fun getWeatherData(lat: Double, long: Double): Resource<WeatherInfo> {
        return try {
            Resource.Success(
                data = api.getWeatherData(
                    lat = lat,
                    long = long
                ).toWeatherInfo()
            )
        }catch (e: Exception){
            e.printStackTrace()
            Resource.Error(e.message ?: "An Unknown Error Occurred.")
        }
    }
}