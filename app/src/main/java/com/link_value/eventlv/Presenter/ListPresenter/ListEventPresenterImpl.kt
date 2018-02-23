package com.link_value.eventlv.Presenter.ListPresenter

import com.link_value.eventlv.Model.Category
import com.link_value.eventlv.Model.EventLV
import com.link_value.eventlv.Repository.List.ListCategoryRepository
import com.link_value.eventlv.Repository.List.ListEventRepository
import com.link_value.eventlv.View.ListEvent.ListCategoryView
import com.link_value.eventlv.View.ListEvent.ListEventView

/**
 * Created by julienb on 29/11/17.
 */
class ListEventPresenterImpl(
        private val mListEventView: ListEventView,
        private val mListCategoryView: ListCategoryView,
        private val mListRepo: ListEventRepository,
        private val mCategoriesRepo: ListCategoryRepository
    ): ListEventPresenter {

    override fun updateParticipantEvent(event: EventLV) {
        mListEventView.replaceEvent(event)
    }

    override fun addEvent(event: EventLV) {
        mListEventView.newEvent(event)
    }

    override fun waitForList() {
        mListEventView.displayLoading()
    }

    override fun start() {
        mListRepo.queryComingEvents(mListEventView)
        mCategoriesRepo.queryCategories(mListCategoryView)
    }

    override fun refreshWithCategory(category: String) {
        mListRepo.queryEventsByCategory(category, mListEventView)
    }

    override fun fetchAll() {
        mListRepo.queryComingEvents(mListEventView)
    }

    override fun moveToCategory(category: Category) {
        mListCategoryView.onCategorySelected(category)
    }

}