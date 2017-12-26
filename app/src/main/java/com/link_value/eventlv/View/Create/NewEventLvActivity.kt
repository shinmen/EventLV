package com.link_value.eventlv.View.Create

import android.app.DialogFragment
import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.widget.Toast
import com.link_value.eventlv.Common.DatePickerDialogFragment
import com.link_value.eventlv.Common.TimePickerDialogFragment
import com.link_value.eventlv.Event.PostDateEvent
import com.link_value.eventlv.Event.PostTimeEvent
import com.link_value.eventlv.R
import kotlinx.android.synthetic.main.activity_new_event_lv.*
import org.greenrobot.eventbus.EventBus
import org.jetbrains.anko.sdk25.coroutines.onClick
import java.util.*
import org.greenrobot.eventbus.Subscribe


class NewEventLvActivity : AppCompatActivity()
{

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_event_lv)
        EventBus.getDefault().register(this)

        event_date.onClick {
            val datePickerFragment = DatePickerDialogFragment.newInstance(Date())
            datePickerFragment.show(supportFragmentManager, "date")
        }

        event_time.onClick {
            val timePickerFragment = TimePickerDialogFragment.newInstance(Date())
            timePickerFragment.show(supportFragmentManager, "time")
        }
    }

    override fun onDestroy() {
        EventBus.getDefault().unregister(this)
        super.onDestroy()
    }

    @Subscribe()
    fun onDatePicked(date: PostDateEvent) {
        event_date.text = date.toString()
    }

    @Subscribe()
    fun onTimePicked(date: PostTimeEvent) {
        event_time.text = date.toString()
    }

    companion object {
        private val REQUEST_DATE = 0
        private val REQUEST_TIME = 1

        fun newIntent(packageContext: Context): Intent {
            val intent = Intent(packageContext, NewEventLvActivity::class.java)

            return intent
        }
    }
}
