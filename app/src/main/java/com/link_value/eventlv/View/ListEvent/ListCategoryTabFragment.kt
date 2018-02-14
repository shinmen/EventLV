package com.link_value.eventlv.View.ListEvent

import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.link_value.eventlv.Model.Category
import com.link_value.eventlv.Model.Event.TabSelectedEvent
import com.link_value.eventlv.Presenter.ListPresenter.ListEventPresenterImpl
import com.link_value.eventlv.R
import kotlinx.android.synthetic.main.fragment_liststatustab.*
import org.greenrobot.eventbus.EventBus

/**
 * Created by julienb on 19/01/18.
 */
class ListCategoryTabFragment : Fragment(),
        ListCategoryView
{
    lateinit var mPresenter: ListEventPresenterImpl
    var mCategories: Map<String, Category> = HashMap()

    override fun onCategoriesFetched(categories: Map<String, Category>) {
        mCategories = categories
        categories.forEach {
           val tab = tab_list.newTab().setText(it.key)
           tab_list.addTab(tab)
       }
    }

    override fun onErrorCategoryFetch(error: String?) {
        Toast.makeText(activity, getString(R.string.error_list_category), Toast.LENGTH_LONG).show()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_liststatustab, container, false)
        return view
    }

    override fun onStart() {
        tab_list.addOnTabSelectedListener(object: TabLayout.OnTabSelectedListener{
            override fun onTabReselected(tab: TabLayout.Tab?) {
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabSelected(tab: TabLayout.Tab?) {
                if (mCategories.containsKey(tab?.text.toString())) {
                    val category = mCategories[tab?.text.toString()]
                    EventBus.getDefault().post(TabSelectedEvent(category!!))
                } else {
                    val category = Category(resources.getString(R.string.all_types), "all")
                    EventBus.getDefault().post(TabSelectedEvent(category))
                }
            }
        })
        super.onStart()
    }

    companion object {
        fun newInstance(): ListCategoryTabFragment {
            val fragment = ListCategoryTabFragment()
            val args = Bundle()
            fragment.arguments = args

            return fragment
        }
    }
}