package com.plcoding.weatherapp.domain.location

import android.location.Location

//Interface to provide abstraction to get location from GPS
interface LocationTracker {
    suspend fun getCurrentLocation(): Location? //nullable b/c permission may not be given, GPS not enabled
}