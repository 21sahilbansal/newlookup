package com.loconav.lookup.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by sejal on 18-07-2018.
 */

public class InstallationRequirements implements Parcelable {

    @SerializedName("truck_id")
    private int truck_id;

    @SerializedName("fastag_id")
    private int fastag_id;

    public InstallationRequirements()
    {

    }

    protected InstallationRequirements(Parcel in) {
        truck_id = in.readInt();
        fastag_id = in.readInt();
    }

    public static final Creator<InstallationRequirements> CREATOR = new Creator<InstallationRequirements>() {
        @Override
        public InstallationRequirements createFromParcel(Parcel in) {
            return new InstallationRequirements(in);
        }

        @Override
        public InstallationRequirements[] newArray(int size) {
            return new InstallationRequirements[size];
        }
    };

    public int getTruck_id() {
        return truck_id;
    }

    public void setTruck_id(int truck_id) {
        this.truck_id = truck_id;
    }

    public int getFastag_id() {
        return fastag_id;
    }

    public void setFastag_id(int fastag_id) {
        this.fastag_id = fastag_id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(truck_id);
        dest.writeInt(fastag_id);
    }
}
