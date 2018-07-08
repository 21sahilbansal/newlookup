package com.loconav.lookup.model;

import android.content.Intent;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by sejal on 29-06-2018.
 */

public class VehiclesList implements Serializable {

    @SerializedName("number")
    private String number;
    @SerializedName("id")
    private int id;

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public int getId() {
        return id;
    }

    public VehiclesList(String number, int id) {
        this.number = number;
        this.id = id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
