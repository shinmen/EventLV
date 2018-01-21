package com.link_value.eventlv.Model

import android.os.Parcel
import android.os.Parcelable

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.util.*

/**
 * Created by julienb on 29/11/17.
 */

class EventLV : Parcelable {

    @SerializedName("title")
    @Expose
    var title: String

    @SerializedName("startedAt")
    @Expose
    var startedAt: Date? = null

    @SerializedName("duration")
    @Expose
    var duration: Int? = null

    @SerializedName("location_name")
    @Expose
    var locationName: String

    @SerializedName("address")
    @Expose
    var address: String

    @SerializedName("creator")
    @Expose
    var initiator: Partner

    @SerializedName("participants")
    @Expose
    var participants: List<Partner> = ArrayList()

    var locationStreetPictureUrl: String? = null

    constructor(
            title: String,
            startedAt: Date,
            duration: Int?,
            locationName: String,
            address: String,
            initiator: Partner,
            participants: List<Partner>
    ) {
        this.title = title
        this.startedAt = startedAt
        this.duration = duration
        this.locationName = locationName
        this.address = address
        this.initiator = initiator
        this.participants = participants
    }

    private constructor(`in`: Parcel) {
        title = `in`.readString()
        locationName = `in`.readString()
        address = `in`.readString()
        locationStreetPictureUrl = `in`.readString()
        `in`.readList(participants, Partner::class.java.classLoader)
        initiator = `in`.readParcelable(Partner::class.java.classLoader)
        duration = `in`.readInt()
        startedAt = Date(`in`.readLong())
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(title)
        dest.writeString(locationName)
        dest.writeString(address)
        dest.writeString(locationStreetPictureUrl)
        dest.writeList(participants)
        dest.writeParcelable(initiator, flags)
        dest.writeInt(duration!!)

        dest.writeLong(startedAt!!.time)
    }

    companion object {

        var PARCEL_NAME = "event_lv_parcel"

        val CREATOR: Parcelable.Creator<EventLV> = object : Parcelable.Creator<EventLV> {
            override fun createFromParcel(`in`: Parcel): EventLV {
                return EventLV(`in`)
            }

            override fun newArray(size: Int): Array<EventLV?> {
                return arrayOfNulls(size)
            }
        }
    }
}
