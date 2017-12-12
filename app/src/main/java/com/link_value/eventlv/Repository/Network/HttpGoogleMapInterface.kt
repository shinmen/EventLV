package com.link_value.eventlv.Repository.Network

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.QueryMap

/**
 * Created by julienb on 08/12/17.
 */
interface HttpGoogleMapInterface {
    companion object {
        val BASE_URL = "https://maps.googleapis.com/maps/api/"
    }

    @GET("streetview")
    fun getStreetView(@QueryMap parameters: Map<String, String>): Call<ResponseBody>
}