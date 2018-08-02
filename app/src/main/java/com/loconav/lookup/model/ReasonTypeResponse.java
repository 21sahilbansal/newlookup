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

    public ReasonTypeResponse(int id, String name) {
        this.id = id;
        this.name = name;
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

    @SerializedName("name")
    @Expose
    private String name;


}
