package com.plcoding.weatherapp.data.remote.location

import android.app.Application
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.plcoding.weatherapp.domain.location.LocationTracker
import kotlinx.coroutines.suspendCancellableCoroutine
import java.util.jar.Manifest
import javax.inject.Inject
import kotlin.coroutines.resume

//Location tracker class implementing LocationTracker interface
class DefaultLocationTracker @Inject constructor(
    //dependencies needed: LocationClient(comes from android framework)
    //fusedLocationProviderClient
    private val locationClient: FusedLocationProviderClient,//comes from dependency we added
    private val application: Application    //to be able to check permissions
): LocationTracker {
    override suspend fun getCurrentLocation(): Location? {
        val hasAccessFineLocationPermission = ContextCompat.checkSelfPermission(
            application,
            android.Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
        val hasAccessCoarseLocationPermission = ContextCompat.checkSelfPermission(
            application,
            android.Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
        //get reference to the locationManager(comes from android framework i.e. it's a system service)
        val locationManager = application.getSystemService(Context.LOCATION_SERVICE) as LocationManager //as = casting
        val isGPSEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)  || //provider => retrieve location using network
            locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)

        if(!hasAccessCoarseLocationPermission || !hasAccessFineLocationPermission || !isGPSEnabled){
            return null //resouce object could be returned for specific msg
        }
        return suspendCancellableCoroutine { cont ->
            locationClient.lastLocation.apply {//gives Task
                if(isComplete){
                    if(isSuccessful)
                        cont.resume(result)
                    else
                        cont.resume(null)
                    return@suspendCancellableCoroutine
                }
                /*Callbacks to the coroutine*/
                //Wait Until it's complete
                addOnSuccessListener {//this give the location
                    cont.resume(it)
                }
                //if it's an error
                addOnFailureListener {
                    cont.resume(null)
                }
                //if it was cancelled
                addOnCanceledListener {
                    cont.cancel()
                }
            }
        }
    }
}