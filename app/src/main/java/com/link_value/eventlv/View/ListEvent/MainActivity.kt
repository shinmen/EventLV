package com.link_value.eventlv.View.ListEvent

import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v7.app.AppCompatActivity
import com.link_value.eventlv.R
import com.link_value.eventlv.View.Create.NewEventLvActivity
import kotlinx.android.synthetic.main.activity_main.*
import android.support.v4.app.ActivityOptionsCompat
import android.view.View
import com.link_value.eventlv.Infrastructure.Network.HttpClient
import com.link_value.eventlv.Model.Event.MoveToProposalEvent
import com.link_value.eventlv.Presenter.ListPresenter.ListEventPresenterImpl
import com.link_value.eventlv.Repository.List.ListCategoryRepositoryImpl
import com.link_value.eventlv.Repository.List.ListEventRepositoryImpl
import com.link_value.eventlv.Repository.List.StreetViewRepositoryImpl
import org.greenrobot.eventbus.Subscribe
import android.support.v4.view.GravityCompat
import android.view.Gravity
import android.support.design.widget.CoordinatorLayout
import android.support.v7.widget.WithHint
import android.widget.Toast
import kotlinx.android.synthetic.main.small_floating_bottom_end.*
import org.greenrobot.eventbus.EventBus


class MainActivity : AppCompatActivity() {

    private var mPresenter: ListEventPresenterImpl? = null
    private lateinit var mListEventFragment: EventLvFragment
    private lateinit var mListCategoryTabFragment: ListCategoryTabFragment
    private lateinit var mFabView: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        EventBus.getDefault().register(this)
        mListEventFragment = EventLvFragment.newInstance()
        mListCategoryTabFragment = ListCategoryTabFragment.newInstance()
        mFabView = findViewById(R.id.fab)

        supportFragmentManager
                .beginTransaction()
                .add(android.R.id.content, mListCategoryTabFragment)
                .add(R.id.list_container, mListEventFragment)
                .commit()

        onClickGoToNewEvent()
        initPresenter()
    }

    private fun onClickGoToNewEvent() {
        fab.setOnClickListener { view ->
            val i = NewEventLvActivity.newIntent(this)
            val options = ActivityOptionsCompat.makeSceneTransitionAnimation(this, view as View, resources.getString(R.string.transition_add_btn))
            startActivity(i, options.toBundle())
        }
    }

    @Subscribe()
    fun onProposeEvent(event: MoveToProposalEvent) {
        val lp = fab.layoutParams as CoordinatorLayout.LayoutParams
        lp.anchorGravity = Gravity.CENTER_HORIZONTAL
        lp.width = 50
        lp.height = 50
        mFabView.layoutParams = lp
    }

    private fun initPresenter() {
        val streetViewBaseUrl = resources.getString(
                R.string.streetview_picture_base_url,
                "400x400",
                resources.getString(R.string.google_streetview_api_key)
        )
        val streetViewRepo = StreetViewRepositoryImpl(streetViewBaseUrl)
        val eventRepo = ListEventRepositoryImpl(HttpClient(), streetViewRepo)
        val categoryRepo = ListCategoryRepositoryImpl(HttpClient())
        mPresenter = ListEventPresenterImpl(mListEventFragment, mListCategoryTabFragment, eventRepo, categoryRepo)
        mListEventFragment.mPresenter = mPresenter!!
        mListCategoryTabFragment.mPresenter = mPresenter!!
        mPresenter!!.start()
    }

    override fun onDestroy() {
        mPresenter = null
        EventBus.getDefault().unregister(this)
        super.onDestroy()
    }
}
