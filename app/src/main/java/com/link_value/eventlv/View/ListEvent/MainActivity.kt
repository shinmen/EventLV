package com.link_value.eventlv.View.ListEvent

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.link_value.eventlv.R
import com.link_value.eventlv.View.Create.NewEventLvActivity
import kotlinx.android.synthetic.main.activity_main.*
import android.support.v4.app.ActivityOptionsCompat
import android.transition.Explode
import android.view.View
import com.link_value.eventlv.Infrastructure.Network.HttpClient
import com.link_value.eventlv.Presenter.ListPresenter.ListEventPresenterImpl
import com.link_value.eventlv.Repository.List.ListCategoryRepositoryImpl
import com.link_value.eventlv.Repository.List.ListEventRepositoryImpl
import com.link_value.eventlv.Repository.List.StreetViewRepositoryImpl


class MainActivity : AppCompatActivity() {

    private var mPresenter: ListEventPresenterImpl? = null
    private lateinit var mListEventFragment: EventLvFragment
    private lateinit var mListCategoryTabFragment: ListCategoryTabFragment


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
            mListEventFragment = EventLvFragment.newInstance()
            mListCategoryTabFragment = ListCategoryTabFragment.newInstance()
            supportFragmentManager
                    .beginTransaction()
                    .add(android.R.id.content, mListCategoryTabFragment)
                    .add(R.id.list_container, mListEventFragment)
                    .commit()


        onClickGoToNewEvent()
        initPresenter()

        window.allowEnterTransitionOverlap = true
        val explode = Explode()
        window.enterTransition = explode
        window.exitTransition = explode
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

    override fun onDestroy() {
        mPresenter = null
        super.onDestroy()
    }
}
