package com.link_value.eventlv.View.Create

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import com.link_value.eventlv.R


class NewEventLvActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_event_lv)
    }

    fun newIntent(packageContext: Context): Intent {
        val intent = Intent(packageContext, NewEventLvActivity::class.java)

        return intent
    }

}
