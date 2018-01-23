package com.link_value.eventlv.Model

/**
 * Created by julo on 30/12/17.
 */
data class AddressEventLV(
        val name: String,
        val address: String,
        val placeId: String
    ) {
    override fun toString(): String {
        return "$name - $address"
    }
}