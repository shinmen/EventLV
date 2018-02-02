package com.link_value.eventlv.View.Detail

import android.graphics.drawable.Drawable
import android.support.v4.app.Fragment
import android.os.Bundle
import com.link_value.eventlv.Model.EventLV
import com.link_value.eventlv.Model.Partner
import com.link_value.eventlv.R
import kotlinx.android.synthetic.main.fragment_subscribe_event.*
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.LinearLayout.HORIZONTAL
import com.link_value.eventlv.Model.MockLoggedInPartner
import com.squareup.picasso.Picasso
import org.jetbrains.anko.sdk25.coroutines.onClick
import android.support.transition.*
import android.view.*
import android.widget.*
import org.jetbrains.anko.backgroundDrawable
import org.jetbrains.anko.imageResource


/**
 * Created by julienb on 24/01/18.
 */
class SubscribeEventFragment: Fragment(),
        SubscriptionView
{
    override fun onSubscribed() {
    }

    override fun onUnsubscribe() {
    }

    private lateinit var mEventDetail: EventLV
    private lateinit var mParticipants: List<Partner>
    private lateinit var mInitiator: Partner
    private var mPartnerIsParticipating: Boolean = false
    private lateinit var mSceneImage: ImageView
    private lateinit var mRecyclerViewAdapter: PartnerAvatarRecyclerViewAdapter
    private var mLocationList: IntArray = IntArray(2)
    private lateinit var mSceneRoot: ViewGroup
    var mAvatar: Drawable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            mEventDetail = arguments!!.getParcelable(EVENT_LV) as EventLV
            mParticipants = mEventDetail.participants
            mInitiator = mEventDetail.initiator
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_subscribe_event, container, false)
        mSceneRoot = view.findViewById(R.id.root_subscribe_layout) as ViewGroup

        val layoutManager = LinearLayoutManager(activity, HORIZONTAL, false)
        mRecyclerViewAdapter = PartnerAvatarRecyclerViewAdapter(activity!!, mParticipants.toMutableList())
        val partnerList = view.findViewById(R.id.partner_avatar_list) as RecyclerView
        val participatingBtn = view.findViewById(R.id.participate_btn) as ImageButton
        mSceneImage = view.findViewById(R.id.scene_avatar_origin) as ImageView

        partnerList.layoutManager = layoutManager
        partnerList.adapter = mRecyclerViewAdapter
        val picasso = Picasso.with(context)
        picasso.setIndicatorsEnabled(true)

        picasso
                .load(MockLoggedInPartner.loggedInPartner.avatarUrl)
                .placeholder(android.R.drawable.picture_frame)
                .fit()
                .into(participatingBtn, object : com.squareup.picasso.Callback {
                    override fun onSuccess() {
                        mAvatar = participate_btn.drawable
                        participate_btn.backgroundDrawable = mAvatar
                    }

                    override fun onError() {
                    }
                })

        participatingBtn.onClick {
            partnerOnClick()
        }
        TransitionManager.beginDelayedTransition(partnerList)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val location = IntArray(2)
        mSceneImage.getLocationOnScreen(location)
        super.onViewCreated(view, savedInstanceState)
    }


    override fun onDestroy() {
        super.onDestroy()

    }

    private fun partnerOnClick() {
        participate_btn.setOnClickListener({
            mPartnerIsParticipating = !mPartnerIsParticipating
            val bounds = ChangeBounds()
            bounds.duration = 1000
            TransitionManager.beginDelayedTransition(mSceneRoot, bounds)
        })
    }




    private fun btnColorOnParticipatingStatus(isParticipating: Boolean) {
        val colorIn = resources.getColorStateList(android.R.color.holo_green_dark)
        val colorOut = resources.getColorStateList(android.R.color.holo_red_dark)
        if (isParticipating) {
            participate_btn.imageResource = android.R.color.transparent
            participate_btn.backgroundTintList = colorIn
            mRecyclerViewAdapter.addParticipant(MockLoggedInPartner.loggedInPartner)

        } else {
            val picasso = Picasso.with(context)
            picasso.setIndicatorsEnabled(true)

            participate_btn.backgroundDrawable = mAvatar
            participate_btn.backgroundTintList = colorOut
            mRecyclerViewAdapter.removeParticipant(MockLoggedInPartner.loggedInPartner)
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