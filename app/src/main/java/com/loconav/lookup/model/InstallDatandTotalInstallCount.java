package com.loconav.lookup.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class InstallDatandTotalInstallCount {
    @SerializedName("data")
    @Expose
    private List<Installs> data;
    @SerializedName("total_installation_count")
    @Expose
    private int totalcount;
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
}
