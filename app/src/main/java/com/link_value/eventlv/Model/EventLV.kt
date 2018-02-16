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

    @SerializedName("uuid")
    @Expose
    var uuid: String
    private set

    @SerializedName("title")
    @Expose
    var title: String

    @SerializedName("category")
    @Expose
    var category: Category

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
    var participants: MutableList<Partner> = ArrayList()

    @SerializedName("event_coordinates")
    @Expose
    var coordinates: EventLocationLatLng?

    var locationStreetPictureUrl: String? = null

    constructor(
            title: String,
            category: Category,
            startedAt: Date,
            duration: Int?,
            locationName: String,
            address: String,
            initiator: Partner,
            participants: MutableList<Partner>,
            coordinates: EventLocationLatLng?
    ) {
        this.uuid = UUID.randomUUID().toString()
        this.title = title
        this.category = category
        this.startedAt = startedAt
        this.duration = duration
        this.locationName = locationName
        this.address = address
        this.initiator = initiator
        this.participants = participants
        this.coordinates = coordinates
    }

    private constructor(`in`: Parcel) {
        uuid = `in`.readString()
        title = `in`.readString()
        category = `in`.readParcelable(Category::class.java.classLoader)
        locationName = `in`.readString()
        address = `in`.readString()
        locationStreetPictureUrl = `in`.readString()
        `in`.readList(participants, Partner::class.java.classLoader)
        initiator = `in`.readParcelable(Partner::class.java.classLoader)
        duration = `in`.readInt()
        startedAt = Date(`in`.readLong())
        coordinates = `in`.readParcelable(EventLocationLatLng::class.java.classLoader)
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(uuid)
        dest.writeString(title)
        dest.writeParcelable(category, flags)
        dest.writeString(locationName)
        dest.writeString(address)
        dest.writeString(locationStreetPictureUrl)
        dest.writeList(participants)
        dest.writeParcelable(initiator, flags)
        dest.writeInt(duration!!)
        dest.writeLong(startedAt!!.time)
        dest.writeParcelable(coordinates, flags)
    }

    companion object {

        var PARCEL_NAME = "event_lv_parcel"

        @JvmField val CREATOR: Parcelable.Creator<EventLV> = object : Parcelable.Creator<EventLV> {
            override fun createFromParcel(`in`: Parcel) = EventLV(`in`)
            override fun newArray(size: Int) = arrayOfNulls<EventLV>(size)
        }
    }
}
