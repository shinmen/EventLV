package com.link_value.eventlv.View.ListEvent

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
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
import com.link_value.eventlv.View.Detail.DetailEventLvActivity
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe

/**
 * A fragment representing a list of Items.
 *
 */
/**
 * Mandatory empty constructor for the fragment manager to instantiate the
 * fragment (e.g. upon screen orientation changes).
 */
class EventLvFragment : Fragment(), EventListView {

    // TODO: Customize parameters
    private var mColumnCount = 1
    private lateinit var mAdapter:EventLvListRecyclerViewAdapter
    private lateinit var mPresenter: ListEventPresenterImpl

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (arguments != null) {
            mColumnCount = arguments!!.getInt(ARG_COLUMN_COUNT)
        }

        EventBus.getDefault().register(this)
        initPresenter()
    }

    override fun onDestroy() {
        EventBus.getDefault().unregister(this)
        super.onDestroy()
    }

    @Subscribe()
    fun onDisplayDetail(ev: DisplayEventLvDetail) {
        DetailEventLvActivity.newIntent(activity!!, ev.eventLv)
        Toast.makeText(activity, ev.eventLv.title, Toast.LENGTH_LONG).show()
    }

    private fun initPresenter() {
        val streetviewBaseUrl = resources.getString(
                R.string.streetview_picture_base_url,
                "400x400",
                resources.getString(R.string.google_streetview_api_key)
        )
        val streetviewRepo = StreetViewRepositoryImpl(streetviewBaseUrl)
        val repo = ListEventRepositoryImpl(HttpClient(), streetviewRepo)
        mPresenter = ListEventPresenterImpl(this, repo)

        mPresenter.fetchComingEvents()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.fragment_eventlv_list, container, false)
        mAdapter = EventLvListRecyclerViewAdapter(activity!!, emptyList())
        // Set the adapter
        if (view is RecyclerView) {
            val context = view.getContext()
            if (mColumnCount <= 1) {
                view.layoutManager = LinearLayoutManager(context)
            } else {
                view.layoutManager = GridLayoutManager(context, mColumnCount)
            }
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

        // TODO: Customize parameter argument names
        private val ARG_COLUMN_COUNT = "column-count"

        // TODO: Customize parameter initialization
        fun newInstance(columnCount: Int): EventLvFragment {
            val fragment = EventLvFragment()
            val args = Bundle()
            args.putInt(ARG_COLUMN_COUNT, columnCount)
            fragment.arguments = args

            return fragment
        }
    }
}
