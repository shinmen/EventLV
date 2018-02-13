package com.link_value.eventlv.Infrastructure.LocationApi

import com.link_value.eventlv.Model.AddressEventLV

/**
 * Created by julienb on 13/02/18.
 */
class AddressPredictionFetcher(private val autoCompleteAddress: AutocompleteAddress, private val userLocation: FetchUserLocation) {
    suspend fun fetchAddressPrediction(partialQuery: String): List<AddressEventLV> {
        val location = userLocation.fetchLocation()
        val addressList = autoCompleteAddress.getPredictions(partialQuery, location)

        return addressList
    }
}