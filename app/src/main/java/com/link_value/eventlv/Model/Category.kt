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

    @SerializedName("slug")
    @Expose
    var slug: String

    override fun toString(): String {
        return name
    }

    constructor(name: String, slug: String) {
        this.name = name
        this.slug = slug
    }
    private constructor(`in`: Parcel) {
        name = `in`.readString()
        slug = `in`.readString()
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(name)
        dest.writeString(slug)
    }

    companion object {
        var PARCEL_NAME = "category_parcel"

        @JvmField val CREATOR: Parcelable.Creator<Category> = object : Parcelable.Creator<Category> {
            override fun createFromParcel(`in`: Parcel) = Category(`in`)
            override fun newArray(size: Int) = arrayOfNulls<Category>(size)
        }
    }
}