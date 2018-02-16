package com.link_value.eventlv.Presenter.DetailPresenter

import com.link_value.eventlv.Model.EventLV
import com.link_value.eventlv.Model.MockLoggedInPartner
import com.link_value.eventlv.Model.Partner
import com.link_value.eventlv.Repository.Subscription.SubscriptionRepository
import com.link_value.eventlv.View.Detail.SubscriptionView
import java.util.*

/**
 * Created by julienb on 29/01/18.
 */
class DetailPresenterImpl(
        private val subscriptionView: SubscriptionView,
        private val subscriptionRepo: SubscriptionRepository
) : DetailPresenter {
    override fun onSubscribeSuccess() {
        subscriptionView.onSubscribed()
    }

    override fun onUnSubscribeSuccess() {
        subscriptionView.onUnsubscribed()
    }

    override fun onErrorSubscription(error: String?) {
        subscriptionView.onErrorSubscription()
    }

    override fun start() {

    }

    override fun isLoggedInUserParticipating(participants: List<Partner>, loggedInPartner: Partner, subscriptionView: SubscriptionView) {
        val found = participants.find { it.username == loggedInPartner.username }
        if (found != null) {
            subscriptionView.hasAlreadySubscribed()
        } else {
            subscriptionView.hasNotSubscribedYet()
        }
    }

    override fun subscribeToEvent(eventlv: EventLV, loggedInPartner: Partner) {
        subscriptionRepo.subscribeEvent(eventlv.uuid, loggedInPartner.username, this)
    }

    override fun unsubscribeToEvent(eventlv: EventLV, loggedInPartner: Partner) {
        subscriptionRepo.unsubscribeEvent(eventlv.uuid, loggedInPartner.username, this)
    }
}