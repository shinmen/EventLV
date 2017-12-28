package com.link_value.eventlv.Infrastructure.LocationApi

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
import kotlin.coroutines.experimental.suspendCoroutine


/**
 * Created by julo on 30/12/17.
 */
class AutocompleteAddress(private val userLocation: FetchUserLocation, private val googleApiClient: GoogleApiClient) {

    suspend fun getPredictions(query: String): ArrayList<String> {
        var latLng: LatLng
        latLng = try {
            val location = userLocation.fetchLocation()
            LatLng(location!!.latitude, location!!.longitude)
        } catch (e: UnknownLocationException) {
            LatLng(48.883003, 2.316180)
        }

        val southwest = SphericalUtil.computeOffset(latLng, 1500 * Math.sqrt(2.0), 225.toDouble())
        val northeast = SphericalUtil.computeOffset(latLng, 1500 * Math.sqrt(2.0), 45.toDouble())
        val bounds = LatLngBounds(southwest, northeast)
        val addressList = ArrayList<String>()
        launch(CommonPool) {
            val result = Places.GeoDataApi.getAutocompletePredictions(googleApiClient, query, bounds, null)

            val predictions = result.await()

            predictions.forEach {
                val address = AddressEventLV(it.getPrimaryText(null).toString(), it.getSecondaryText(null).toString())
                addressList.add(address.toString())
            }
            predictions.release()
        }

        return addressList
    }

    suspend fun PendingResult<AutocompletePredictionBuffer>.await(): AutocompletePredictionBuffer = suspendCoroutine {
        cont-> setResultCallback { predictions -> cont.resume(predictions) }
    }
}