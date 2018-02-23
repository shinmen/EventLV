package com.link_value.eventlv.View.ListEvent

import android.content.Context
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.link_value.eventlv.Model.Category
import com.link_value.eventlv.Model.Event.TabSelectedEvent
import com.link_value.eventlv.Model.EventLV
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
    private var mCategories: Map<String, Category> = HashMap()
    private var mCategoryListener: CategoryListener? = null

    override fun onCategoriesFetched(categories: Map<String, Category>) {
        mCategories = categories
        categories.forEach {
           val tab = tab_list.newTab().setText(it.value.name)
           tab_list.addTab(tab)
       }
    }

    override fun onErrorCategoryFetch(error: String?) {
        Toast.makeText(activity, getString(R.string.error_list_category), Toast.LENGTH_LONG).show()
    }

    override fun onCategorySelected(category: Category) {
        if (mCategories.isNotEmpty() && mCategories.containsKey(category.slug)) {
            val index = mCategories.keys.toList().indexOf(category.slug)
            val tab = tab_list.getTabAt(index+1)
            tab?.select()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_liststatustab, container, false)
        return view
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        mCategoryListener = context as CategoryListener
    }

    override fun onDetach() {
        super.onDetach()
        mCategoryListener = null
    }
    override fun onStart() {
        tab_list.addOnTabSelectedListener(object: TabLayout.OnTabSelectedListener{
            override fun onTabReselected(tab: TabLayout.Tab?) {
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabSelected(tab: TabLayout.Tab?) {
                val categoriesSelected = mCategories
                        .filter { it.value.name ==  tab?.text.toString()}
                        .map {
                            it.value
                        }
                if (categoriesSelected.isNotEmpty()) {
                    mCategoryListener?.onTabSelected(categoriesSelected.first())
                } else {
                    val category = Category(resources.getString(R.string.all_types), "all")
                    mCategoryListener?.onTabSelected(category)
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

    interface CategoryListener {
        fun onTabSelected(category: Category)
    }
}