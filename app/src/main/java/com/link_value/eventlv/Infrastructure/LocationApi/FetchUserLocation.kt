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
    companion object {
        val LV_LATLNG = LatLng(48.883003, 2.316180)
    }

    fun fetchLocation(): Location? {
        try {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0.toFloat(), object :LocationListener{
                override fun onLocationChanged(location: Location?) {locationManager.removeUpdates(this)}
                override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}
                override fun onProviderEnabled(provider: String?) {}
                override fun onProviderDisabled(provider: String?) {}
            })

            return locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
        } catch (ex: SecurityException) {
            throw ex
        }
    }
}