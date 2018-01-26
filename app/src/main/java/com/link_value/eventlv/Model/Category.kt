package com.link_value.eventlv.Model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.util.*

/**
 * Created by julienb on 26/01/18.
 */
class Category: Parcelable {
    @SerializedName("name")
    @Expose
    var name: String

    override fun toString(): String {
        return name
    }

    constructor(name: String) {
        this.name = name
    }
    private constructor(`in`: Parcel) {
        name = `in`.readString()
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(name)
    }

    companion object {
        var PARCEL_NAME = "category_parcel"

        @JvmField val CREATOR: Parcelable.Creator<Category> = object : Parcelable.Creator<Category> {
            override fun createFromParcel(`in`: Parcel) = Category(`in`)
            override fun newArray(size: Int) = arrayOfNulls<Category>(size)
        }
    }
}