package com.link_value.eventlv.Repository.Network

import com.link_value.eventlv.Model.EventLV
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

/**
 * Created by julienb on 01/12/17.
 */
interface HttpEventLvInterface {
    @GET("eventlv/list")
    fun getComingEvents(): Call<List<EventLV>>

    @POST("eventlv/create")
    fun createEvent(@Body event:EventLV): Call<EventLV>
}