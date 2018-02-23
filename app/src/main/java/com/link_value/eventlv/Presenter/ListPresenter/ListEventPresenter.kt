package com.link_value.eventlv.Presenter.ListPresenter

import com.link_value.eventlv.Model.Category
import com.link_value.eventlv.Model.EventLV

/**
 * Created by julienb on 29/11/17.
 */
interface ListEventPresenter {
    fun start()
    fun waitForList()
    fun refreshWithCategory(category: String)
    fun fetchAll()
    fun updateParticipantEvent(event: EventLV)
    fun addEvent(event: EventLV)
    fun moveToCategory(category: Category)
}