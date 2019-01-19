package com.loconav.lookup.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RepairsDataandTotalRepairCount implements Parcelable {
    @SerializedName("data")
    @Expose
    private List<Repairs> data;
    @SerializedName("total_repair_count")
    @Expose
    private int totalcount;

    public RepairsDataandTotalRepairCount()
    {

    }

    private RepairsDataandTotalRepairCount(Parcel in) {
        data = in.createTypedArrayList(Repairs.CREATOR);
        totalcount = in.readInt();
    }

    public static final Creator<RepairsDataandTotalRepairCount> CREATOR = new Creator<RepairsDataandTotalRepairCount>() {
        @Override
        public RepairsDataandTotalRepairCount createFromParcel(Parcel in) {
            return new RepairsDataandTotalRepairCount(in);
        }

        @Override
        public RepairsDataandTotalRepairCount[] newArray(int size) {
            return new RepairsDataandTotalRepairCount[size];
        }
    };

    public List<Repairs> getData() {
        return data;
    }
    public RepairsDataandTotalRepairCount(List<Repairs> data, int totalcount) {
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
