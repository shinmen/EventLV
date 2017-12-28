package com.link_value.eventlv.Repository.List

import com.link_value.eventlv.Repository.List.StreetViewRepository
import java.net.URLEncoder

/**
 * Created by julienb on 08/12/17.
 */
class StreetViewRepositoryImpl(private val baseUrl: String): StreetViewRepository {

    override fun getStreetViewUrl(address: String): String {
        val builder = StringBuilder(baseUrl)
        builder.append(URLEncoder.encode(address, "UTF-8"))

        return builder.toString()
    }
}