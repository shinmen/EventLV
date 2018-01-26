package com.link_value.eventlv.Repository.List

import com.link_value.eventlv.Presenter.ListPresenter.ListEventPresenter
import com.link_value.eventlv.View.ListEvent.ListCategoryView

/**
 * Created by julienb on 26/01/18.
 */
interface ListCategoryRepository {
    fun queryCategories(listener: ListCategoryView)
}