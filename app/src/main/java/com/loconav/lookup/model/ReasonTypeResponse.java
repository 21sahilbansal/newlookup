package com.loconav.lookup.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by sejal on 25-07-2018.
 */

public class ReasonTypeResponse implements Serializable {

    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("icon_url")
    @Expose
    private String  iconUrl;

    @SerializedName("name")
    @Expose
    private String name;

    public ReasonTypeResponse(int id, String name, String iconUrl) {
        this.id = id;
        this.name = name;
        this.iconUrl = iconUrl;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }
}
