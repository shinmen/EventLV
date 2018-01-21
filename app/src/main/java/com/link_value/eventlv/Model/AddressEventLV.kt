package com.link_value.eventlv.Model

/**
 * Created by julo on 30/12/17.
 */
data class AddressEventLV(private val name: String, private val address: String) {
    override fun toString(): String {
        return "$name - $address"
    }
}