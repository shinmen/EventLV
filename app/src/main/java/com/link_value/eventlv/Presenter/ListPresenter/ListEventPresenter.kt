package com.link_value.eventlv.Presenter.ListPresenter

/**
 * Created by julienb on 29/11/17.
 */
interface ListEventPresenter {
    fun start()
    fun refreshWithCategory(category: String)
    fun fetchAll()
}