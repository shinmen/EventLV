package com.link_value.eventlv.View.Detail

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcelable
import android.transition.Explode
import android.transition.Slide
import android.view.Window
import com.link_value.eventlv.Model.EventLV
import com.link_value.eventlv.Presenter.DetailPresenter.DetailPresenter
import com.link_value.eventlv.Presenter.DetailPresenter.DetailPresenterImpl
import com.link_value.eventlv.R

class DetailEventLvActivity : AppCompatActivity()
{
    private lateinit var mPresenter: DetailPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_event_lv)
        val bundle = intent.extras
        val event = bundle.getParcelable(EventLV.PARCEL_NAME) as EventLV
        val detailEventValueFragment = SubscribeEventFragment.newInstance(event)
        val mapEventFragment = MapFragment.newInstance(event)

        supportFragmentManager
                .beginTransaction()
                .setTransition(android.R.transition.explode)
                .add(R.id.detail_container, mapEventFragment)
                .add(R.id.detail_container, detailEventValueFragment)
                .commit()

        mPresenter = DetailPresenterImpl(detailEventValueFragment)
        detailEventValueFragment.mPresenter = mPresenter
        mPresenter.start()
    }

    companion object {
        fun newIntent(packageContext: Context, event: EventLV): Intent {
            val intent = Intent(packageContext, DetailEventLvActivity::class.java)
            intent.putExtra(EventLV.PARCEL_NAME, event)

            return intent
        }
    }
}
