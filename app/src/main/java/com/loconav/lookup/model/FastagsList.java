package com.loconav.lookup.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by sejal on 04-07-2018.
 */

public class FastagsList implements Parcelable {
    @SerializedName("serial_number")
    private String serialNumber;
    @SerializedName("id")
    private int id;
    @SerializedName("color")
    private String color;

    public FastagsList()
    {

    }

    protected FastagsList(Parcel in) {
        serialNumber = in.readString();
        id = in.readInt();
        color = in.readString();
    }

    public static final Creator<FastagsList> CREATOR = new Creator<FastagsList>() {
        @Override
        public FastagsList createFromParcel(Parcel in) {
            return new FastagsList(in);
        }

        @Override
        public FastagsList[] newArray(int size) {
            return new FastagsList[size];
        }
    };

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public FastagsList(String serialNumber, int id, String color) {
        this.serialNumber = serialNumber;
        this.id = id;
        this.color = color;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(serialNumber);
        dest.writeInt(id);
        dest.writeString(color);
    }
}
