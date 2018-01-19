package com.link_value.eventlv.Repository.Create

import com.link_value.eventlv.Infrastructure.Network.HttpClient
import com.link_value.eventlv.Infrastructure.Network.HttpEventLvInterface
import com.link_value.eventlv.Model.EventLV
import com.link_value.eventlv.Presenter.CreatePresenter.CreateEventPresenter
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch

/**
 * Created by julienb on 19/01/18.
 */
class NewEventRepositoryImpl(private val httpClient: HttpClient): NewEventRepository {

    override fun saveEvent(event: EventLV,listener: CreateEventPresenter) {
        val api = httpClient.retrofit.baseUrl(HttpEventLvInterface.BASE_URL).build()
        val repoEvents = api.create(HttpEventLvInterface::class.java)

        launch(UI) {
            try {
                repoEvents.saveEvent(event)

                listener.onSuccessEventSaved()
            } catch (ex: Exception) {
                listener.onErrorEventSaved(ex.message)
            }
        }
    }

}