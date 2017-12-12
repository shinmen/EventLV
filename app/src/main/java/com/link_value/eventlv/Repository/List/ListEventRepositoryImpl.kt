package com.link_value.eventlv.Repository.List

import com.link_value.eventlv.Model.EventLV
import com.link_value.eventlv.Presenter.ListEventPresenter
import com.link_value.eventlv.Repository.Network.HttpClient
import com.link_value.eventlv.Repository.Network.HttpEventLvInterface
import com.link_value.eventlv.Repository.Network.HttpGoogleMapInterface
import com.link_value.eventlv.Repository.Network.HttpRequest
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.launch
import retrofit2.Call
import ru.gildor.coroutines.retrofit.await
import java.net.URLEncoder

/**
 * Created by julienb on 08/12/17.
 */
class ListEventRepositoryImpl(private val httpClient: HttpClient): ListEventRepository {

    override fun queryComingEvents(listener: ListEventPresenter) {
        val api = httpClient.retrofit.baseUrl(HttpEventLvInterface.BASE_URL).build()
        val repoEvents = api.create(HttpEventLvInterface::class.java)
        val repoStreetView = api.create(HttpGoogleMapInterface::class.java)

        launch(UI) {
            val list = requestComingEvents(repoEvents)
            list.forEach({
                val builder = StringBuilder("https://maps.googleapis.com/maps/api/streetview?key=AIzaSyDzTRrAfaiouHUeuRuXuLhzlvukzMXUFsE&size=400x400&location=")
                builder.append(URLEncoder.encode(it.address, "UTF-8"))
                it.locationStreetPictureUrl = builder.toString()
            })

            listener.onSuccessFetchEvents(list)
        }
    }

    suspend private fun requestComingEvents(repo: HttpEventLvInterface): List<EventLV> {
        return repo.getComingEvents().await()
    }

    //suspend private fun requestStreetView(repo: HttpGoogleMapInterface):
}