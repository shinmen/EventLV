package com.link_value.eventlv.View.ListEvent

import com.link_value.eventlv.Model.EventLV

/**
 * Created by julienb on 29/11/17.
 */
interface EventListView {
    fun onEventsFetched(events :List<EventLV>)
    fun onErrorEventsFetch(error :String)
}