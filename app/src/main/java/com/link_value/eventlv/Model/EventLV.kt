package com.link_value.eventlv.Model

import android.os.Parcel
import android.os.Parcelable

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

import java.util.ArrayList
import java.util.Calendar
import java.util.GregorianCalendar
import java.util.TimeZone

/**
 * Created by julienb on 29/11/17.
 */

class EventLV : Parcelable {

    @SerializedName("title")
    @Expose
    var title: String
        protected set

    @SerializedName("startedAt")
    @Expose
    var startedAt: Calendar
        protected set

    @SerializedName("duration")
    @Expose
    var duration: Int? = null
        protected set

    @SerializedName("location_name")
    @Expose
    var locationName: String
        protected set

    @SerializedName("address")
    @Expose
    var address: String
        protected set

    @SerializedName("creator")
    @Expose
    var initiator: Partner
        protected set

    @SerializedName("participants")
    @Expose
    var participants: List<Partner> = ArrayList()
        protected set

    var locationStreetPictureUrl: String = ""

    val endedAt: Calendar
        get() {
            val endedAt = startedAt.clone() as Calendar
            endedAt.add(Calendar.HOUR, duration!!)

            return endedAt
        }

    constructor(title: String, startedAt: Calendar, duration: Int?, locationName: String, address: String, initiator: Partner, participants: List<Partner>) {
        this.title = title
        this.startedAt = startedAt
        this.duration = duration
        this.locationName = locationName
        this.address = address
        this.initiator = initiator
        this.participants = participants
    }

    protected constructor(`in`: Parcel) {
        title = `in`.readString()
        locationName = `in`.readString()
        address = `in`.readString()
        locationStreetPictureUrl = `in`.readString()
        `in`.readList(participants, Partner::class.java.classLoader)
        initiator = `in`.readParcelable(Partner::class.java.classLoader)
        duration = `in`.readInt()

        val milliseconds = `in`.readLong()
        val timezoneId = `in`.readString()
        startedAt = GregorianCalendar(TimeZone.getTimeZone(timezoneId))
        startedAt.timeInMillis = milliseconds
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

        dest.writeLong(startedAt.timeInMillis)
        dest.writeString(startedAt.timeZone.id)
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