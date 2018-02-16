package com.link_value.eventlv.Repository.Subscription

import com.link_value.eventlv.Infrastructure.Network.HttpClient
import com.link_value.eventlv.Infrastructure.Network.HttpEventLvInterface
import com.link_value.eventlv.Presenter.DetailPresenter.DetailPresenter
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch
import ru.gildor.coroutines.retrofit.await

/**
 * Created by julienb on 15/02/18.
 */
class SubscriptionRepositoryImpl(private val httpClient: HttpClient): SubscriptionRepository {

    override fun subscribeEvent(uuid: String, username: String, listener: DetailPresenter) {
        val api = httpClient.retrofit.baseUrl(HttpEventLvInterface.BASE_URL).build()
        val repoEvents = api.create(HttpEventLvInterface::class.java)

        launch(UI) {
            try {
                repoEvents.subscribeEvent(uuid, username).await()
                listener.onSubscribeSuccess()
            } catch (ex: Exception) {
                listener.onErrorSubscription(ex.message)
            }
        }
    }

    override fun unsubscribeEvent(uuid: String, username: String, listener: DetailPresenter) {
        val api = httpClient.retrofit.baseUrl(HttpEventLvInterface.BASE_URL).build()
        val repoEvents = api.create(HttpEventLvInterface::class.java)

        launch(UI) {
            try {
                repoEvents.unSubscribeEvent(uuid, username).await()
                listener.onUnSubscribeSuccess()
            } catch (ex: Exception) {
                listener.onErrorSubscription(ex.message)
            }
        }
    }

}