package com.link_value.eventlv.Infrastructure.LocationApi

import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.channels.Channel
import kotlinx.coroutines.experimental.launch
import kotlin.coroutines.experimental.suspendCoroutine


/**
 * Created by julienb on 28/12/17.
 */
class FetchUserLocation(private val locationManager: LocationManager) {

    private val ch = Channel<Location?>()
    companion object {
        val LV_LATLNG = LatLng(48.883003, 2.316180)
    }

    suspend fun fetchLocation(): Location? {
        try {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0.toFloat(), object :LocationListener{
                override fun onLocationChanged(location: Location?) {}
                override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}
                override fun onProviderEnabled(provider: String?) {}
                override fun onProviderDisabled(provider: String?) {}
            })

            return locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
        } catch (ex: SecurityException) {
            throw ex
        }
    }

    private suspend fun LocationManager.await(locationProvider: String): Location? = suspendCoroutine { cont ->
        try {
            requestLocationUpdates(locationProvider, 0, 0.toFloat(), object:LocationListener{
                override fun onLocationChanged(location: Location?) {
                    cont.resume(location)
                }

                override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
                }

                override fun onProviderEnabled(provider: String?) {
                }

                override fun onProviderDisabled(provider: String?) {
                }
            })

        } catch (ex: SecurityException) {
            cont.resumeWithException(ex)
        }
    }
}