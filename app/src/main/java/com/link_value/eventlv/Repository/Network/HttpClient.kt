package com.link_value.eventlv.Repository.Network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.Retrofit
import com.google.gson.GsonBuilder

/**
 * Created by julienb on 01/12/17.
 */
class HttpClient {

    val retrofit: Retrofit.Builder

    companion object {
        val TAG = HttpClient.javaClass.simpleName
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
        retrofit = Retrofit.Builder()
                //.baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(httpClient.build())
                //.build()
        //api = retrofit.create(HttpEventLvInterface::class.java)
    }

    fun build(request: Class<HttpRequest>, baseUrl: String): HttpRequest {
        val api = retrofit.baseUrl(baseUrl).build()

        return api.create(request)
    }

/*    override fun queryComingEvents(listener: ListEventPresenter) {
        launch(UI) {
            //val response = fetchStreetView()
            //try {
                val list = getComingEvents()
                list.forEach {
                    val response = requestStreetView()
                    it.locationStreetPictureUrl = response.string()
                }
                listener.onSuccessFetchEvents(list)
            *//*} catch (e:Exception) {
                listener.onErrorFetchEvents(e.message!!)
            }*//*

        }*/

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

 /*   override fun fetchStreetView() {
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
    }*/
