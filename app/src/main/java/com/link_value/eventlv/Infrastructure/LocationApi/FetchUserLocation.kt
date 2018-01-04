package com.link_value.eventlv.Infrastructure.LocationApi

import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import com.google.android.gms.maps.model.LatLng
import kotlin.coroutines.experimental.suspendCoroutine


/**
 * Created by julienb on 28/12/17.
 */
class FetchUserLocation(private val locationManager: LocationManager) {

    companion object {
        val LV_LATLNG = LatLng(48.883003, 2.316180)
    }

    suspend fun fetchLocation(): Location? {
        try {
            return locationManager.await(LocationManager.GPS_PROVIDER)
        } catch (ex: SecurityException) {
            throw UnknownLocationException()
        }
    }

    private suspend fun LocationManager.await(locationProvider: String): Location = suspendCoroutine { cont ->
        try {
            requestLocationUpdates(locationProvider, 0, 0.toFloat(), object :LocationListener{
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