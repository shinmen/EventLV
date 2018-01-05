package com.link_value.eventlv.Infrastructure.LocationApi

import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.maps.model.LatLng
import kotlin.coroutines.experimental.suspendCoroutine


/**
 * Created by julienb on 28/12/17.
 */
class FetchUserLocation(val locationManager: LocationManager) {

    companion object {
        val LV_LATLNG = LatLng(48.883003, 2.316180)
    }

    suspend fun fetchLocation(): Location? {

        try {
            var location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            val locationUpdated = locationManager.await(LocationManager.GPS_PROVIDER)
            if (locationUpdated != null) {
                location = locationUpdated
            }

            return location
        } catch (ex: SecurityException) {
            throw UnknownLocationException()
        }
    }

    private suspend fun LocationManager.await(locationProvider: String): Location? = suspendCoroutine { cont ->
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
                    cont.resume(location)
                    locationManager.removeUpdates(this)
                }
            })
        } catch (ex: SecurityException) {
            cont.resumeWithException(ex)
        }
    }
}