package com.loconav.lookup.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by sejal on 18-07-2018.
 */

public class InstallationRequirements {

    @SerializedName("truck_id")
    private int truck_id;

    @SerializedName("fastag_id")
    private int fastag_id;

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
}
