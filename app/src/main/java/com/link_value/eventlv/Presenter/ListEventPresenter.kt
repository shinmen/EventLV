package com.link_value.eventlv.Presenter

import com.link_value.eventlv.Model.EventLV

/**
 * Created by julienb on 29/11/17.
 */
interface ListEventPresenter {
    fun fetchComingEvents():List<EventLV>
    fun onSuccessFetchEvents(events: List<EventLV>)
    fun onErrorFetchEvents(error: String)

}