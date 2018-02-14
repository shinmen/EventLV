package com.link_value.eventlv.View.Detail

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
import android.support.v4.content.ContextCompat
import android.view.*
import android.widget.*
import com.link_value.eventlv.Presenter.DetailPresenter.DetailPresenter
import org.jetbrains.anko.backgroundDrawable
import org.jetbrains.anko.imageResource
import java.text.DateFormat
import java.util.*


/**
 * Created by julienb on 24/01/18.
 */
class SubscribeEventFragment: Fragment(),
        SubscriptionView
{
    override fun onSubscribed() {
        mPartnerIsParticipating = true
        btnOnParticipatingStatus(mPartnerIsParticipating)
    }

    override fun onUnsubscribe() {
        mPartnerIsParticipating = false
        btnOnParticipatingStatus(mPartnerIsParticipating)
    }

    lateinit var mPresenter: DetailPresenter
    private lateinit var mEventDetail: EventLV
    private lateinit var mParticipants: List<Partner>
    private lateinit var mInitiator: Partner
    private var mPartnerIsParticipating: Boolean = false
    private lateinit var mRecyclerViewAdapter: PartnerAvatarRecyclerViewAdapter
    private lateinit var mRootView: ViewGroup
    private lateinit var participatingBtnView: ImageButton
    private lateinit var mInitiatorAvatarView: ImageView
    private lateinit var picasso: Picasso
    private lateinit var mEventAddressView: TextView
    private lateinit var mEventNameView: TextView
    private lateinit var mEventTimeView: TextView
    private lateinit var mEventLocationNameView: TextView
    private lateinit var mEventCategoryView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
        if (arguments != null) {
            mEventDetail = arguments!!.getParcelable(EVENT_LV) as EventLV
            mParticipants = mEventDetail.participants
            mInitiator = mEventDetail.initiator
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_subscribe_event, container, false)
        mRootView = view.findViewById(R.id.root_subscribe_layout) as ViewGroup
        participatingBtnView = view.findViewById(R.id.participate_btn)
        mInitiatorAvatarView = view.findViewById(R.id.initiator_avatar)
        mEventAddressView = view.findViewById(R.id.event_address)
        mEventNameView = view.findViewById(R.id.event_name)
        mEventTimeView = view.findViewById(R.id.event_time)
        mEventLocationNameView = view.findViewById(R.id.event_location_name)
        mEventCategoryView = view.findViewById(R.id.event_category)
        val partnerListView = view.findViewById<RecyclerView>(R.id.partner_avatar_list)

        val layoutManager = LinearLayoutManager(activity, HORIZONTAL, false)
        mRecyclerViewAdapter = PartnerAvatarRecyclerViewAdapter(activity!!, mParticipants.toMutableList())
        partnerListView.layoutManager = layoutManager
        partnerListView.adapter = mRecyclerViewAdapter

        picasso = Picasso.with(context)
        picasso.setIndicatorsEnabled(false)
        loadLoggedInPartner()
        loadInitiator()
        hydrateEvent()

        mPresenter.isLoggedInUserParticipating(this)

        partnerOnClick()

        return view
    }

    private fun hydrateEvent() {
        mEventAddressView.text = mEventDetail.address
        mEventNameView.text = mEventDetail.title
        mEventLocationNameView.text = mEventDetail.locationName
        val date = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT, Locale.getDefault()).format(mEventDetail.startedAt)
        mEventTimeView.text = date
        mEventCategoryView.text = mEventDetail.category.name
    }

    private fun loadLoggedInPartner() {
        picasso
            .load(MockLoggedInPartner.loggedInPartner.avatarUrl)
            .placeholder(android.R.drawable.picture_frame)
            .fit()
            .into(participatingBtnView, object : com.squareup.picasso.Callback {
                override fun onSuccess() {
                    participate_btn.backgroundDrawable = participate_btn.drawable
                }
                override fun onError() {}
            })
    }

    private fun loadInitiator() {
        picasso
                .load(mInitiator.avatarUrl)
                .placeholder(android.R.drawable.picture_frame)
                .fit()
                .into(mInitiatorAvatarView)
    }

    private fun partnerOnClick() {
        participatingBtnView.onClick {
            participate_btn.setOnClickListener({
                mPartnerIsParticipating = !mPartnerIsParticipating
                val bounds = ChangeBounds()
                bounds.duration = 500
                TransitionManager.beginDelayedTransition(mRootView, bounds)
                btnOnParticipatingStatus(mPartnerIsParticipating)
            })
        }
    }

    private fun btnOnParticipatingStatus(isParticipating: Boolean) {
        if (isParticipating) {
            mRecyclerViewAdapter.addParticipant(MockLoggedInPartner.loggedInPartner)
            participatingBtnView.imageResource = android.R.color.transparent
            participatingBtnView.backgroundTintList = ContextCompat.getColorStateList(activity!!, R.color.colorPrimaryDark)
        } else {
            picasso
                .load(MockLoggedInPartner.loggedInPartner.avatarUrl)
                .placeholder(android.R.drawable.picture_frame)
                .fit()
                .into(participatingBtnView)
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