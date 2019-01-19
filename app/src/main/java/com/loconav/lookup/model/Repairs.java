package com.loconav.lookup.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.LinkedHashMap;

public class Repairs implements Parcelable {


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
    @SerializedName("audit_status")
    private String auditStatus=null;

    public Repairs()
    {

    }

    private Repairs(Parcel in) {
        id = in.readInt();
        serialNumber = in.readString();
        truckNumber = in.readString();
        repairReason = in.readString();
        if (in.readByte() == 0) {
            preRepairImages = null;
        } else {
            preRepairImages = in.readInt();
        }
        if (in.readByte() == 0) {
            postRepairImages = null;
        } else {
            postRepairImages = in.readInt();
        }
        remarks = in.readString();
        createdAt = in.readLong();
        auditStatus = in.readString();
    }

    public static final Creator<Repairs> CREATOR = new Creator<Repairs>() {
        @Override
        public Repairs createFromParcel(Parcel in) {
            return new Repairs(in);
        }

        @Override
        public Repairs[] newArray(int size) {
            return new Repairs[size];
        }
    };

    public String getAuditStatus() {
        return auditStatus;
    }

    public void setAuditStatus(String auditStatus) {
        this.auditStatus = auditStatus;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(serialNumber);
        dest.writeString(truckNumber);
        dest.writeString(repairReason);
        if (preRepairImages == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(preRepairImages);
        }
        if (postRepairImages == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(postRepairImages);
        }
        dest.writeString(remarks);
        dest.writeLong(createdAt);
        dest.writeString(auditStatus);
    }
}
