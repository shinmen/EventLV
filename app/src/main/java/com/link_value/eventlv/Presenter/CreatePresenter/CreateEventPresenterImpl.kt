package com.link_value.eventlv.Presenter.CreatePresenter

import com.link_value.eventlv.Model.EventLV
import com.link_value.eventlv.Repository.Create.NewEventRepository
import com.link_value.eventlv.View.Create.CreateEventView

/**
 * Created by julo on 30/12/17.
 */
class CreateEventPresenterImpl(
        private val view: CreateEventView,
        private val mNewRepo: NewEventRepository
    ): CreateEventPresenter {

    override fun onSuccessEventSaved() {
        view.onEventPersisted()
    }

    override fun onErrorEventSaved(error: String?) {
        view.onError(error)
    }

    override fun persistEventLv(event: EventLV) {
        mNewRepo.saveEvent(event, this)
    }
}

