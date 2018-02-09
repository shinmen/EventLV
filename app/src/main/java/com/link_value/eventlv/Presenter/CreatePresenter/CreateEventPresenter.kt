package com.link_value.eventlv.Presenter.CreatePresenter

import android.location.Location
import com.link_value.eventlv.Model.EventLV

/**
 * Created by julo on 30/12/17.
 */
interface CreateEventPresenter {
    fun start()
    fun persistEventLv(event: EventLV)
    fun onSuccessEventSaved()
    fun onErrorEventSaved(error: String?)
}