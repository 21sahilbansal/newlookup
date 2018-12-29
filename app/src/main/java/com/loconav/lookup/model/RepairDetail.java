package com.loconav.lookup.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import okhttp3.ResponseBody;

public class RepairDetail {
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
    private LinkedHashMap<String,String>  repairData;
    @SerializedName("pre_repair_images")
    @Expose
    private List<String> preRepairImages=new ArrayList<>();
    @SerializedName("post_repair_images")
    @Expose
    private List<String> postRepairImages= new ArrayList<>();
    @SerializedName("remarks")
    @Expose
    private String remarks;
    @SerializedName("created_at")
    @Expose
    private long createdAt;
    @SerializedName("audit_status")
    private String auditStatus;
    @SerializedName("audit_notes")
    private String auditNotes;

    public String getAuditStatus() {
        return auditStatus;
    }

    public void setAuditStatus(String auditStatus) {
        this.auditStatus = auditStatus;
    }

    public String getAuditNotes() {
        return auditNotes;
    }

    public void setAuditNotes(String auditNotes) {
        this.auditNotes = auditNotes;
    }

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

    public LinkedHashMap<String,String>  getRepairData() {
        return repairData;
    }

    public void setRepairData(LinkedHashMap<String,String>   repairData) {
        this.repairData = repairData;
    }

    public List<String> getPreRepairImages() {
        return preRepairImages;
    }

    public void setPreRepairImages(List<String> preRepairImages) {
        this.preRepairImages = preRepairImages;
    }

    public List<String> getPostRepairImages() {
        return postRepairImages;
    }

    public void setPostRepairImages(List<String> postRepairImages) {
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
