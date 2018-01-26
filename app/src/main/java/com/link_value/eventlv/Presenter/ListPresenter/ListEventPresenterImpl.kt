package com.link_value.eventlv.Presenter.ListPresenter

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

    override fun start() {
        mListRepo.queryComingEvents(mListEventView)
        mCategoriesRepo.queryCategories(mListCategoryView)
    }

    override fun refreshWithCategory(category: String) {
        mListRepo.queryEventsByCategory(category, mListEventView)
    }

}