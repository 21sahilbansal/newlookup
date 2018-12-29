package com.loconav.lookup.model;

import com.google.gson.annotations.SerializedName;

public class Coordinates {
    @SerializedName("lat")
    private String latitude;
    @SerializedName("long")
    private String  longitude;
    @SerializedName("recorded_at")
    private long recordedAt;

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public void setRecordedAt(long recordedAt) {
        this.recordedAt = recordedAt;
    }

    public String getLatitude() {
        return latitude;

    }

    public String getLongitude() {
        return longitude;
    }

    public long getRecordedAt() {
        return recordedAt;
    }
}
