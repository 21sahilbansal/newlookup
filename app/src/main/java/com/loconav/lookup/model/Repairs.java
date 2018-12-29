package com.loconav.lookup.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.LinkedHashMap;
import java.util.List;

public class Repairs {


    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("serial_number")
    @Expose
    private String serialNumber;
    @SerializedName("truck_number")
    @Expose
    private String truckNumber;
    @SerializedName("repair_reason")
    @Expose
    private String repairReason;
    @SerializedName("repair_data")
    @Expose
    private LinkedHashMap<String,String> repairData;

    @SerializedName("pre_repair_images")
    @Expose
    private Integer preRepairImages;
    @SerializedName("post_repair_images")
    @Expose
    private Integer postRepairImages;
    @SerializedName("remarks")
    @Expose
    private String remarks;
    @SerializedName("created_at")
    @Expose
    private long createdAt;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getTruckNumber() {
        return truckNumber;
    }

    public void setTruckNumber(String truckNumber) {
        this.truckNumber = truckNumber;
    }

    public String getRepairReason() {
        return repairReason;
    }

    public void setRepairReason(String repairReason) {
        this.repairReason = repairReason;
    }

    public LinkedHashMap<String, String> getRepairData() {
        return repairData;
    }

    public void setRepairData(LinkedHashMap repairData) {
        this.repairData = repairData;
    }

    public Integer getPreRepairImages() {
        return preRepairImages;
    }

    public void setPreRepairImages(Integer preRepairImages) {
        this.preRepairImages = preRepairImages;
    }

    public Integer getPostRepairImages() {
        return postRepairImages;
    }

    public void setPostRepairImages(Integer postRepairImages) {
        this.postRepairImages = postRepairImages;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

}
