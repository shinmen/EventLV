package com.link_value.eventlv.View.Detail

import android.support.v4.app.Fragment
import android.os.Bundle
import com.link_value.eventlv.Model.EventLV
import com.link_value.eventlv.Model.Partner
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.View
import com.link_value.eventlv.R
import kotlinx.android.synthetic.main.fragment_subscribe_event.*
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.LinearLayout.HORIZONTAL
import org.jetbrains.anko.support.v4.find


/**
 * Created by julienb on 24/01/18.
 */
class SubscribeEventFragment: Fragment() {

    private lateinit var eventDetail: EventLV
    private lateinit var participants: List<Partner>
    private var partnerIsParticipating: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            eventDetail = arguments!!.getParcelable(EVENT_LV) as EventLV
            participants = eventDetail.participants
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_subscribe_event, container, false)
        val layoutManager = LinearLayoutManager(activity, HORIZONTAL, false)
        val recyclerViewAdapter = PartnerAvatarRecyclerViewAdapter(activity!!, participants.toMutableList())
        val partnerList = view.findViewById(R.id.partner_avatar_list) as RecyclerView
        partnerList.layoutManager = layoutManager
        partnerList.adapter = recyclerViewAdapter

        return view
    }

    override fun onDestroy() {
        super.onDestroy()

    }

    private fun partnerOnClick() {
        participate_btn.setOnClickListener({
            if (!partnerIsParticipating) {
                partnerIsParticipating = true

            } else {
                partnerIsParticipating = false

            }
        })
    }

    private fun btnColorOnParticipatingStatus(isParticipating: Boolean) {
        val colorIn = resources.getColorStateList(android.R.color.holo_green_dark)
        val colorOut = resources.getColorStateList(android.R.color.holo_red_dark)
        if (isParticipating) {
            participate_btn.backgroundTintList = colorIn
        } else {
            participate_btn.backgroundTintList = colorOut
        }
    }


    companion object {
        private const val EVENT_LV = "subscribe_event_fragment"

        fun newInstance(eventLv: EventLV): SubscribeEventFragment {
            val fragment = SubscribeEventFragment()
            val args = Bundle()
            args.putParcelable(EVENT_LV, eventLv)
            fragment.arguments = args

            return fragment
        }

    }
}