package com.loconav.lookup.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RepairsDataandTotalRepairCount {
    @SerializedName("data")
    @Expose
    private List<Repairs> data;
    @SerializedName("total_repair_count")
    @Expose
    private int totalcount;
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


}
