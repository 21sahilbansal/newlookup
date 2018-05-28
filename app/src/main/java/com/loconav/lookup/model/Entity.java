package com.loconav.lookup.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by prateek on 12/05/18.
 */

public class Entity implements Serializable {
    @SerializedName("title")
    private String title;
    @SerializedName("value")
    private String value;
    @SerializedName("status")
    private Boolean status;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }
}
