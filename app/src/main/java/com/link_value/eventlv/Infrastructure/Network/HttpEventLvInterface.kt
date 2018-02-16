package com.link_value.eventlv.Infrastructure.Network

import com.link_value.eventlv.Model.Category
import com.link_value.eventlv.Model.EventLV
import retrofit2.Call
import retrofit2.http.*

/**
 * Created by julienb on 01/12/17.
 */
interface HttpEventLvInterface {
    companion object {
        const val BASE_URL = "http://api.jbouffard.fr"
    }

    @GET("eventlv/list")
    fun getComingEvents(): Call<List<EventLV>>

    @GET("eventlv/list/{category}")
    fun getFilteredEvents(@Path("category") category: String): Call<List<EventLV>>

    @POST("eventlv/create")
    fun saveEvent(@Body event:EventLV): Call<EventLV>

    @GET("eventlv/category/list")
    fun getCategories(): Call<List<Category>>

    @POST("eventlv/subscribe/{uuid}/{username}")
    fun subscribeEvent(@Path("uuid") uuid: String, @Path("username") username: String): Call<Any>

    @POST("eventlv/unsubscribe/{uuid}/{username}")
    fun unSubscribeEvent(@Path("uuid") uuid: String, @Path("username") username: String): Call<Any>
}