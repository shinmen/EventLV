package com.link_value.eventlv.View.Create

import android.content.Context
import android.widget.ArrayAdapter
import com.link_value.eventlv.Model.Category

/**
 * Created by julienb on 08/02/18.
 */
class CategoryAdapter(context: Context, resource: Int, private val categories: List<Category>): ArrayAdapter<Category>(context, resource) {
    override fun getCount(): Int {
        return categories.size
    }

    override fun getItem(position: Int): Category {
        return categories[position]
    }
}