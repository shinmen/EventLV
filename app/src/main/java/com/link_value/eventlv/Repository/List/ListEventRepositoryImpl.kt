package com.link_value.eventlv.Repository.List

import com.link_value.eventlv.Model.EventLV
import com.link_value.eventlv.Presenter.ListPresenter.ListEventPresenter
import com.link_value.eventlv.Infrastructure.Network.HttpClient
import com.link_value.eventlv.Infrastructure.Network.HttpEventLvInterface
import com.link_value.eventlv.View.ListEvent.ListEventView
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch
import ru.gildor.coroutines.retrofit.await
import java.util.Comparator

/**
 * Created by julienb on 08/12/17.
 */
class ListEventRepositoryImpl(
        private val httpClient: HttpClient,
        private val streetViewRepo: StreetViewRepository
    ): ListEventRepository {

    override fun queryEventsByCategory(category:String, listener: ListEventView) {
        val api = httpClient.retrofit.baseUrl(HttpEventLvInterface.BASE_URL).build()
        val repoEvents = api.create(HttpEventLvInterface::class.java)

        launch(UI) {
            try {
                val list = repoEvents.getFilteredEvents(category).await()
                list
                    .forEach({
                        it.locationStreetPictureUrl = streetViewRepo.getStreetViewUrl(it.address)
                    })
                if (list.isEmpty()) {
                    listener.onEmptyEvents()
                } else {
                    listener.onEventsFetched(list)
                }
            } catch (ex: Exception) {
                listener.onErrorEventsFetch(ex.message)
            }
        }
    }

    override fun queryComingEvents(listener: ListEventView) {
        val api = httpClient.retrofit.baseUrl(HttpEventLvInterface.BASE_URL).build()
        val repoEvents = api.create(HttpEventLvInterface::class.java)

        launch(UI) {
            try {
                val list = repoEvents.getComingEvents().await()
                //val byDate = Comparator{ o1: EventLV, o2: EventLV -> o1.startedAt?.compareTo(o2.startedAt) }
                list.sortedWith(compareBy())
                   .forEach({
                        it.locationStreetPictureUrl = streetViewRepo.getStreetViewUrl(it.address)
                    })

                listener.onEventsFetched(list)
            } catch (ex: Exception) {
                listener.onErrorEventsFetch(ex.message)
            }
        }
    }
}