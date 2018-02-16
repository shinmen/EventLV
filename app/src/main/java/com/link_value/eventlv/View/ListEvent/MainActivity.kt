package com.link_value.eventlv.View.ListEvent

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
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
import android.transition.Explode
import android.widget.Toast
import com.link_value.eventlv.Model.Category
import com.link_value.eventlv.Model.EventLV
import com.link_value.eventlv.View.Detail.DetailEventLvActivity
import kotlinx.android.synthetic.main.small_floating_bottom_end.*
import org.greenrobot.eventbus.EventBus


class MainActivity : AppCompatActivity(),
        EventLvFragment.ListListener
    {
    private var mPresenter: ListEventPresenterImpl? = null
    private lateinit var mListEventFragment: EventLvFragment
    private lateinit var mListCategoryTabFragment: ListCategoryTabFragment
    private lateinit var mFabView: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mListEventFragment = EventLvFragment.newInstance()
        mListCategoryTabFragment = ListCategoryTabFragment.newInstance()
        mFabView = findViewById(R.id.fab)
        val explode = Explode()
        window.enterTransition = explode

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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == UPDATE_LIST_CODE) {
            val eventLV = data?.extras?.get(EventLV.PARCEL_NAME) as EventLV
            mPresenter!!.updateParticipantEvent(eventLV)
        }
    }

    @SuppressLint("RestrictedApi")
    override fun onDisplayDetail(view: View, event: EventLV) {
        val i = DetailEventLvActivity.newIntent(this, event)
        val options = ActivityOptionsCompat.makeSceneTransitionAnimation(this, view, resources.getString(R.string.transition_add_btn))
        startActivityForResult(i, UPDATE_LIST_CODE, options.toBundle())
    }

/*    override fun onTabSelected(category: Category) {
        //To change body of created functions use File | Settings | File Templates.
    }*/

    override fun onDestroy() {
        mPresenter = null
        super.onDestroy()
    }

    companion object {
        const val UPDATE_LIST_CODE = 42
        fun newIntent(packageContext: Context, event: EventLV): Intent {
            val intent = Intent(packageContext, MainActivity::class.java)
            intent.putExtra(EventLV.PARCEL_NAME, event)

            return intent
        }
    }
}
