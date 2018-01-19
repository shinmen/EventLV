package com.link_value.eventlv.Repository.Create

import com.link_value.eventlv.Model.EventLV
import com.link_value.eventlv.Presenter.CreatePresenter.CreateEventPresenter

/**
 * Created by julienb on 19/01/18.
 */
interface NewEventRepository {
    fun saveEvent(eventLv: EventLV, listener: CreateEventPresenter)
}