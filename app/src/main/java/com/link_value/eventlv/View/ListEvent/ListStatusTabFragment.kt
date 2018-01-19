package com.link_value.eventlv.View.ListEvent

import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.link_value.eventlv.Model.Event.TabSelectedEvent
import com.link_value.eventlv.R
import kotlinx.android.synthetic.main.fragment_liststatustab.*
import org.greenrobot.eventbus.EventBus

/**
 * Created by julienb on 19/01/18.
 */
class ListStatusTabFragment: Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_liststatustab, container, false)
        return view
    }

    override fun onStart() {
        tab_list.addOnTabSelectedListener(object: TabLayout.OnTabSelectedListener{
            override fun onTabReselected(tab: TabLayout.Tab?) {
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabSelected(tab: TabLayout.Tab?) {
                EventBus.getDefault().post(TabSelectedEvent(tab!!.text.toString()))
            }

        })
        super.onStart()
    }

    companion object {

        fun newInstance(): ListStatusTabFragment {
            val fragment = ListStatusTabFragment()
            val args = Bundle()
            fragment.arguments = args

            return fragment
        }
    }
}