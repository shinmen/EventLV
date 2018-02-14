package com.link_value.eventlv.View.ListEvent

import android.os.Bundle
import android.support.transition.*
import android.support.v4.app.ActivityOptionsCompat
import android.support.v4.app.Fragment
import android.support.v4.widget.ContentLoadingProgressBar
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.link_value.eventlv.Model.Event.DisplayEventLvDetail
import com.link_value.eventlv.Model.Event.MoveToProposalEvent
import com.link_value.eventlv.Model.EventLV
import com.link_value.eventlv.Presenter.ListPresenter.ListEventPresenterImpl
import com.link_value.eventlv.R
import com.link_value.eventlv.Model.Event.TabSelectedEvent
import com.link_value.eventlv.View.Create.NewEventLvActivity
import com.link_value.eventlv.View.Detail.DetailEventLvActivity
import kotlinx.android.synthetic.main.fragment_eventlv_list.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe

/**
 * Mandatory empty constructor for the fragment manager to instantiate the
 * fragment (e.g. upon screen orientation changes).
 */
class EventLvFragment : Fragment(), ListEventView {
    private lateinit var mAdapter:EventLvListRecyclerViewAdapter
    lateinit var mPresenter: ListEventPresenterImpl
    private var tabSelected: String? = null
    private lateinit var mView: ViewGroup
    private lateinit var mListView: RecyclerView
    private lateinit var mLoadingView: ContentLoadingProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
        EventBus.getDefault().register(this)
    }

    override fun onDestroy() {
        EventBus.getDefault().unregister(this)
        super.onDestroy()
    }

    @Subscribe()
    fun onDisplayDetail(ev: DisplayEventLvDetail) {
        val i = DetailEventLvActivity.newIntent(activity!!, ev.eventLv)
        startActivity(i)
    }

    @Subscribe
    fun onTabSelected(ev: TabSelectedEvent) {
        if (ev.category.name != tabSelected) {
            tabSelected = ev.category.name
            if (tabSelected == resources.getString(R.string.all_types)) {
                mPresenter.waitForList()
                mPresenter.fetchAll()
            } else {
                mPresenter.waitForList()
                mPresenter.refreshWithCategory(ev.category.slug)
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mView = inflater.inflate(R.layout.fragment_eventlv_list, container, false) as ViewGroup
        mAdapter = EventLvListRecyclerViewAdapter(activity!!, emptyList())
        mListView = mView.findViewById(R.id.list)
        mLoadingView = mView.findViewById(R.id.loading)
        // Set the adapter
        mListView.layoutManager = LinearLayoutManager(mListView.context)
        mListView.adapter = mAdapter
        mPresenter.waitForList()

        return mView
    }

    override fun onEmptyEvents() {
        TransitionManager.beginDelayedTransition(mView, Slide())
        mListView.visibility = View.GONE
        mLoadingView.visibility = View.GONE
        EventBus.getDefault().post(MoveToProposalEvent())
    }

    override fun displayLoading() {
        mLoadingView.visibility = View.VISIBLE
    }

    override fun onEventsFetched(events: List<EventLV>) {
        TransitionManager.beginDelayedTransition(mView, Slide())
        mAdapter.loadEvents(events)
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
}
