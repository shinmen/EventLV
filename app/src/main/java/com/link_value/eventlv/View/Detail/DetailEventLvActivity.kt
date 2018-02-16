package com.link_value.eventlv.View.Detail

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.transition.Explode
import com.link_value.eventlv.Infrastructure.Network.HttpClient
import com.link_value.eventlv.Model.EventLV
import com.link_value.eventlv.Presenter.DetailPresenter.DetailPresenter
import com.link_value.eventlv.Presenter.DetailPresenter.DetailPresenterImpl
import com.link_value.eventlv.R
import com.link_value.eventlv.Repository.Subscription.SubscriptionRepositoryImpl
import com.link_value.eventlv.View.ListEvent.MainActivity

class DetailEventLvActivity : AppCompatActivity()
{
    private lateinit var mPresenter: DetailPresenter
    private lateinit var mEvent: EventLV

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_event_lv)
        val bundle = intent.extras
        mEvent = bundle.getParcelable(EventLV.PARCEL_NAME) as EventLV

        val detailEventValueFragment = SubscribeEventFragment.newInstance(mEvent)
        val mapEventFragment = MapFragment.newInstance(mEvent)
        val explode = Explode()
        window.enterTransition = explode

        supportFragmentManager
                .beginTransaction()
                .setTransition(android.R.transition.explode)
                .add(R.id.detail_container, mapEventFragment)
                .add(R.id.detail_container, detailEventValueFragment)
                .commit()
        val subscriptionRepo = SubscriptionRepositoryImpl(HttpClient())
        mPresenter = DetailPresenterImpl(detailEventValueFragment, subscriptionRepo)

        detailEventValueFragment.mPresenter = mPresenter
        mPresenter.start()
    }

    override fun onBackPressed() {
        val i = MainActivity.newIntent(this, mEvent)
        setResult(Activity.RESULT_OK, i)
        finish()
        super.onBackPressed()
    }

    companion object {
        fun newIntent(packageContext: Context, event: EventLV): Intent {
            val intent = Intent(packageContext, DetailEventLvActivity::class.java)
            intent.putExtra(EventLV.PARCEL_NAME, event)

            return intent
        }
    }
}
