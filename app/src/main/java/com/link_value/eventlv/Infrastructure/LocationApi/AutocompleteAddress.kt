package com.link_value.eventlv.Infrastructure.LocationApi

import android.location.Location
import android.text.Editable
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.PendingResult
import com.google.android.gms.location.places.AutocompleteFilter
import com.google.android.gms.location.places.AutocompletePredictionBuffer
import com.google.android.gms.location.places.PlaceBuffer
import com.google.android.gms.location.places.Places
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.SphericalUtil
import com.link_value.eventlv.Model.AddressEventLV
import kotlinx.android.synthetic.main.activity_new_event_lv.*
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.newSingleThreadContext
import kotlin.coroutines.experimental.CoroutineContext
import kotlin.coroutines.experimental.suspendCoroutine


/**
 * Created by julo on 30/12/17.
 */
class AutocompleteAddress(private val googleApiClient: GoogleApiClient) {

    suspend fun getPredictions(query: String, location: Location?): ArrayList<AddressEventLV> {
        val latLng: LatLng = if (location == null) {
            FetchUserLocation.LV_LATLNG
        } else {
            LatLng(location.latitude, location.longitude)
        }
        val addressList = ArrayList<AddressEventLV>()
        val southwest = SphericalUtil.computeOffset(latLng, 1500 * Math.sqrt(2.0), 225.toDouble())
        val northeast = SphericalUtil.computeOffset(latLng, 1500 * Math.sqrt(2.0), 45.toDouble())
        val bounds = LatLngBounds(southwest, northeast)
        val job = launch {
            val predictionBuffer = Places.GeoDataApi.getAutocompletePredictions(googleApiClient, query, bounds, null)
            val predictions = predictionBuffer.awaitPredictions()
            predictions.forEach {
                val address = AddressEventLV(
                        it.getPrimaryText(null).toString(),
                        it.getSecondaryText(null).toString(),
                        it.placeId.toString()
                )
                addressList.add(address)
            }
            predictions.release()
        }
        job.join()

        return addressList
    }

    suspend fun getPreciseAddress(address: AddressEventLV): String {
        val placeBuffer = Places.GeoDataApi.getPlaceById(googleApiClient, address.placeId)
        val places = placeBuffer.awaitPlace()
        val preciseAddress = places.get(0).address
        places.release()

        return preciseAddress.toString()
    }

    private suspend fun PendingResult<AutocompletePredictionBuffer>.awaitPredictions(): AutocompletePredictionBuffer = suspendCoroutine {
        cont-> setResultCallback { predictions -> cont.resume(predictions) }
    }

    private suspend fun PendingResult<PlaceBuffer>.awaitPlace(): PlaceBuffer = suspendCoroutine {
        cont-> setResultCallback { places -> cont.resume(places) }
    }
}