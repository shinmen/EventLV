package com.link_value.eventlv.Presenter.DetailPresenter

import com.link_value.eventlv.View.Detail.SubscriptionView

/**
 * Created by julienb on 29/01/18.
 */
interface DetailPresenter {
    fun start()
    fun isLoggedInUserParticipating(subscriptionView: SubscriptionView)
    fun subscribeToEvent()
    fun unsubscribeToEvent()
}