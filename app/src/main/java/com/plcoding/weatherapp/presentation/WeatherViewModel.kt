package com.plcoding.weatherapp.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.plcoding.weatherapp.domain.location.LocationTracker
import com.plcoding.weatherapp.domain.repository.WeatherRespository
import com.plcoding.weatherapp.domain.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel  //to inject hilt dependencies in this viewModel
class WeatherViewModel @Inject constructor(
    private val repository: WeatherRespository,
    private val locationTracker: LocationTracker
): ViewModel(){
    //create instance of state
    var state by mutableStateOf(WeatherState())
        private set //only viewModel can change the state
    fun loadWeatherInfo(){  //calls API; gets location and combines to the state
        viewModelScope.launch {
            state = state.copy(
                isLoading = true,
                error = null
            )
            locationTracker.getCurrentLocation()?.let { location ->
                when(val result = repository.getWeatherData(location.latitude, location.longitude)){
                    is Resource.Success -> {
                        state = state.copy(
                            weatherInfo = result.data,
                            isLoading = false,
                            error = null
                        )
                    }
                    is Resource.Error -> {
                        state = state.copy(
                            weatherInfo = null,
                            isLoading = false,
                            error = result.message    //Error => that we get from repository
                        )
                    }
                }
            } ?: kotlin.run {   //When we don't get a location(location = null)
                state = state.copy(
                    isLoading = false,
                    error = "Couldn't retrieve location. Make sure to grant permission & enable GPS."
                )
            }
        }
    }
}