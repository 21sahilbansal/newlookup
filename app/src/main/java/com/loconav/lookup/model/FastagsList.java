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

}
