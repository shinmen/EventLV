package com.link_value.eventlv.View.ListEvent

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.link_value.eventlv.Model.Event.DisplayEventLvDetail
import com.link_value.eventlv.Model.EventLV
import com.link_value.eventlv.Presenter.ListPresenter.ListEventPresenterImpl
import com.link_value.eventlv.R
import com.link_value.eventlv.Repository.List.ListEventRepositoryImpl
import com.link_value.eventlv.Repository.List.StreetViewRepositoryImpl
import com.link_value.eventlv.Infrastructure.Network.HttpClient
import com.link_value.eventlv.Model.Event.TabSelectedEvent
import com.link_value.eventlv.View.Detail.DetailEventLvActivity
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
        Toast.makeText(activity, ev.eventLv.title, Toast.LENGTH_LONG).show()
    }

    @Subscribe
    fun onTabSelected(ev: TabSelectedEvent) {
        if (ev.value != tabSelected) {
            tabSelected = ev.value
            mPresenter.refreshWithCategory(ev.value)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_eventlv_list, container, false)
        mAdapter = EventLvListRecyclerViewAdapter(activity!!, emptyList())
        // Set the adapter
        if (view is RecyclerView) {
            val context = view.getContext()
            view.layoutManager = LinearLayoutManager(context)
            view.adapter = mAdapter
        }

        return view
    }

    override fun onEventsFetched(events: List<EventLV>) {
        mAdapter.loadEvents(events)
    }

    override fun onErrorEventsFetch(error : String?) {
        Toast.makeText(activity, error, Toast.LENGTH_LONG).show()
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
