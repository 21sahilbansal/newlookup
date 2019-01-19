package com.loconav.lookup.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Coordinates implements Parcelable {
    @SerializedName("lat")
    private String latitude;
    @SerializedName("long")
    private String  longitude;
    @SerializedName("recorded_at")
    private long recordedAt;

    public Coordinates() {
    }

    public Coordinates(Parcel in) {
        latitude = in.readString();
        longitude = in.readString();
        recordedAt = in.readLong();
    }

    public static final Creator<Coordinates> CREATOR = new Creator<Coordinates>() {
        @Override
        public Coordinates createFromParcel(Parcel in) {
            return new Coordinates(in);
        }

        @Override
        public Coordinates[] newArray(int size) {
            return new Coordinates[size];
        }
    };

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public void setRecordedAt(long recordedAt) {
        this.recordedAt = recordedAt;
    }

    public String getLatitude() {
        return latitude;

    }

    public String getLongitude() {
        return longitude;
    }

    public long getRecordedAt() {
        return recordedAt;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(latitude);
        dest.writeString(longitude);
        dest.writeLong(recordedAt);
    }
}
