package com.loconav.lookup.model;

import com.google.gson.annotations.SerializedName;

public class Coordinates {
    @SerializedName("lat")
    private String latitude;
    @SerializedName("long")
    private String  longitude;
    @SerializedName("recorded_at")
    private long recordedat;

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public void setRecordedat(long recordedat) {
        this.recordedat = recordedat;
    }

    public String getLatitude() {
        return latitude;

    }

    public String getLongitude() {
        return longitude;
    }

    public long getRecordedat() {
        return recordedat;
    }
}
