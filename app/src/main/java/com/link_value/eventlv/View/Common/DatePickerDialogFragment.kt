package com.link_value.eventlv.View.Common

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.widget.DatePicker
import com.link_value.eventlv.Model.Event.PostDateEvent
import com.link_value.eventlv.R
import org.greenrobot.eventbus.EventBus
import java.util.*

/**
 * Created by julienb on 26/12/17.
 */
class DatePickerDialogFragment : DialogFragment(),
        DatePickerDialog.OnDateSetListener
{
    companion object {
        private val ARG_DATE = "date"

        fun newInstance(date: Date): DatePickerDialogFragment {
            val args = Bundle()
            args.putSerializable(ARG_DATE, date)
            val fragment = DatePickerDialogFragment()
            fragment.arguments = args

            return fragment
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val date = arguments!!.getSerializable(ARG_DATE) as Date
        // Use the current time as the default values for the picker
        val c = Calendar.getInstance()
        c.time = date
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.AppTheme)

        // Create a new instance of TimePickerDialog and return it
        return DatePickerDialog(activity, this, year, month, day)
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        // Do something with the date chosen by the user
        val date = GregorianCalendar(year, month, dayOfMonth).time
        EventBus.getDefault().post(PostDateEvent(date))
    }
}