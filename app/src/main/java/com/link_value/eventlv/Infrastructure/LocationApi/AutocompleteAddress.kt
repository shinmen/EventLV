package com.link_value.eventlv.Infrastructure.LocationApi

import android.location.Location
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.PendingResult
import com.google.android.gms.location.places.AutocompletePredictionBuffer
import com.google.android.gms.location.places.Places
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.SphericalUtil
import com.link_value.eventlv.Model.AddressEventLV
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.newSingleThreadContext
import kotlin.coroutines.experimental.CoroutineContext
import kotlin.coroutines.experimental.suspendCoroutine


/**
 * Created by julo on 30/12/17.
 */
class AutocompleteAddress(private val userLocation: FetchUserLocation, private val googleApiClient: GoogleApiClient) {

    suspend fun getPredictions(query: String, location: Location?): ArrayList<AddressEventLV> {
        val addressList = ArrayList<AddressEventLV>()
        val latLng = LatLng(location!!.latitude, location!!.longitude)
        val southwest = SphericalUtil.computeOffset(latLng, 1500 * Math.sqrt(2.0), 225.toDouble())
        val northeast = SphericalUtil.computeOffset(latLng, 1500 * Math.sqrt(2.0), 45.toDouble())
        val bounds = LatLngBounds(southwest, northeast)
        val job = launch {
            val result = Places.GeoDataApi.getAutocompletePredictions(googleApiClient, query, bounds, null)
            val predictions = result.await()
            predictions.forEach {
                addressList.add(AddressEventLV(it.getPrimaryText(null).toString(), it.getSecondaryText(null).toString()))
            }
            predictions.release()
        }
        job.join()

        return addressList
    }

    suspend fun PendingResult<AutocompletePredictionBuffer>.await(): AutocompletePredictionBuffer = suspendCoroutine {
        cont-> setResultCallback { predictions -> cont.resume(predictions) }
    }
}