package com.link_value.eventlv.Repository.Network

import com.link_value.eventlv.Model.EventLV
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.QueryMap

/**
 * Created by julienb on 01/12/17.
 */
interface HttpEventLvInterface {
    @GET("eventlv/list")
    fun getComingEvents(): Call<List<EventLV>>

    @POST("eventlv/create")
    fun createEvent(@Body event:EventLV): Call<EventLV>

    @GET("streetview")
    fun getStreetView(@QueryMap parameters: Map<String, String>): Call<ResponseBody>
}