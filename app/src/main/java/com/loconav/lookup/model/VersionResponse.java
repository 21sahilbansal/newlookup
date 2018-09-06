package com.loconav.lookup.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by sejal on 09-08-2018.
 */

public class VersionResponse {
    @SerializedName("update_available")
    private Boolean update_available;
    @SerializedName("next_version")
    private int next_version;
    @SerializedName("force_update")
    private Boolean force_update;

    public Boolean getUpdate_available() {
        return update_available;
    }

    public void setUpdate_available(Boolean update_available) {
        this.update_available = update_available;
    }

    public int getNext_version() {
        return next_version;
    }

    public void setNext_version(int next_version) {
        this.next_version = next_version;
    }

    public Boolean getForce_update() {
        return force_update;
    }

    public void setForce_update(Boolean force_update) {
        this.force_update = force_update;
    }
}
