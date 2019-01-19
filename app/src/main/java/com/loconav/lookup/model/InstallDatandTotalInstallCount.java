package com.loconav.lookup.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class InstallDatandTotalInstallCount implements Parcelable {
    @SerializedName("data")
    @Expose
    private List<Installs> data;
    @SerializedName("total_installation_count")
    @Expose
    private int totalcount;

    public InstallDatandTotalInstallCount()
    {

    }

    private InstallDatandTotalInstallCount(Parcel in) {
        data = in.createTypedArrayList(Installs.CREATOR);
        totalcount = in.readInt();
    }

    public static final Creator<InstallDatandTotalInstallCount> CREATOR = new Creator<InstallDatandTotalInstallCount>() {
        @Override
        public InstallDatandTotalInstallCount createFromParcel(Parcel in) {
            return new InstallDatandTotalInstallCount(in);
        }

        @Override
        public InstallDatandTotalInstallCount[] newArray(int size) {
            return new InstallDatandTotalInstallCount[size];
        }
    };

    public List<Installs> getData() {
        return data;
    }
    public InstallDatandTotalInstallCount(List<Installs> data, int totalcount) {
        this.data = data;
        this.totalcount = totalcount;
    }
    public int getTotalcount() {
        return totalcount;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(data);
        dest.writeInt(totalcount);
    }
}
