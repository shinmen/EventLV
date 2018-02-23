package com.link_value.eventlv.View.ListEvent

import android.os.Bundle
import android.support.transition.*
import android.support.v4.app.Fragment
import android.support.v4.widget.ContentLoadingProgressBar
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.link_value.eventlv.Model.Category
import com.link_value.eventlv.Model.EventLV
import com.link_value.eventlv.Presenter.ListPresenter.ListEventPresenterImpl
import com.link_value.eventlv.R
import android.content.Context


/**
 * Mandatory empty constructor for the fragment manager to instantiate the
 * fragment (e.g. upon screen orientation changes).
 */
class EventLvFragment : Fragment(),
        ListEventView
{
    private lateinit var mAdapter:EventLvListRecyclerViewAdapter
    lateinit var mPresenter: ListEventPresenterImpl
    private var tabSelected: String? = null
    private lateinit var mView: ViewGroup
    private lateinit var mListView: RecyclerView
    private lateinit var mLoadingView: ContentLoadingProgressBar
    private var mListener: ListListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
    }

    fun onTabSelected(category: Category) {
        if (category.name != tabSelected) {
            tabSelected = category.name
            if (tabSelected == resources.getString(R.string.all_types)) {
                mPresenter.waitForList()
                mPresenter.fetchAll()
            } else {
                mPresenter.waitForList()
                mPresenter.refreshWithCategory(category.slug)
            }
        }
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        mListener = activity as ListListener
    }

    override fun onDetach() {
        super.onDetach()
        mListener = null
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mView = inflater.inflate(R.layout.fragment_eventlv_list, container, false) as ViewGroup
        mAdapter = EventLvListRecyclerViewAdapter(mListener!!, activity!!, mutableListOf())
        mListView = mView.findViewById(R.id.list)
        mLoadingView = mView.findViewById(R.id.loading)
        // Set the adapter
        mListView.layoutManager = LinearLayoutManager(mListView.context)
        mListView.adapter = mAdapter
        mPresenter.waitForList()

        return mView
    }

    override fun newEvent(event: EventLV) {
        mListener?.onMoveToCategory(event.category)
        mPresenter.refreshWithCategory(event.category.slug)
    }

    override fun replaceEvent(event: EventLV) {
        mAdapter.updateEvent(event)
    }

    override fun onEmptyEvents() {
        TransitionManager.beginDelayedTransition(mView, Slide())
        mListView.visibility = View.GONE
        mLoadingView.visibility = View.GONE
    }

    override fun displayLoading() {
        mLoadingView.visibility = View.VISIBLE
    }

    override fun onEventsFetched(events: List<EventLV>) {
        TransitionManager.beginDelayedTransition(mView, Slide())
        mAdapter.loadEvents(events.toMutableList())
        mListView.visibility = View.VISIBLE
        mLoadingView.visibility = View.GONE
    }

    override fun onErrorEventsFetch(error : String?) {
        Toast.makeText(activity, getString(R.string.error_list), Toast.LENGTH_LONG).show()
    }

    companion object {
        fun newInstance(): EventLvFragment {
            val fragment = EventLvFragment()
            val args = Bundle()
            fragment.arguments = args

            return fragment
        }
    }

    interface ListListener {
        fun onDisplayDetail(view: View, event: EventLV)
        fun onMoveToCategory(category: Category)
    }
}
