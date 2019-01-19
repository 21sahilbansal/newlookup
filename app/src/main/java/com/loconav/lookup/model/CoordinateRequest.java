package com.loconav.lookup.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class CoordinateRequest implements Parcelable {
    protected CoordinateRequest(Parcel in) {
        coordinates = in.createTypedArrayList(Coordinates.CREATOR);
    }

    public CoordinateRequest()
    {

    }

    public static final Creator<CoordinateRequest> CREATOR = new Creator<CoordinateRequest>() {
        @Override
        public CoordinateRequest createFromParcel(Parcel in) {
            return new CoordinateRequest(in);
        }

        @Override
        public CoordinateRequest[] newArray(int size) {
            return new CoordinateRequest[size];
        }
    };

    public List<Coordinates> getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(List<Coordinates> coordinates) {
        this.coordinates = coordinates;
    }

    @SerializedName("coordinates")

    private List<Coordinates> coordinates;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(coordinates);
    }
}
