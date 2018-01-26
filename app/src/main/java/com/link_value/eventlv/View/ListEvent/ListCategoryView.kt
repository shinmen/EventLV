package com.link_value.eventlv.View.ListEvent

import com.link_value.eventlv.Model.Category

/**
 * Created by julienb on 26/01/18.
 */
interface ListCategoryView {
    fun onCategoriesFetched(categories :List<Category>)
    fun onErrorCategoryFetch(error :String?)
}