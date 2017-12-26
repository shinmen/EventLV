package com.link_value.eventlv.View.Detail

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcelable
import com.link_value.eventlv.Model.EventLV
import com.link_value.eventlv.R

class DetailEventLvActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_event_lv)
    }

    companion object {
        fun newIntent(packageContext: Context, event: EventLV): Intent {
            val intent = Intent(packageContext, DetailEventLvActivity::class.java)
            intent.putExtra(EventLV.PARCEL_NAME, event)

            return intent
        }
    }
}
