package com.link_value.eventlv.Infrastructure.LocationApi

import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.os.Process
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.maps.model.LatLng
import com.link_value.eventlv.Infrastructure.GoogleServiceConcurrentHandler
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.channels.Channel
import kotlinx.coroutines.experimental.channels.SendChannel
import kotlinx.coroutines.experimental.launch
import org.jetbrains.anko.coroutines.experimental.asReference
import kotlin.coroutines.experimental.Continuation
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
            //var location: Location?
            //ch.send(locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER))
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0.toFloat(), object :LocationListener{
                override fun onLocationChanged(location: Location?) {
                    //launch(UI) {  ch.send(location)}
                }

                override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
                }

                override fun onProviderEnabled(provider: String?) {
                }

                override fun onProviderDisabled(provider: String?) {
                }

            })
            val location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)

            /*val locationUpdated = ch.receive()
            if (locationUpdated != null) {
                location = locationUpdated
            }*/

            return location
        } catch (ex: SecurityException) {
            throw ex
        } catch (ex: Throwable) {
            throw ex
        } catch (ex: UnknownLocationException) {
            throw ex
        }
    }

    private suspend fun LocationManager.await(locationProvider: String): Location? = suspendCoroutine { cont ->
        try {
            val liste = liste(cont)
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
        } catch (ex: Throwable) {
            cont.resumeWithException(ex)
        }
    }

    class liste(private val cont:Continuation<Location?>): LocationListener {
        override fun onLocationChanged(location: Location?) {
            cont.resume(location)
        }

        override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
        }

        override fun onProviderEnabled(provider: String?) {
        }

        override fun onProviderDisabled(provider: String?) {
        }

    }
}