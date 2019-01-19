package com.loconav.lookup.model;

import android.content.Intent;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by sejal on 29-06-2018.
 */

public class VehiclesList implements Parcelable {

    @SerializedName("number")
    private String number;
    @SerializedName("id")
    private int id;

    public VehiclesList()
    {

    }

    protected VehiclesList(Parcel in) {
        number = in.readString();
        id = in.readInt();
    }

    public static final Creator<VehiclesList> CREATOR = new Creator<VehiclesList>() {
        @Override
        public VehiclesList createFromParcel(Parcel in) {
            return new VehiclesList(in);
        }

        @Override
        public VehiclesList[] newArray(int size) {
            return new VehiclesList[size];
        }
    };

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public int getId() {
        return id;
    }

    public VehiclesList(String number, int id) {
        this.number = number;
        this.id = id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(number);
        dest.writeInt(id);
    }
}
