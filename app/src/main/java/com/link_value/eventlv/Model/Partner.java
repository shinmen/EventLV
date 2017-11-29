package com.link_value.eventlv.Model;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by julienb on 29/11/17.
 */

public class Partner implements Parcelable {
    @SerializedName("username")
    @Expose
    protected String username;

    @SerializedName("avatarUrl")
    @Expose
    protected String avatarUrl;
    protected Bitmap avatar;

    public Partner() {
    }

    public Partner(String username, Bitmap avatar) {
        this.username = username;
        this.avatar = avatar;
    }

    public Partner(String username, String avatarUrl) {
        this.username = username;
        this.avatarUrl = avatarUrl;
    }

    protected Partner(Parcel in) {
        username = in.readString();
        avatar = in.readParcelable(Bitmap.class.getClassLoader());
        avatarUrl = in.readString();
    }

    public static final Creator<Partner> CREATOR = new Creator<Partner>() {
        @Override
        public Partner createFromParcel(Parcel in) {
            return new Partner(in);
        }

        @Override
        public Partner[] newArray(int size) {
            return new Partner[size];
        }
    };

    public String getUsername() {
        return username;
    }

    public Bitmap getAvatar() {
        return avatar;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(username);
        dest.writeParcelable(avatar, flags);
        dest.writeString(avatarUrl);
    }

    public void loadAvatar(Bitmap bitmap) {
        avatar = bitmap;
    }
}
