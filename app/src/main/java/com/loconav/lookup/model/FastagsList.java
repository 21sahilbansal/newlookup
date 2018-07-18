package com.loconav.lookup.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by sejal on 04-07-2018.
 */

public class FastagsList implements Serializable {
    @SerializedName("serial_number")
    private String serialNumber;
    @SerializedName("id")
    private int id;
    @SerializedName("color")
    private String color;

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public FastagsList(String serialNumber, int id, String color) {
        this.serialNumber = serialNumber;
        this.id = id;
        this.color = color;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
