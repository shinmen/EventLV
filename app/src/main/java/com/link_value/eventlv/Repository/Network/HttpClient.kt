package com.link_value.eventlv.Repository.Network

import android.support.v4.util.ArrayMap
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.Retrofit
import com.google.gson.GsonBuilder
import com.link_value.eventlv.Model.EventLV
import com.link_value.eventlv.Presenter.ListEventPresenter
import com.link_value.eventlv.Repository.List.ListEventRepository
import com.link_value.eventlv.Repository.List.StreetViewRepository
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.gildor.coroutines.retrofit.await


/**
 * Created by julienb on 01/12/17.
 */
class HttpClient: ListEventRepository, StreetViewRepository {

    private val api :HttpEventLvInterface

    companion object {
        val TAG = HttpClient.javaClass.simpleName
        val BASE_URL = "http://api.jbouffard.fr"
    }

    init {
        val logging = HttpLoggingInterceptor()
        // set your desired log level
        logging.level = HttpLoggingInterceptor.Level.BODY
        val httpClient = OkHttpClient.Builder()
        httpClient.addInterceptor(logging)

        val gson = GsonBuilder()
                .setDateFormat("yyyy-M-dd hh:mm:ss")
                .create()
        val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(httpClient.build())
                .build()
        api = retrofit.create(HttpEventLvInterface::class.java)
    }

    override fun queryComingEvents(listener: ListEventPresenter) {
        launch(UI) {
            //val response = fetchStreetView()
            //try {
                val list = getComingEvents()
                list.forEach {
                    //val response = requestStreetView()
                    //it.locationStreetPictureUrl = response.string()
                }
                listener.onSuccessFetchEvents(list)
            /*} catch (e:Exception) {
                listener.onErrorFetchEvents(e.message!!)
            }*/

        }

       /* val call:Call<List<EventLV>> = api.getComingEvents()

        call.enqueue(object : Callback<List<EventLV>> {
            override fun onResponse(call: Call<List<EventLV>>, response: Response<List<EventLV>>) {
                if (response.code() < 300) {
                    listener.onSuccessFetchEvents(response.body()!!)
                } else {
                    listener.onErrorFetchEvents(response.message())
                }
            }

            override fun onFailure(call: Call<List<EventLV>>, t: Throwable) {
                listener.onErrorFetchEvents(t.localizedMessage)
                t.printStackTrace()
            }
        })*/
    }

    override fun fetchStreetView() {
        launch(UI) {
            val response = requestStreetView()

        }
    }

    suspend fun getComingEvents(): List<EventLV> {
        return api.getComingEvents().await()
    }

    suspend fun requestStreetView(): ResponseBody {
        val map = ArrayMap<String, String>()
        map.put("location", "26+Rue+des+Lombards+75004+Paris")
        map.put("key", "AIzaSyDzTRrAfaiouHUeuRuXuLhzlvukzMXUFsE")
        map.put("size", "400x400")

        return api.getStreetView(map).await()
    }
}