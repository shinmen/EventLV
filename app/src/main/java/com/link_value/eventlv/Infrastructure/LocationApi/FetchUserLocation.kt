package com.link_value.eventlv.Infrastructure.LocationApi

import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import kotlin.coroutines.experimental.suspendCoroutine


/**
 * Created by julienb on 28/12/17.
 */
class FetchUserLocation(private val locationManager: LocationManager) {

    private var currentLocation :Location? = null

    suspend fun fetchLocation(): Location? {
        val locationProvider = LocationManager.GPS_PROVIDER

        try {
            currentLocation = locationManager.getLastKnownLocation(locationProvider)
            if (currentLocation == null) {
                currentLocation = locationManager.await()
            }

            return currentLocation
        } catch (ex: SecurityException) {
            throw UnknownLocationException()
        }
    }

    private suspend fun LocationManager.await(): Location = suspendCoroutine { cont ->
        try {
            requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0.toFloat(), object :LocationListener{
                override fun onStatusChanged(p0: String?, p1: Int, p2: Bundle?) {
                }

                override fun onProviderEnabled(p0: String?) {
                }

                override fun onProviderDisabled(p0: String?) {
                    cont.resumeWithException(UnknownLocationException())
                }

                override fun onLocationChanged(location: Location?) {
                    if (location != null) {
                        cont.resume(location)
                    } else {
                        cont.resumeWithException(UnknownLocationException())
                    }
                }
            })
        } catch (ex: SecurityException) {
            cont.resumeWithException(UnknownLocationException())
        }
    }
}