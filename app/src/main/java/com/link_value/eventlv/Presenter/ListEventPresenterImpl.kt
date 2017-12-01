package com.link_value.eventlv.Presenter

import com.link_value.eventlv.Model.EventLV
import com.link_value.eventlv.Repository.List.ListEventRepository
import com.link_value.eventlv.View.ListEvent.EventListView

/**
 * Created by julienb on 29/11/17.
 */
class ListEventPresenterImpl(private val mEventListView: EventListView, private val mListRepo: ListEventRepository):ListEventPresenter {

    override fun onSucessFetchEvents(events: List<EventLV>) {
        mEventListView.onEventsFetched(events)
    }

    override fun onErrorFetchEvents(error: String) {
        mEventListView.onErrorEventsFetch(error)
    }

    override fun fetchComingEvents(): List<EventLV> {
        mListRepo.queryComingEvents(this)
        return ArrayList<EventLV>()
    }

}