package com.loconav.lookup.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by sejal on 26-07-2018.
 */

public class RepairRequirements implements Serializable {

    @SerializedName("device_serial_number")
    @Expose
    private String device_serial_number;

    @SerializedName("reason_id")
    @Expose
    private int reason_id;

    @SerializedName("remarks")
    @Expose
    private String remarks;

    @SerializedName("repair_data")
    @Expose
    private String repair_data;

    @SerializedName("pre_repair_images")
    @Expose
    private ArrayList<String> pre_repair_images;

    @SerializedName("post_repair_images")
    @Expose
    private ArrayList<String> post_repair_images;

    public RepairRequirements(){}

    public RepairRequirements(String device_serial_number, int reason_id, String remarks, String repair_data, ArrayList<String> pre_repair_images, ArrayList<String> post_repair_images) {
        this.device_serial_number =device_serial_number;
        this.reason_id =reason_id;
        this.remarks =remarks;
        this.repair_data =repair_data;
         this.pre_repair_images=pre_repair_images;
        this.post_repair_images =post_repair_images;
    }

    public String getDevice_id() {
        return device_serial_number;
    }

    public void setDevice_id(String device_id) {
        this.device_serial_number = device_id;
    }

    public int getReason_id() {
        return reason_id;
    }

    public void setReason_id(int reason_id) {
        this.reason_id = reason_id;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getRepair_data() {
        return repair_data;
    }

    public void setRepair_data(String repair_data) {
        this.repair_data = repair_data;
    }

    public ArrayList<String> getPre_repair_images() {
        return pre_repair_images;
    }

    public void setPre_repair_images(ArrayList<String> pre_repair_images) {
        this.pre_repair_images = pre_repair_images;
    }

    public ArrayList<String> getPost_repair_images() {
        return post_repair_images;
    }

    public void setPost_repair_images(ArrayList<String> post_repair_images) {
        this.post_repair_images = post_repair_images;
    }
}
