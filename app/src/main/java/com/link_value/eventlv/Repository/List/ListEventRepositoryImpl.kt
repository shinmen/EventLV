package com.link_value.eventlv.Repository.List

import com.link_value.eventlv.Model.EventLV
import com.link_value.eventlv.Presenter.ListPresenter.ListEventPresenter
import com.link_value.eventlv.Infrastructure.Network.HttpClient
import com.link_value.eventlv.Infrastructure.Network.HttpEventLvInterface
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch
import ru.gildor.coroutines.retrofit.await

/**
 * Created by julienb on 08/12/17.
 */
class ListEventRepositoryImpl(private val httpClient: HttpClient, private val streetViewRepo: StreetViewRepository): ListEventRepository {

    override fun queryComingEvents(listener: ListEventPresenter) {
        val api = httpClient.retrofit.baseUrl(HttpEventLvInterface.BASE_URL).build()
        val repoEvents = api.create(HttpEventLvInterface::class.java)

        launch(UI) {
            try {
                val list = requestComingEvents(repoEvents)
                list.forEach({
                    it.locationStreetPictureUrl = streetViewRepo.getStreetViewUrl(it.address)
                })

                listener.onSuccessFetchEvents(list)
            } catch (ex: Exception) {
                listener.onErrorFetchEvents(ex.message)
            }
        }
    }

    suspend private fun requestComingEvents(repo: HttpEventLvInterface): List<EventLV> {
        return repo.getComingEvents().await()
    }
}