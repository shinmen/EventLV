package com.link_value.eventlv.Common

import android.app.TimePickerDialog
import android.content.Intent
import android.app.Activity
import android.app.Dialog
import android.widget.TimePicker
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.util.Log
import com.link_value.eventlv.Event.PostTimeEvent
import com.link_value.eventlv.R
import org.greenrobot.eventbus.EventBus
import java.util.*


/**
 * Created by julienb on 26/12/17.
 */
class TimePickerDialogFragment: DialogFragment(),
        TimePickerDialog.OnTimeSetListener
{
    companion object {
        val SAVED_TIME = "timepicker_fragment.time"
        private val ARG_TIME = "time"

        fun newInstance(date: Date): TimePickerDialogFragment {
            val args = Bundle()
            args.putSerializable(ARG_TIME, date)

            val fragment = TimePickerDialogFragment()
            fragment.arguments= args

            return fragment
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        // Use the current time as the default values for the picker
        val c = Calendar.getInstance()
        val hour = c.get(Calendar.HOUR_OF_DAY)
        val minute = c.get(Calendar.MINUTE)
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.AppTheme)

        // Create a new instance of TimePickerDialog and return it
        return TimePickerDialog(activity, this, hour, minute, true)
    }

    override fun onTimeSet(view: TimePicker, hourOfDay: Int, minute: Int) {
        val date = GregorianCalendar(1970, 1, 1, hourOfDay, minute, 0).time
        EventBus.getDefault().post(PostTimeEvent(date))
    }
}