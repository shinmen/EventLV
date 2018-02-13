package com.link_value.eventlv.Model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by julienb on 12/02/18.
 */
class EventLocationLatLng :Parcelable {
    @SerializedName("lat")
    @Expose
    var lat: Double
    @SerializedName("lng")
    @Expose
    var lng: Double

    constructor(lat: Double, lng: Double) {
        this.lat = lat
        this.lng = lng
    }

    private constructor(`in`: Parcel) {
        lat = `in`.readDouble()
        lng = `in`.readDouble()
    }
    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeDouble(lat)
        dest.writeDouble(lng)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object {

        var PARCEL_NAME = "location_lat_lng_parcel"

        @JvmField val CREATOR: Parcelable.Creator<EventLocationLatLng> = object : Parcelable.Creator<EventLocationLatLng> {
            override fun createFromParcel(`in`: Parcel) = EventLocationLatLng(`in`)
            override fun newArray(size: Int) = arrayOfNulls<EventLocationLatLng>(size)
        }
    }

}