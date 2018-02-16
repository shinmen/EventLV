package com.link_value.eventlv.Presenter.DetailPresenter

import com.link_value.eventlv.Model.EventLV
import com.link_value.eventlv.Model.Partner
import com.link_value.eventlv.View.Detail.SubscriptionView

/**
 * Created by julienb on 29/01/18.
 */
interface DetailPresenter {
    fun start()
    fun isLoggedInUserParticipating(participants: List<Partner>, loggedInPartner: Partner, subscriptionView: SubscriptionView)
    fun subscribeToEvent(eventlv: EventLV, loggedInPartner: Partner)
    fun unsubscribeToEvent(eventlv: EventLV, loggedInPartner: Partner)
    fun onSubscribeSuccess()
    fun onUnSubscribeSuccess()
    fun onErrorSubscription(error: String?)
}