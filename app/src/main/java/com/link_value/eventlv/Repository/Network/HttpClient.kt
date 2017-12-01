package com.link_value.eventlv.Repository.Network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.Retrofit
import com.google.gson.GsonBuilder
import com.google.gson.Gson
import com.link_value.eventlv.Model.EventLV
import com.link_value.eventlv.Presenter.ListEventPresenter
import com.link_value.eventlv.Repository.List.ListEventRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


/**
 * Created by julienb on 01/12/17.
 */
class HttpClient: ListEventRepository {

    private val api :HttpEventLvInterface

    companion object {
        val TAG = HttpClient.javaClass.simpleName
        val BASE_URL = "api.jbouffard.fr"
    }

    init {
        val logging = HttpLoggingInterceptor()
        // set your desired log level
        logging.level = HttpLoggingInterceptor.Level.BODY
        val httpClient = OkHttpClient.Builder()

        val gson = GsonBuilder().create()
        val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(httpClient.build())
                .build()
        api = retrofit.create(HttpEventLvInterface::class.java)
    }

    override fun queryComingEvents(listener: ListEventPresenter) {
        val call:Call<List<EventLV>> = api.getComingEvents()

        call.enqueue(object : Callback<List<EventLV>> {
            override fun onResponse(call: Call<List<EventLV>>, response: Response<List<EventLV>>) {
                if (response.code() < 300) {
                    listener.onSucessFetchEvents(response.body()!!)
                } else {
                    listener.onErrorFetchEvents(response.message())
                }
            }

            override fun onFailure(call: Call<List<EventLV>>, t: Throwable) {
                listener.onErrorFetchEvents(t.localizedMessage)
                t.printStackTrace()
            }
        })
    }
}