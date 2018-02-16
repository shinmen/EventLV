package com.link_value.eventlv.Repository.Subscription

import com.link_value.eventlv.Presenter.DetailPresenter.DetailPresenter

/**
 * Created by julienb on 15/02/18.
 */
interface SubscriptionRepository {
    fun subscribeEvent(uuid: String, username: String, listener: DetailPresenter)
    fun unsubscribeEvent(uuid: String, username: String, listener: DetailPresenter)
}