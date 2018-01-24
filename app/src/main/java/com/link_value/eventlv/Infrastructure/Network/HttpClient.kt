package com.link_value.eventlv.Infrastructure.Network

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
        val TAG = HttpClient::class.java.simpleName
    }

    init {
        val logging = HttpLoggingInterceptor()
        // set your desired log level
        logging.level = HttpLoggingInterceptor.Level.BODY
        val httpClient = OkHttpClient.Builder()
        httpClient.addInterceptor(logging)

        val gson = GsonBuilder().setDateFormat("yyyy-M-dd hh:mm:ss").create()
        retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(httpClient.build())
    }
}
