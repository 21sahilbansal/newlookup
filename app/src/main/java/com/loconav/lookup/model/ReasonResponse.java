package com.loconav.lookup.model;

import com.google.gson.JsonArray;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.loconav.lookup.Input;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by sejal on 25-07-2018.
 */

public class ReasonResponse implements Serializable {

    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("reasons")
    @Expose
    private List<ReasonTypeResponse> reasons;

    @SerializedName("additional_fields")
    @Expose
    private ArrayList<Input> additional_fields;

    @SerializedName("icon_url")
    private String iconUrl;

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;

    }

    public ArrayList<Input> getAdditional_fields() {
        return additional_fields;
    }

    public void setAdditional_fields(ArrayList<Input> additional_fields) {
        this.additional_fields = additional_fields;
    }

    private int color;

    public ReasonResponse(int id, String name, List<ReasonTypeResponse> reasons,
                          ArrayList<Input> additional_fields, String iconUrl) {
        this.id = id;
        this.name = name;
        this.additional_fields=additional_fields;
        this.reasons = reasons;
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

    public List<ReasonTypeResponse> getReasons() {
        return reasons;
    }

    public void setReasons(List<ReasonTypeResponse> reasons) {
        this.reasons = reasons;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }
}
