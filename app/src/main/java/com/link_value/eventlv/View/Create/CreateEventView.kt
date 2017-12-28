package com.link_value.eventlv.View.Create

/**
 * Created by julo on 30/12/17.
 */
interface CreateEventView {
    fun onEventPersisted()

    fun onError(message: String?)
}