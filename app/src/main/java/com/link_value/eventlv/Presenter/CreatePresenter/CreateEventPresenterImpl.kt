package com.link_value.eventlv.Presenter.CreatePresenter

import com.link_value.eventlv.Model.EventLV
import com.link_value.eventlv.View.Create.CreateEventView
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch

/**
 * Created by julo on 30/12/17.
 */
class CreateEventPresenterImpl(private val view: CreateEventView): CreateEventPresenter {

    override fun persistEventLv(event: EventLV) {

    }
}

