package com.link_value.eventlv.Repository.List

import com.link_value.eventlv.Presenter.ListEventPresenter

/**
 * Created by julienb on 30/11/17.
 */
interface ListEventRepository {
    fun queryComingEvents(listener: ListEventPresenter)
}