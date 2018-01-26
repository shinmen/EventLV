package com.link_value.eventlv.Repository.List

import com.link_value.eventlv.View.ListEvent.ListEventView

/**
 * Created by julienb on 30/11/17.
 */
interface ListEventRepository {
    fun queryComingEvents(listener: ListEventView)
    fun queryEventsByCategory(category:String, listener: ListEventView)
}